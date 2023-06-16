package com.kingmarco.myclientmodel.Auxiliary.Classes.SyncFirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.kingmarco.myclientmodel.Auxiliary.Classes.Holders.ClientHolder;
import com.kingmarco.myclientmodel.Auxiliary.Enums.SnackBarsInfo;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.GetAuthDB;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.GetFireStoreDB;
import com.kingmarco.myclientmodel.POJOs.Clients;

public class SyncAuthDB {
    private static SyncAuthDB sharedInstance;
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private ListenerRegistration listenerRegistration;

    public static SyncAuthDB getInstance(){
        if(sharedInstance == null){
            sharedInstance = new SyncAuthDB();
        }
        return sharedInstance;
    }

    public SyncAuthDB() {
    }


    public void registerClient(String email, String password, GetAuthDB authentication, GetFireStoreDB getFireStoreDB){
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    authentication.onAuthStateChange(firebaseAuth);
                } else{
                    getFireStoreDB.onCompleteFireStoreRequest(SnackBarsInfo.REGISTER_ERROR);
                }
            }
        });
    }

    public void loginClient(String email, String password, GetAuthDB authentication, GetFireStoreDB getFireStoreDB){
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    authentication.onAuthStateChange(firebaseAuth);
                } else{
                    getFireStoreDB.onCompleteFireStoreRequest(SnackBarsInfo.LOGIN_ERROR);
                }
            }
        });
    }

    public boolean isLogin(){
        return firebaseAuth.getCurrentUser() != null;
    }

    public void logOut(){
        if(!isLogin()) {return;}
        firebaseAuth.signOut();
    }

    public void changeEmail(String oldEmail, String oldPassword, String newEmail, GetAuthDB callback, GetFireStoreDB getFireStoreDB){
        firebaseAuth.signInWithEmailAndPassword(oldEmail,oldPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    updateEmail(newEmail, callback, getFireStoreDB);
                } else{
                    getFireStoreDB.onCompleteFireStoreRequest(SnackBarsInfo.LOGIN_ERROR);
                }
            }
        });
    }

    private void updateEmail(String email, GetAuthDB callback,GetFireStoreDB getFireStoreDB){
        if (!isLogin()){return;}
        firebaseAuth.getCurrentUser().updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    callback.onAuthStateChange(firebaseAuth);
                }else{
                    getFireStoreDB.onCompleteFireStoreRequest(SnackBarsInfo.EMAIL_INCORRECT_ERROR);
                }
            }
        });
    }

    public void changePassword(String oldEmail, String oldPassword, String newPassword, GetAuthDB callback, GetFireStoreDB getFireStoreDB){
        firebaseAuth.signInWithEmailAndPassword(oldEmail,oldPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    updatePassword(newPassword,callback);
                } else{
                    getFireStoreDB.onCompleteFireStoreRequest(SnackBarsInfo.LOGIN_ERROR);
                }
            }
        });
    }

    private void updatePassword(String newPassword, GetAuthDB callback){
        if (!isLogin()){return;}
        firebaseAuth.getCurrentUser().updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    callback.onAuthStateChange(firebaseAuth);
                }
            }
        });
    }

    public void addListenerAuth(GetAuthDB onAuthChange){
        firebaseAuth.addAuthStateListener(onAuthChange::onAuthStateChange);
    }

    public void removeListenerAuth(GetAuthDB onAuthChange){
        firebaseAuth.removeAuthStateListener(onAuthChange::onAuthStateChange);
    }

    public void addListenerClient(){
        if (!isLogin()){return;}
        listenerRegistration = newListenerClient();
    }

    public void removeListenerClient(){
        if(listenerRegistration == null){return;}
        ClientHolder.setClientNull();
        listenerRegistration.remove();
    }

    private ListenerRegistration newListenerClient(){
        if (!isLogin()){
            return null;
        }
        String id = firebaseAuth.getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("Clientes").document(id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (!isLogin()){return;}
                if (value == null){
                    SyncAuthDB.getInstance().logOut();
                    return;
                }
                Clients clients = value.toObject(Clients.class);
                ClientHolder.setYouClient(clients);
            }
        });
    }
}
