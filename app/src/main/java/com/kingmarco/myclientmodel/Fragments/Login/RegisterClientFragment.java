package com.kingmarco.myclientmodel.Fragments.Login;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kingmarco.myclientmodel.Auxiliary.Classes.Static.InAppSnackBars;
import com.kingmarco.myclientmodel.Auxiliary.Classes.SyncFirebase.SyncAuthDB;
import com.kingmarco.myclientmodel.Auxiliary.Enums.SnackBarsInfo;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.GetAuthDB;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.GetFireStoreDB;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.SetLabelName;
import com.kingmarco.myclientmodel.POJOs.Clients;
import com.kingmarco.myclientmodel.R;

import java.util.UUID;

/**This fragment is responsable to let the user register itself in the database*/
public class RegisterClientFragment extends Fragment implements GetFireStoreDB, GetAuthDB {

    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private View contentView;
    private SetLabelName setLabelName;
    private ImageView ivClient;
    private FloatingActionButton btnRegister;
    private Button btnUploadImage;
    private EditText edtEmail, edtPassword, edtRePassword, edtDocumentId, edtName, edtLastName, edtAge, edtPhoneNumber;
    private Spinner documentTypeSpinner;
    private Uri imageUri;

    /**Request the image for the storage*/
    private final ActivityResultLauncher<String> getImage = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    imageUri = result;
                    if(imageUri != null){
                        Glide.with(RegisterClientFragment.this.contentView)
                                .load(imageUri)
                                .apply(RequestOptions.circleCropTransform())
                                .into(ivClient);
                    }
                }
            }
    );

    public RegisterClientFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLabelName = (SetLabelName) getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_register_client, container, false);
        setViews(contentView);
        setListeners();
        setLabelName.setLabelName("Registrarse");
        // Inflate the layout for this fragment
        return this.contentView;
    }

    /**Set the views of the fragment*/
    private void setViews(View view){
        ivClient = view.findViewById(R.id.ivClient);

        btnUploadImage = view.findViewById(R.id.btnUploadImage);
        btnRegister = view.findViewById(R.id.btnRegister);

        edtEmail = view.findViewById(R.id.edtEmail);
        edtPassword = view.findViewById(R.id.edtPassword);
        edtRePassword = view.findViewById(R.id.edtRePassword);
        edtDocumentId = view.findViewById(R.id.edtDocumentId);
        edtName = view.findViewById(R.id.edtName);
        edtLastName = view.findViewById(R.id.edtLastName);
        edtAge = view.findViewById(R.id.edtAge);
        edtPhoneNumber = view.findViewById(R.id.edtPhoneNumber);

        documentTypeSpinner = view.findViewById(R.id.documentTypeSpinner);
    }

    private void setListeners(){
        btnUploadImage.setOnClickListener(this::onClickUploadImage);
        btnRegister.setOnClickListener(this::onClickRegisterButton);
    }

    private void onClickUploadImage(View view){
        getImage.launch("image/*");
    }

    /**If all is correct, add the client to the database*/
    private void onClickRegisterButton(View view){
        if(SyncAuthDB.getInstance().isLogin()){
            SyncAuthDB.getInstance().logOut();
            getActivity().onBackPressed();
            return;
        }
        if (!sanitizeData()) {
            onCompleteFireStoreRequest(SnackBarsInfo.INCOMPLETE_INFO_ERROR);
            return;
        }
        if(edtPassword.getText().toString().length()<7){
            onCompleteFireStoreRequest(SnackBarsInfo.PASSWORD_LENGTH_ERROR);
            return;
        }
        SyncAuthDB.getInstance().registerClient(edtEmail.getText().toString(),
                edtPassword.getText().toString(),
                this,
                this);
    }

    /**Sanitize the data*/
    private boolean sanitizeData(){
        if (edtEmail.getText().toString().isEmpty()) {
            edtEmail.requestFocus();
            return false;
        } else if(edtPassword.getText().toString().isEmpty()){
            edtPassword.requestFocus();
            return false;
        }else if(edtRePassword.getText().toString().isEmpty()){
            edtRePassword.requestFocus();
            return false;
        }else if(edtDocumentId.getText().toString().isEmpty()){
            edtDocumentId.requestFocus();
            return false;
        }else if(edtName.getText().toString().isEmpty()){
            edtName.requestFocus();
            return false;
        }else if(edtLastName.getText().toString().isEmpty()){
            edtLastName.requestFocus();
            return false;
        }else if(edtAge.getText().toString().isEmpty()){
            edtAge.requestFocus();
            return false;
        }else if(edtPhoneNumber.getText().toString().isEmpty()){
            edtPhoneNumber.requestFocus();
            return false;
        } else if(!edtPassword.getText().toString().equals(edtRePassword.getText().toString())){
            edtRePassword.requestFocus();
            return false;
        } else if (imageUri == null){
            ivClient.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void onAuthStateChange(@NonNull FirebaseAuth firebaseAuth) {
        if(!SyncAuthDB.getInstance().isLogin()){return;}
        checkDocument();
    }

    private void checkDocument(){
        if(firebaseAuth.getCurrentUser() == null){return;}
        String id = firebaseAuth.getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Clientes").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (!SyncAuthDB.getInstance().isLogin()){return;}
                if (task.isSuccessful()){
                    if (!task.getResult().exists()){
                        addEmployee();
                    } else{
                        onCompleteFireStoreRequest(SnackBarsInfo.EXIST_ERROR);
                    }
                } else{
                    onCompleteFireStoreRequest(SnackBarsInfo.DATA_ERROR);
                }
            }
        });
    }

    private void addEmployee() {
        if(!SyncAuthDB.getInstance().isLogin()){return;}
        String clientUID = firebaseAuth.getCurrentUser().getUid();
        StorageReference clientStorageReference = storage.getReference("Clientes/"+clientUID);
        int i = UUID.randomUUID().hashCode();
        StorageReference imageRef = clientStorageReference.child("image_"+i);
        imageRef.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    StorageReference storageReference = task.getResult().getStorage();
                    String imageUrl = storageReference.toString();

                    Clients client = new Clients();
                    client.setId(clientUID);
                    client.setImageUrl(imageUrl);
                    client.setMessagingId(i);
                    client.setDocumentID(edtDocumentId.getText().toString());
                    client.setDocumentType(documentTypeSpinner.getSelectedItem().toString());
                    client.setName(edtName.getText().toString());
                    client.setLastName(edtLastName.getText().toString());
                    client.setAge(Integer.parseInt(edtAge.getText().toString()));
                    client.setPhoneNumber(edtPhoneNumber.getText().toString());

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    DocumentReference documentReference = db.collection("Clientes").document(client.getId());
                    documentReference.set(client).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                onCompleteFireStoreRequest(SnackBarsInfo.UPLOAD_SUCCESS);
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

    @Override
    public void onCompleteFireStoreRequest(SnackBarsInfo snackBarsInfo) {
        InAppSnackBars.defineSnackBarInfo(snackBarsInfo,contentView,getContext(),getActivity(),true);
    }
}