package com.example.fjesus.whatsclone.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fjesus.whatsclone.R;
import com.example.fjesus.whatsclone.adapter.TabAdapter;
import com.example.fjesus.whatsclone.config.ConfiguracaoFirebase;
import com.example.fjesus.whatsclone.helper.Base64Custom;
import com.example.fjesus.whatsclone.helper.Preferencias;
import com.example.fjesus.whatsclone.helper.SlidingTabLayout;
import com.example.fjesus.whatsclone.model.Contato;
import com.example.fjesus.whatsclone.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private Toolbar toolbar;
    private SlidingTabLayout  stl_tabs;
    private ViewPager vp_pagina;
    private String identificador;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Ola");

        stl_tabs = (SlidingTabLayout) findViewById(R.id.stl_tabs);
        vp_pagina = (ViewPager) findViewById(R.id.vp_pagina);

        stl_tabs.setDistributeEvenly(true);

        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());
        vp_pagina.setAdapter(tabAdapter);
        stl_tabs.setViewPager(vp_pagina);


        stl_tabs.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.colorAccent));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.item_sair:{
                deslogar();
            }
            case R.id.item_add:{
                abrirDialogCadastroContato();
            }
        }

        return super.onOptionsItemSelected(item);
    }
    public void deslogar(){
        auth = ConfiguracaoFirebase.getFirebaseAutenticacao();
        auth.signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void abrirDialogCadastroContato(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Novo Contato");
        builder.setMessage("Email do Usuario");

        final EditText editText = new EditText(MainActivity.this);
        builder.setView(editText);

        builder.setPositiveButton("Cadastrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String email = editText.getText().toString();
                if(email.isEmpty()){
                    Toast.makeText(MainActivity.this, "Insira um email", Toast.LENGTH_LONG).show();
                }else{
                    identificador = Base64Custom.encode(email);
                    databaseReference = ConfiguracaoFirebase.getFirebase().child("usuarios").child(identificador);
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getValue() != null){

                                Usuario usuario = dataSnapshot.getValue(Usuario.class);

                                Preferencias preferencias = new Preferencias(MainActivity.this);
                                String identificadorUsuarioLogado = preferencias.getIdentificador();

                                databaseReference = ConfiguracaoFirebase.getFirebase();
                                databaseReference = databaseReference.child("contatos")
                                        .child(identificadorUsuarioLogado)
                                        .child(identificador);

                                Contato contato = new Contato();
                                contato.setIdentificador(identificador);
                                contato.setEmail(usuario.getEmail());
                                contato.setNome(usuario.getNome());

                                databaseReference.setValue(contato);

                            }else{
                                Toast.makeText(MainActivity.this, "Usuario Nao eonctrado", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }

            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });


        builder.show();

    }

}