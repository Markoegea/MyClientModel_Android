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
import android.widget.ScrollView;
import android.widget.Spinner;

import com.google.android.material.snackbar.Snackbar;
import com.kingmarco.myclientmodel.Auxiliary.InAppSnackBars;
import com.kingmarco.myclientmodel.Auxiliary.SetLabelName;
import com.kingmarco.myclientmodel.POJOs.Clients;
import com.kingmarco.myclientmodel.R;

/**The Fragment responsible to show and update the not sensitive information of the clien*/
public class ChangeInfoAccountFragment extends Fragment implements InAppSnackBars {

    private SetLabelName setLabelName;
    private Clients client;
    //Personal View
    private EditText edtDocumentId, edtName, edtLastName, edtAge, edtPhoneNumber;
    private Spinner documentTypeSpinner;
    private Button btnUpload;
    private View contentView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLabelName = (SetLabelName) getContext();
        client = getArguments().getParcelable("client");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_change_info_account, container, false);

        setPersonalViews(contentView);

        setLabelName.setLabelName("Actualizar");
        // Inflate the layout for this fragment
        return contentView;
    }

    /**Personal Functions set the views*/
    private void setPersonalViews(View view){
        edtDocumentId = view.findViewById(R.id.edtDocumentId);
        edtName = view.findViewById(R.id.edtName);
        edtLastName = view.findViewById(R.id.edtLastName);
        edtAge = view.findViewById(R.id.edtAge);
        edtPhoneNumber = view.findViewById(R.id.edtPhoneNumber);
        documentTypeSpinner = view.findViewById(R.id.documentTypeSpinner);
        btnUpload = view.findViewById(R.id.btnUpdate);

        btnUpload.setOnClickListener(this::onUpdateClick);

        setEdtData();
    }

    /**Set the information in the edit Text*/
    private void setEdtData(){
        if(client == null){return;}
        edtDocumentId.setText(client.getDocumentID());
        edtName.setText(client.getName());
        edtLastName.setText(client.getLastName());
        edtAge.setText(String.valueOf(client.getAge()));
        edtPhoneNumber.setText(client.getPhoneNumber());
        for (int i =0; i < documentTypeSpinner.getCount();i++){
            if (documentTypeSpinner.getItemAtPosition(i).toString().equals(client.getDocumentType())){
                documentTypeSpinner.setSelection(i);
                break;
            }
        }
    }

    /**If there is no empty Edit Text update the information*/
    private void onUpdateClick(View view){
        if(edtDocumentId.getText().toString().isEmpty() ||
                edtName.getText().toString().isEmpty() ||
                edtLastName.getText().toString().isEmpty() ||
                edtAge.getText().toString().isEmpty()||
                edtPhoneNumber.getText().toString().isEmpty()){
            showSnackBar("Hay Campos en Blanco");
            return;
        }
        client.setDocumentID(edtDocumentId.getText().toString());
        client.setDocumentType(documentTypeSpinner.getSelectedItem().toString());
        client.setName(edtName.getText().toString());
        client.setLastName(edtLastName.getText().toString());
        client.setAge(Integer.parseInt(edtAge.getText().toString()));
        client.setPhoneNumber(edtPhoneNumber.getText().toString());

        showSnackBar("Tu informacion se ha actulizado correctamente");
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