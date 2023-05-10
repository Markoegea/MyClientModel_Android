package com.kingmarco.myclientmodel.Fragments.Login;

import android.os.Bundle;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.kingmarco.myclientmodel.Auxiliary.InAppSnackBars;
import com.kingmarco.myclientmodel.Auxiliary.SetLabelName;
import com.kingmarco.myclientmodel.POJOs.Clients;
import com.kingmarco.myclientmodel.R;

/**This fragment is the responsible to login the client or send him to the register fragment*/
public class LoginFragment extends Fragment implements InAppSnackBars {
    //TODO: IF THE USER LOGIN IN THE APP, CHANGE THE BOTTOM NAVIGATION BAR TO NAVIGATE TO THE CLIENT ACCOUNT FRAGMENT AND NOT HERE
    private SetLabelName setLabelName;
    private NavController nav;
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
        nav = NavHostFragment.findNavController(this);

        btnLogin = contentView.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this::onClickBtnLogin);

        btnRegister = contentView.findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this::onClickBtnRegister);

        edtEmail = contentView.findViewById(R.id.edtEmail);
        edtPassword = contentView.findViewById(R.id.edtPassword);

        setLabelName.setLabelName("Iniciar Sesión");
        // Inflate the layout for this fragment
        return contentView;
    }

    /**If all is correct, search for the email in the database, if the password is correct, grant the access*/
    private void onClickBtnLogin(View view){
        //Navigate to the login Fragment with the user info
        if(sanitizeData()){
            Clients clients = Clients.clients.get(0);
            if(clients.getEmail().equals(edtEmail.getText().toString()) && clients.getPassword().equals(edtPassword.getText().toString())){
                Bundle data = new Bundle();
                data.putParcelable("client",clients);
                NavController nav = NavHostFragment.findNavController(this);
                nav.navigate(R.id.action_loginFragment_to_clientAccountFragment,data);
            }
        }
    }

    private boolean sanitizeData(){
        if (edtEmail.getText().toString().isEmpty()) {
            showSnackBar("Falta El Correo.");
            edtEmail.requestFocus();
            return false;
        } else if(edtPassword.getText().toString().isEmpty()){
            showSnackBar("Falta La Contraseña.");
            edtPassword.requestFocus();
            return false;
        }
        return true;
    }

    /**Navigate to the register fragment*/
    private void onClickBtnRegister(View view){
        //Navigate to the register fragment, with no info
        nav.navigate(R.id.action_loginFragment_to_registerClientFragment);
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