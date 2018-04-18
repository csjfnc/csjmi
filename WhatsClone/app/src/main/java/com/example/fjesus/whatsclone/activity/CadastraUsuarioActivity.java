package com.example.fjesus.whatsclone.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fjesus.whatsclone.R;
import com.example.fjesus.whatsclone.config.ConfiguracaoFirebase;
import com.example.fjesus.whatsclone.helper.Base64Custom;
import com.example.fjesus.whatsclone.helper.Preferencias;
import com.example.fjesus.whatsclone.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class CadastraUsuarioActivity extends AppCompatActivity {

    private EditText wnome, wemail, wsenha;
    private Button btn_cadastrar;
    private Usuario usuario;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastra_usuario);

       /* getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        */
        wnome = (EditText) findViewById(R.id.wnome);
        wemail = (EditText) findViewById(R.id.wemail);
        wsenha = (EditText) findViewById(R.id.wsenhacad);
        btn_cadastrar = (Button) findViewById(R.id.btn_cadastrar);

        btn_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usuario = new Usuario();
                usuario.setNome(wnome.getText().toString());
                usuario.setEmail(wemail.getText().toString());
                usuario.setSenha(wsenha.getText().toString());
                cadastraUsuario();
            }
        });
    }

    public void cadastraUsuario(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(CadastraUsuarioActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String identificadorUsuario = Base64Custom.encode(usuario.getEmail());
                    FirebaseUser firebaseUser = task.getResult().getUser();
                    usuario.setId(identificadorUsuario);
                    usuario.salvar();
                    Toast.makeText(CadastraUsuarioActivity.this, "Cadastrado com Sucesso", Toast.LENGTH_LONG).show();
                    Preferencias preferencias = new Preferencias(getApplicationContext());
                    String identificador = Base64Custom.encode(usuario.getEmail());
                    preferencias.salvarUsuarioPreferencia(identificador);
                }else{

                    String erroExcessao = "";

                    try{
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        erroExcessao =  "Senha muito curta!! experimente misturar letras e numeros";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erroExcessao = "Email invalido";
                    } catch (FirebaseAuthUserCollisionException e) {
                        erroExcessao = "Email ja está em uso";
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(CadastraUsuarioActivity.this, "Erro: "+ erroExcessao, Toast.LENGTH_LONG).show();

                }

                abrirLoginUsuario();

                //autenticacao.signOut();


            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void abrirLoginUsuario(){
        Intent intent = new Intent(CadastraUsuarioActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}