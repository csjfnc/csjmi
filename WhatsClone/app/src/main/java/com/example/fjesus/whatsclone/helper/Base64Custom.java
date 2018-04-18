package com.example.fjesus.whatsclone.helper;

import android.util.Base64;

/**
 * Created by fjesus on 18/04/2017.
 */

public class Base64Custom {

    public static String encode(String texto){

        return Base64.encodeToString(texto.getBytes(), Base64.DEFAULT).replaceAll("(\\n||\\r)", "");
    }

    public static String decode(String texto){
        return new String(Base64.decode(texto.getBytes(), Base64.DEFAULT));
    }
}
