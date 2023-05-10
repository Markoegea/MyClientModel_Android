package com.kingmarco.myclientmodel.Fragments.Login;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.kingmarco.myclientmodel.Auxiliary.InAppSnackBars;
import com.kingmarco.myclientmodel.Auxiliary.SetLabelName;
import com.kingmarco.myclientmodel.POJOs.Clients;
import com.kingmarco.myclientmodel.R;

/**This fragment is responsable to let the user register itself in the database*/
public class RegisterClientFragment extends Fragment implements InAppSnackBars {

    private View contentView;
    private SetLabelName setLabelName;
    private ImageView ivClient;
    private Button btnUploadImage, btnRegister;
    private EditText edtEmail, edtPassword, edtRePassword, edtDocumentId, edtName, edtLastName, edtAge, edtPhoneNumber;
    private Spinner documentTypeSpinner;
    private Uri imageUri;

    /**Request the image for the storage*/
    private ActivityResultLauncher<String> getImage = registerForActivityResult(
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
        this.contentView = inflater.inflate(R.layout.fragment_register_client, container, false);

        setViews(this.contentView);

        setLabelName.setLabelName("Registro");
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

        btnUploadImage.setOnClickListener(this::onClickUploadImage);
        btnRegister.setOnClickListener(this::onClickRegisterButton);
    }

    private void onClickUploadImage(View view){
        getImage.launch("image/*");
    }

    /**If all is correct, add the client to the database*/
    private void onClickRegisterButton(View view){
        if (sanitizeData())
        {
            Clients client = new Clients();
            if(imageUri != null){
                client.setImageUrl(imageUri.toString());
            }
            client.setEmail(edtEmail.getText().toString());
            client.setPassword(edtPassword.getText().toString());
            client.setDocumentID(edtDocumentId.getText().toString());
            client.setDocumentType(documentTypeSpinner.getSelectedItem().toString());
            client.setName(edtName.getText().toString());
            client.setLastName(edtLastName.getText().toString());
            client.setAge(Integer.parseInt(edtAge.getText().toString()));
            client.setPhoneNumber(edtPhoneNumber.getText().toString());
            Clients.clients.add(client);
            showSnackBar("Registro Exitoso");
            getActivity().onBackPressed();
        }
    }

    /**Sanitize the data*/
    private boolean sanitizeData(){
        if (edtEmail.getText().toString().isEmpty()) {
            showSnackBar("Se requiere un email.");
            edtEmail.requestFocus();
            return false;
        } else if(edtPassword.getText().toString().isEmpty()){
            showSnackBar("Falta la contraseña.");
            edtPassword.requestFocus();
            return false;
        }else if(edtRePassword.getText().toString().isEmpty()){
            showSnackBar("Falta la confirmacion de la contraseña.");
            edtRePassword.requestFocus();
            return false;
        }else if(edtDocumentId.getText().toString().isEmpty()){
            showSnackBar("Se requiere numero de documento.");
            edtDocumentId.requestFocus();
            return false;
        }else if(edtName.getText().toString().isEmpty()){
            showSnackBar("Se requiere un nombre.");
            edtName.requestFocus();
            return false;
        }else if(edtLastName.getText().toString().isEmpty()){
            showSnackBar("Se requiere un apellido.");
            edtLastName.requestFocus();
            return false;
        }else if(edtAge.getText().toString().isEmpty()){
            showSnackBar("Falta la edad.");
            edtAge.requestFocus();
            return false;
        }else if(edtPhoneNumber.getText().toString().isEmpty()){
            showSnackBar("Se requiere su numero de telefono.");
            edtPhoneNumber.requestFocus();
            return false;
        } else if(!edtPassword.getText().toString().equals(edtRePassword.getText().toString())){
            showSnackBar("Las contraseñas no coinciden.");
            edtRePassword.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void showSnackBar(String text) {
        Snackbar snackbar = Snackbar.make(contentView,text, Snackbar.LENGTH_SHORT)
                .setTextColor(getContext().getColor(R.color.white))
                .setBackgroundTint(getContext().getColor(R.color.black));
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackbar.getView().getLayoutParams();
        params.gravity = Gravity.TOP;
        snackbar.getView().setLayoutParams(params);
        snackbar.show();
    }

    @Override
    public void showSnackBar(String text, int backgroundColor) {
        Snackbar.make(contentView,text, Snackbar.LENGTH_SHORT)
                .setTextColor(getContext().getColor(R.color.white))
                .setBackgroundTint(getContext().getColor(backgroundColor))
                .show();
    }

    @Override
    public void showSnackBar(String text, int backgroundColor, int textColor) {
        Snackbar.make(contentView,text, Snackbar.LENGTH_SHORT)
                .setTextColor(getContext().getColor(textColor))
                .setBackgroundTint(getContext().getColor(backgroundColor))
                .show();
    }
}