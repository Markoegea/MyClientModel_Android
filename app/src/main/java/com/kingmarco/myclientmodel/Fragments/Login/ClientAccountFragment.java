package com.kingmarco.myclientmodel.Fragments.Login;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kingmarco.myclientmodel.Auxiliary.Classes.Holders.ClientHolder;
import com.kingmarco.myclientmodel.Auxiliary.Classes.GlideApp;
import com.kingmarco.myclientmodel.Auxiliary.Classes.Static.FragmentAnimation;
import com.kingmarco.myclientmodel.Auxiliary.Classes.Static.InAppSnackBars;
import com.kingmarco.myclientmodel.Auxiliary.Classes.SyncFirebase.SyncAuthDB;
import com.kingmarco.myclientmodel.Auxiliary.Classes.SyncFirebase.SyncRealtimeDB;
import com.kingmarco.myclientmodel.Auxiliary.Enums.SnackBarsInfo;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.ClientObserver;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.GetFireStoreDB;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.ProgressBarBehavior;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.SetLabelName;
import com.kingmarco.myclientmodel.POJOs.Clients;
import com.kingmarco.myclientmodel.R;

import java.util.UUID;

/**This fragment show the account of the client, with the location an the image with the card views to change the information*/
public class ClientAccountFragment extends Fragment implements ClientObserver, GetFireStoreDB {

    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private NavController nav;
    private View contentView;
    private SetLabelName setLabelName;
    private ProgressBarBehavior progressBarBehavior;
    private ImageView ivClient;
    private Button btnUploadImage;
    private CardView cvPersonalData, cvLocationData, cvLoginData, cvLogOut;
    private TextView txtInfo;
    private Clients client;

    /**Request the image for the storage*/
    private final ActivityResultLauncher<String> imageActivity = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            this::getImage
    );

    public ClientAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLabelName = (SetLabelName) getContext();
        progressBarBehavior = (ProgressBarBehavior) getContext();
        nav = NavHostFragment.findNavController(this);
    }

    /**Set the views for the fragment, the images and card views*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_client_account, container, false);
        setViews(contentView);
        setLabelName.setLabelName("Cuenta");
        // Inflate the layout for this fragment
        return contentView;
    }

    private void setViews(View view){
        ivClient = view.findViewById(R.id.ivClient);
        btnUploadImage = view.findViewById(R.id.btnUploadImage);
        txtInfo = view.findViewById(R.id.txtInfo);
        cvPersonalData = view.findViewById(R.id.cvPersonalData);
        cvLocationData = view.findViewById(R.id.cvLocationData);
        cvLoginData = view.findViewById(R.id.cvLoginData);
        cvLogOut = view.findViewById(R.id.cvLogOut);
    }

    @Override
    public void onVariableChange(Clients client) {
        if(client == null){return;}
        this.client = client;
        if (client.getImageUrl() != null){
            StorageReference gsReference = storage.getReferenceFromUrl(client.getImageUrl());
            GlideApp.with(contentView)
                    .load(gsReference)
                    .apply(RequestOptions.circleCropTransform())
                    .into(ivClient);
        }
        setListeners(client);
    }

    private void setListeners(Clients client){
        /**Navigate to other fragments*/
        txtInfo.setText(client.getDirections());
        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageActivity.launch("image/*");
            }
        });
        cvPersonalData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nav.navigate(R.id.changeInfoAccountFragment,null,FragmentAnimation.navigateBehavior());
            }
        });
        cvLocationData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nav.navigate(R.id.changeLocationDataFragment,null,FragmentAnimation.navigateBehavior());
            }
        });
        cvLoginData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nav.navigate(R.id.changeLoginDataFragment,null,FragmentAnimation.navigateBehavior());
            }
        });
        cvLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogOut();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!SyncAuthDB.getInstance().isLogin() || ClientHolder.getYouClient() == null){
            onLogOut();
            return;
        }
        ClientHolder.addObserver(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        ClientHolder.removeObserver(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        ClientHolder.removeObserver(this);
    }

    private void getImage(Uri uri){
        if (uri != null){
            deleteImage(client);
            uploadImage(client,uri);
        }
    }

    private void deleteImage(Clients client) {
        StorageReference imageRef = storage.getReferenceFromUrl(client.getImageUrl());
        imageRef.delete();
    }

    private void uploadImage(Clients client, Uri uri) {
        progressBarBehavior.startProgressBar();
        StorageReference clientStorageReference = storage.getReference("Clientes/"+client.getId());
        int i = UUID.randomUUID().hashCode();
        StorageReference imageRef = clientStorageReference.child("image_"+i);
        imageRef.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    StorageReference storageReference = task.getResult().getStorage();
                    String imageUrl = storageReference.toString();
                    client.setImageUrl(imageUrl);
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    DocumentReference documentReference = db.collection("Clientes").document(client.getId());
                    documentReference.set(client).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("chats/"+client.getMessagingId());
                                SyncRealtimeDB.uploadChat(databaseReference,imageUrl,"image");
                                onCompleteFireStoreRequest(SnackBarsInfo.UPDATE_SUCCESS);
                            } else{
                                onCompleteFireStoreRequest(SnackBarsInfo.DATA_ERROR);
                            }
                        }
                    });
                } else {
                    onCompleteFireStoreRequest(SnackBarsInfo.IMAGES_ERROR);
                }
            }
        });
    }

    public void onLogOut(){
        SyncAuthDB.getInstance().logOut();
        onCompleteFireStoreRequest(SnackBarsInfo.LOG_OUT_SUCCESS);
        if (getActivity() != null){
            getActivity().onBackPressed();
            return;
        }
        NavController nav = NavHostFragment.findNavController(this);
        nav.navigate(R.id.loginFragment,null, FragmentAnimation.navigateBehavior());
    }

    @Override
    public void onCompleteFireStoreRequest(SnackBarsInfo snackBarsInfo) {
        InAppSnackBars.defineSnackBarInfo(snackBarsInfo,contentView,getContext(),getActivity(),false);
        progressBarBehavior.stopProgressBar();
    }
}