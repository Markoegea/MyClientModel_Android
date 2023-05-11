package com.kingmarco.myclientmodel.Fragments.Login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kingmarco.myclientmodel.Auxiliary.Classes.InAppSnackBars;
import com.kingmarco.myclientmodel.Auxiliary.Classes.SyncAuthDB;
import com.kingmarco.myclientmodel.Auxiliary.Enums.SnackBarsInfo;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.GetAuthDB;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.GetFireStoreDB;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.SetLabelName;
import com.kingmarco.myclientmodel.R;

/**This fragment is the responsible to login the client or send him to the register fragment*/
public class LoginFragment extends Fragment implements GetAuthDB, GetFireStoreDB {
    private SetLabelName setLabelName;
    private Button btnLogin, btnRegister;
    private EditText edtEmail, edtPassword;
    private View contentView;

    public LoginFragment() {
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
        contentView = inflater.inflate(R.layout.fragment_login, container, false);
        setViews(contentView);
        setListeners();
        setLabelName.setLabelName("Iniciar Sesión");
        // Inflate the layout for this fragment
        return contentView;
    }

    private void setViews(View view){
        btnLogin = view.findViewById(R.id.btnLogin);
        btnRegister = view.findViewById(R.id.btnRegister);
        edtEmail = view.findViewById(R.id.edtEmail);
        edtPassword = view.findViewById(R.id.edtPassword);
    }

    private void setListeners(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();
                if (!sanitizeData(email,password)){
                    onCompleteFireStoreRequest(SnackBarsInfo.INCOMPLETE_INFO_ERROR);
                    return;
                }
                SyncAuthDB.getInstance().loginClient(email,password,LoginFragment.this,LoginFragment.this);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = NavHostFragment.findNavController(LoginFragment.this);
                navController.navigate(R.id.action_loginFragment_to_registerClientFragment);
            }
        });
    }

    private boolean sanitizeData(String email, String password){
        if (email.isEmpty()) {
            edtEmail.requestFocus();
            return false;
        } else if(password.isEmpty()){
            edtPassword.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        onAuthStateChange(FirebaseAuth.getInstance());
    }

    @Override
    public void onAuthStateChange(@NonNull FirebaseAuth firebaseAuth) {
        checkDocument(firebaseAuth);
    }

    private void checkDocument(FirebaseAuth firebaseAuth) {
        if(!SyncAuthDB.getInstance().isLogin()){return;}
        String id = firebaseAuth.getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Clientes").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (!SyncAuthDB.getInstance().isLogin()){return;}
                if (task.isSuccessful()){
                    if (task.getResult().exists()){
                        NavController navController = NavHostFragment.findNavController(LoginFragment.this);
                        navController.navigate(R.id.action_loginFragment_to_clientAccountFragment);
                        onCompleteFireStoreRequest(SnackBarsInfo.LOGIN_SUCCESS);
                    } else{
                        SyncAuthDB.getInstance().logOut();
                        onCompleteFireStoreRequest(SnackBarsInfo.LOGIN_ERROR);
                    }
                } else{
                    SyncAuthDB.getInstance().logOut();
                    onCompleteFireStoreRequest(SnackBarsInfo.LOGIN_ERROR);
                }
            }
        });
    }

    @Override
    public void onCompleteFireStoreRequest(SnackBarsInfo snackBarsInfo) {
        InAppSnackBars.defineSnackBarInfo(snackBarsInfo,contentView,getContext(),getActivity(),false);
    }
}