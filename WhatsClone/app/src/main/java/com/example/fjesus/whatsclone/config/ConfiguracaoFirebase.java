package com.example.fjesus.whatsclone.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by fjesus on 10/04/2017.
 */

public final class ConfiguracaoFirebase {

    private static DatabaseReference databaseReference;
    private static FirebaseAuth autenticacao;

    public static DatabaseReference getFirebase(){

        if(databaseReference == null) {
            databaseReference = FirebaseDatabase.getInstance().getReference();
        }
        return databaseReference;
    }

    public static FirebaseAuth getFirebaseAutenticacao(){
        if(autenticacao == null){
            autenticacao = FirebaseAuth.getInstance();
        }
        return autenticacao;
    }
}