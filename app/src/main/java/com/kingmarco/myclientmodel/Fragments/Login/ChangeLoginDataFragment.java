package com.kingmarco.myclientmodel.Fragments.Login;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.kingmarco.myclientmodel.Auxiliary.Classes.InAppSnackBars;
import com.kingmarco.myclientmodel.Auxiliary.Classes.SyncAuthDB;
import com.kingmarco.myclientmodel.Auxiliary.Enums.SnackBarsInfo;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.GetAuthDB;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.GetFireStoreDB;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.SetLabelName;
import com.kingmarco.myclientmodel.R;

/**The fragment responsible for update the sensitive data of the user*/
public class ChangeLoginDataFragment extends Fragment implements GetFireStoreDB, GetAuthDB {

    private SetLabelName setLabelName;
    //Login View
    private LinearLayout emailLayout, passwordLayout;
    private RadioGroup radioLogin;
    private EditText edtOldEmail, edtOldPassword, edtEmail, edtReEmail, edtPassword, edtRePassword;
    private FloatingActionButton btnUpload;
    private View contentView;
    private boolean isEmail;

    public ChangeLoginDataFragment() {
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
        contentView = inflater.inflate(R.layout.fragment_change_login_data, container, false);
        setLoginViews(contentView);
        setLabelName.setLabelName("Actualizar email y/o Contaseña");
        // Inflate the layout for this fragment
        return contentView;
    }


    /**Set the views to the variables*/
    private void setLoginViews(View view){
        edtOldEmail = view.findViewById(R.id.edtOldEmail);
        edtOldPassword = view.findViewById(R.id.edtOldPassword);

        emailLayout = view.findViewById(R.id.llEmail);
        emailLayout.setVisibility(View.VISIBLE);
        isEmail = true;

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
                isEmail = true;
                break;
            case R.id.radioPassword:
                emailLayout.setVisibility(View.INVISIBLE);
                passwordLayout.setVisibility(View.VISIBLE);
                btnUpload.setOnClickListener(this::onPasswordButtonClick);
                isEmail = false;
                break;
            default:
                break;
        }
    }

    /**If everything is correct, update the email of the client*/
    private void onEmailButtonClick(View view){
        if(!checkData()){return;}
        if(edtEmail.getText().toString().isEmpty() || edtReEmail.getText().toString().isEmpty()){
            onCompleteFireStoreRequest(SnackBarsInfo.INCOMPLETE_INFO_ERROR);
            return;
        }
        if(!edtEmail.getText().toString().equals(edtReEmail.getText().toString())){
            onCompleteFireStoreRequest(SnackBarsInfo.EMAILS_EQUALS_ERROR);
            return;
        }
        SyncAuthDB.getInstance().changeEmail(edtOldEmail.getText().toString(),
                edtOldPassword.getText().toString(),
                edtEmail.getText().toString(),
                this,
                this);
    }

    /**If everything is correct, update the password of the client*/
    private void onPasswordButtonClick(View view){
        if(!checkData()){
            return;
        }
        if(edtPassword.getText().toString().isEmpty() || edtRePassword.getText().toString().isEmpty()){
            onCompleteFireStoreRequest(SnackBarsInfo.INCOMPLETE_INFO_ERROR);
            return;
        }
        if(!edtPassword.getText().toString().equals(edtRePassword.getText().toString())){
            onCompleteFireStoreRequest(SnackBarsInfo.PASSWORD_EQUALS_ERROR);
            return;
        }
        if(edtPassword.getText().toString().length()<7){
            onCompleteFireStoreRequest(SnackBarsInfo.PASSWORD_LENGTH_ERROR);
            return;
        }
        SyncAuthDB.getInstance().changePassword(edtOldEmail.getText().toString(),
                edtOldPassword.getText().toString(),
                edtPassword.getText().toString(),
                this,
                this);
    }

    private boolean checkData(){
        if(edtOldEmail.getText().toString().isEmpty()){
            onCompleteFireStoreRequest(SnackBarsInfo.INCOMPLETE_INFO_ERROR);
            return false;
        } else if(edtOldPassword.getText().toString().isEmpty()){
            onCompleteFireStoreRequest(SnackBarsInfo.INCOMPLETE_INFO_ERROR);
            return false;
        }
        return true;
    }

    @Override
    public void onAuthStateChange(@NonNull FirebaseAuth firebaseAuth) {
        if (isEmail){
            onCompleteFireStoreRequest(SnackBarsInfo.EMAIL_SUCCESS);
        }else {
            onCompleteFireStoreRequest(SnackBarsInfo.PASSWORD_SUCCESS);
        }
    }

    @Override
    public void onCompleteFireStoreRequest(SnackBarsInfo snackBarsInfo) {
        InAppSnackBars.defineSnackBarInfo(snackBarsInfo,contentView,getContext(),getActivity(),true);
    }
}