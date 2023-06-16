package com.kingmarco.myclientmodel.Fragments.Login;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kingmarco.myclientmodel.Auxiliary.Classes.Holders.ClientHolder;
import com.kingmarco.myclientmodel.Auxiliary.Classes.Static.InAppSnackBars;
import com.kingmarco.myclientmodel.Auxiliary.Classes.SyncFirebase.SyncRealtimeDB;
import com.kingmarco.myclientmodel.Auxiliary.Enums.SnackBarsInfo;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.GetFireStoreDB;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.ProgressBarBehavior;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.SetLabelName;
import com.kingmarco.myclientmodel.POJOs.Clients;
import com.kingmarco.myclientmodel.R;

/**The Fragment responsible to show and update the not sensitive information of the clien*/
public class ChangeInfoAccountFragment extends Fragment implements GetFireStoreDB {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference clientDB = db.collection("Clientes");
    private SetLabelName setLabelName;
    private ProgressBarBehavior progressBarBehavior;
    private ScrollView svParent;
    private Clients client;
    private EditText edtDocumentId, edtName, edtLastName, edtAge, edtPhoneNumber;
    private Spinner documentTypeSpinner;
    private FloatingActionButton btnUpload;
    private View contentView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLabelName = (SetLabelName) getContext();
        progressBarBehavior = (ProgressBarBehavior) getContext();
        client = ClientHolder.getYouClient();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_change_info_account, container, false);
        setPersonalViews(contentView);
        setEdtData();
        setListeners();
        setLabelName.setLabelName("Actualizar");
        // Inflate the layout for this fragment
        return contentView;
    }

    /**Personal Functions set the views*/
    private void setPersonalViews(View view){
        svParent = view.findViewById(R.id.svParent);

        edtDocumentId = view.findViewById(R.id.edtDocumentId);
        scrollView(edtDocumentId);
        edtName = view.findViewById(R.id.edtName);
        scrollView(edtName);
        edtLastName = view.findViewById(R.id.edtLastName);
        scrollView(edtLastName);
        edtAge = view.findViewById(R.id.edtAge);
        scrollView(edtAge);
        edtPhoneNumber = view.findViewById(R.id.edtPhoneNumber);
        scrollView(edtPhoneNumber);
        documentTypeSpinner = view.findViewById(R.id.documentTypeSpinner);
        btnUpload = view.findViewById(R.id.btnUpdate);
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

    private void scrollView(EditText editText){
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus){
                    svParent.post(new Runnable() {
                        @Override
                        public void run() {
                            int scrollY = svParent.getScrollY() + view.getHeight();
                            svParent.scrollTo(0,scrollY);
                        }
                    });
                }
            }
        });
    }

    private void setListeners(){
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarBehavior.startProgressBar();
                if(!checkData()){
                    onCompleteFireStoreRequest(SnackBarsInfo.INCOMPLETE_INFO_ERROR);
                    return;
                }
                updateInformation();
            }
        });
    }

    private boolean checkData(){
        if(edtDocumentId.getText().toString().isEmpty()){
            return false;
        } else if (edtName.getText().toString().isEmpty()){
            return false;
        }else if (edtLastName.getText().toString().isEmpty()){
            return false;
        }else if (edtAge.getText().toString().isEmpty()){
            return false;
        }else if (edtPhoneNumber.getText().toString().isEmpty()){
            return false;
        }
        return true;
    }


    /**If there is no empty Edit Text update the information*/
    private void updateInformation(){
        client.setDocumentID(edtDocumentId.getText().toString());
        client.setDocumentType(documentTypeSpinner.getSelectedItem().toString());
        client.setName(edtName.getText().toString());
        client.setLastName(edtLastName.getText().toString());
        client.setAge(Integer.parseInt(edtAge.getText().toString()));
        client.setPhoneNumber(edtPhoneNumber.getText().toString());
        DocumentReference documentReference = clientDB.document(client.getId());
        documentReference.set(client).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("chats/"+client.getMessagingId());
                    SyncRealtimeDB.uploadChat(databaseReference,client.getName(),"name");
                    onCompleteFireStoreRequest(SnackBarsInfo.UPDATE_SUCCESS);
                } else{
                    onCompleteFireStoreRequest(SnackBarsInfo.DATA_ERROR);
                }
            }
        });
    }

    @Override
    public void onCompleteFireStoreRequest(SnackBarsInfo snackBarsInfo) {
        InAppSnackBars.defineSnackBarInfo(snackBarsInfo,contentView,getContext(),getActivity(),true);
        progressBarBehavior.stopProgressBar();
    }
}