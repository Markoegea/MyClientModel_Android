package com.kingmarco.myclientmodel.Fragments.Login;


import android.os.Bundle;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.google.android.material.snackbar.Snackbar;
import com.kingmarco.myclientmodel.Auxiliary.InAppSnackBars;
import com.kingmarco.myclientmodel.Auxiliary.SetLabelName;
import com.kingmarco.myclientmodel.POJOs.Clients;
import com.kingmarco.myclientmodel.R;

/**The fragment responsible for update the sensitive data of the user*/
public class ChangeLoginDataFragment extends Fragment implements InAppSnackBars{

    private SetLabelName setLabelName;
    private Clients client;
    //Login View
    private LinearLayout emailLayout, passwordLayout;
    private RadioGroup radioLogin;
    private EditText edtEmail, edtReEmail, edtPassword, edtRePassword;
    private Button btnUpload;
    private View contentView;

    public ChangeLoginDataFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLabelName = (SetLabelName) getContext();
        client = getArguments().getParcelable("client");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_change_login_data, container, false);
        setLoginViews(contentView);

        setLabelName.setLabelName("Actualizar email y/o Contaseña");
        // Inflate the layout for this fragment
        return contentView;
    }


    /**Set the views to the variables*/
    private void setLoginViews(View view){
        emailLayout = view.findViewById(R.id.llEmail);
        emailLayout.setVisibility(View.VISIBLE);

        passwordLayout = view.findViewById(R.id.llPassword);

        radioLogin = view.findViewById(R.id.radioLogin);
        radioLogin.setOnCheckedChangeListener(this::OnRadioChange);

        edtEmail = view.findViewById(R.id.edtEmail);
        edtReEmail = view.findViewById(R.id.edtReEmail);
        edtPassword = view.findViewById(R.id.edtPassword);
        edtRePassword = view.findViewById(R.id.edtRePassword);
        btnUpload = view.findViewById(R.id.btnUpdate);
        btnUpload.setOnClickListener(this::onEmailButtonClick);
    }

    /**Change the visibility of the layout depends of the radio status*/
    private void OnRadioChange(RadioGroup radioGroup, int i){
        switch (i){
            case R.id.radioEmail:
                emailLayout.setVisibility(View.VISIBLE);
                passwordLayout.setVisibility(View.INVISIBLE);
                btnUpload.setOnClickListener(this::onEmailButtonClick);
                break;
            case R.id.radioPassword:
                emailLayout.setVisibility(View.INVISIBLE);
                passwordLayout.setVisibility(View.VISIBLE);
                btnUpload.setOnClickListener(this::onPasswordButtonClick);
                break;
            default:
                break;
        }
    }

    /**If everything is correct, update the email of the client*/
    private void onEmailButtonClick(View view){
        if(edtEmail.getText().toString().isEmpty() || edtReEmail.getText().toString().isEmpty()){
            showSnackBar("Hay Algunos Espacios en Blanco");
            return;
        }
        if(!edtEmail.getText().toString().equals(edtReEmail.getText().toString())){
            showSnackBar("Los Correos no Coinciden");
            return;
        }
        client.setEmail(edtEmail.getText().toString());
        showSnackBar("Tu Correo se ha Actualizado Correctamente");
        getActivity().onBackPressed();
    }

    /**If everything is correct, update the password of the client*/
    private void onPasswordButtonClick(View view){
        if(edtPassword.getText().toString().isEmpty() || edtRePassword.getText().toString().isEmpty()){
            showSnackBar("Hay Algunos Espacios en Blanco.");
            return;
        }
        if(!edtPassword.getText().toString().equals(edtRePassword.getText().toString())){
            showSnackBar("Las Contraseñas No Coinciden");
            return;
        }
        client.setPassword(edtPassword.getText().toString());
        showSnackBar("Tu Contraseña se ha Actualizado Correctamente");
        getActivity().onBackPressed();
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