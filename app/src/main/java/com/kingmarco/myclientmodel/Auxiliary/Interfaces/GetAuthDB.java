package com.kingmarco.myclientmodel.Auxiliary.Interfaces;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;

public interface GetAuthDB {
    void onAuthStateChange(@NonNull FirebaseAuth firebaseAuth);
}
