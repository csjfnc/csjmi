package com.example.fjesus.whatsclone.helper;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fjesus on 07/04/2017.
 */

public class Permissao {

    public static boolean validarPermissao(int requestCode, Activity activity, String[] permissoes){

        if(Build.VERSION.SDK_INT >= 23){

            List<String> listaPermissao = new ArrayList<>();

            for (String permissao : permissoes){
                Boolean validaPermissao = ContextCompat.checkSelfPermission(activity, permissao) == PackageManager.PERMISSION_GRANTED;
                if(!validaPermissao) listaPermissao.add(permissao);
            }

            if(listaPermissao.isEmpty()) return  true;

            String[] novasPermissoes = new String[listaPermissao.size()];
            listaPermissao.toArray(novasPermissoes);

            ActivityCompat.requestPermissions(activity, novasPermissoes, requestCode);
        }
        return true;
    }
}
