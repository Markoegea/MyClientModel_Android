package com.kingmarco.myclientmodel.Fragments.Login;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kingmarco.myclientmodel.Auxiliary.SetLabelName;
import com.kingmarco.myclientmodel.POJOs.Clients;
import com.kingmarco.myclientmodel.R;

/**This fragment show the account of the client, with the location an the image with the card views to change the information*/
public class ClientAccountFragment extends Fragment {
    //TODO: This fragment cannot pass across LoginFragment, if the user login, fix the  nav graph
    private View contentView;
    private SetLabelName setLabelName;
    private ImageView ivClient;
    private Button btnUploadImage;
    private CardView cvPersonalData, cvLocationData, cvLoginData;
    private TextView txtInfo;
    private Clients client;
    private Uri imageUri;

    /**Request the image for the storage*/
    private ActivityResultLauncher<String> getImage = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    imageUri = result;
                    if(imageUri != null){
                        Glide.with(ClientAccountFragment.this.contentView)
                                .load(imageUri)
                                .apply(RequestOptions.circleCropTransform())
                                .into(ivClient);
                        client.setImageUrl(imageUri.toString());
                    }
                }
            }
    );

    public ClientAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLabelName = (SetLabelName) getContext();
        Bundle data = getArguments();
        client = data.getParcelable("client");
    }

    /**Set the views for the fragment, the images and card views*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_client_account, container, false);
        setViews(contentView);
        setInformation();
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

    }

    private void setInformation(){
        txtInfo.setText(client.getDirections());
        /**Load the image*/
        if(client != null){
            if(client.getImageUrl() != null){
                Glide.with(contentView)
                        .load(client.getImageUrl())
                        .apply(RequestOptions.circleCropTransform())
                        .into(ivClient);
            }
        }
        btnUploadImage.setOnClickListener(this::onBtnUploadImage);

        /**Navigate to other fragments*/
        Bundle bundle = new Bundle();
        bundle.putParcelable("client",client);
        NavController nav = NavHostFragment.findNavController(ClientAccountFragment.this);
        cvPersonalData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nav.navigate(R.id.action_clientAccountFragment_to_changeInfoAccountFragment,bundle);
            }
        });
        cvLocationData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nav.navigate(R.id.action_clientAccountFragment_to_changeLocationDataFragment,bundle);
            }
        });
        cvLoginData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nav.navigate(R.id.action_clientAccountFragment_to_changeLoginDataFragment,bundle);
            }
        });
    }

    private void onBtnUploadImage(View view){
        getImage.launch("image/*");
    }
}