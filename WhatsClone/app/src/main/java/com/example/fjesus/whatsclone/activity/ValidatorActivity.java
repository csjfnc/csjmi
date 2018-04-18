 package com.example.fjesus.whatsclone.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fjesus.whatsclone.R;
import com.example.fjesus.whatsclone.helper.Preferencias;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.util.HashMap;

 public class ValidatorActivity extends AppCompatActivity {

        private EditText edt_codigo;
        private Button btn_validar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validator);

        edt_codigo = (EditText) findViewById(R.id.edt_codigo);
        btn_validar = (Button) findViewById(R.id.btn_validar);

        SimpleMaskFormatter formatterCodigo = new SimpleMaskFormatter("NNNN");
        MaskTextWatcher maskCodigo = new MaskTextWatcher(edt_codigo, formatterCodigo);

        edt_codigo.addTextChangedListener(maskCodigo);

        btn_validar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Preferencias preferencias = new Preferencias(ValidatorActivity.this);

            /*    HashMap<String, String> dadosUsuario = preferencias.getDadosUsuario();
                if(dadosUsuario.get("token").equals(edt_codigo.getText().toString())){
                    Toast.makeText(ValidatorActivity.this, "Token Validado", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(ValidatorActivity.this, "Token Invalido  ", Toast.LENGTH_LONG).show();
                }*/
            }
        });
    }
}
