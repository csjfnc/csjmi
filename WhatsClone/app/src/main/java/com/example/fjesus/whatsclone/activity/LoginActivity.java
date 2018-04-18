package com.example.fjesus.whatsclone.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fjesus.whatsclone.Manifest;
import com.example.fjesus.whatsclone.R;
import com.example.fjesus.whatsclone.config.ConfiguracaoFirebase;
import com.example.fjesus.whatsclone.helper.Base64Custom;
import com.example.fjesus.whatsclone.helper.Permissao;
import com.example.fjesus.whatsclone.helper.Preferencias;
import com.example.fjesus.whatsclone.model.Usuario;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.security.Permission;
import java.util.HashMap;
import java.util.Random;

public class LoginActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;

    private EditText wsenha, wusuario;
    private Button btn_entrar;
    private TextView cadastre;
    private FirebaseAuth auth;
    private Usuario usuario;

    private String[] permissoesNecessarias = new String[]{
            android.Manifest.permission.SEND_SMS
    };

    public void verificaUsuario(){
        auth = ConfiguracaoFirebase.getFirebaseAutenticacao();
        if(auth.getCurrentUser() != null){
            abrirTelaPrincipal();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        verificaUsuario();

        Permissao.validarPermissao(1, this, permissoesNecessarias);

        wusuario = (EditText) findViewById(R.id.wusuario);
        wsenha = (EditText) findViewById(R.id.wsenhacad);
        cadastre = (TextView) findViewById(R.id.cadastre);


        btn_entrar = (Button) findViewById(R.id.btn_entrar);


        cadastre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, CadastraUsuarioActivity.class);
                startActivity(intent);
            }
        });

        btn_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                usuario = new Usuario();
                usuario.setEmail(wusuario.getText().toString());
                usuario.setSenha(wsenha.getText().toString());

                validaLogin();

               /* String nomeCompleto = wusuario.getText().toString();
                String telefoneCompleto = wcodigopais.getText().toString()
                        + wcodigocidade.getText().toString()
                        + wnumero.getText().toString();

                String telefoneSemFormatado = telefoneCompleto.replace("+", "");
                telefoneSemFormatado = telefoneSemFormatado.replace("-", "");*/

              /* Random randomico = new Random();
                int numeroRandomico = randomico.nextInt(9999 - 1000) + 1000;
                String token = String.valueOf(numeroRandomico);

                Preferencias preferencias = new Preferencias(LoginActivity.this);
                preferencias.salvarUsuarioPreferencia(nomeCompleto, telefoneCompleto, token);

                String msg = "Whatsapp Cófigo de confirmação: "+token;

                boolean enviadoSMS = enviaSMS("+"+telefoneCompleto, msg);

                if(enviadoSMS){
                    Intent intent = new Intent(LoginActivity.this, ValidatorActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this, "Prooblema ao enviar SMS tente novamente", Toast.LENGTH_LONG  ).show();
                }*/
                //HashMap<String, String> usuario = preferencias.getDadosUsuario();
            }
        });
    }

    public void validaLogin(){
        auth = ConfiguracaoFirebase.getFirebaseAutenticacao();
        auth.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Login efetuado com Sucesso!", Toast.LENGTH_LONG).show();

                    Preferencias preferencias = new Preferencias(getApplicationContext());
                    String identificador = Base64Custom.encode(usuario.getEmail());
                    preferencias.salvarUsuarioPreferencia(identificador);

                    abrirTelaPrincipal();
                }else{
                    Toast.makeText(getApplicationContext(), "Erro ao efetuar o login!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void     abrirTelaPrincipal(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public boolean enviaSMS(String telefone, String menssagem){
        try{
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(telefone, null, menssagem, null, null);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int results : grantResults){
                if(results == PackageManager.PERMISSION_DENIED){
                    alertValidationPermition();
                }
        }
    }

    public void alertValidationPermition(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Aviso WhatsApp");
        builder.setMessage("Para utilizar esse app é necssario aceitar as pemissoes");

        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}


