package com.example.fjesus.whatsclone.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.fjesus.whatsclone.R;
import com.example.fjesus.whatsclone.adapter.MensagensListAdapter;
import com.example.fjesus.whatsclone.config.ConfiguracaoFirebase;
import com.example.fjesus.whatsclone.helper.Base64Custom;
import com.example.fjesus.whatsclone.helper.Preferencias;
import com.example.fjesus.whatsclone.model.Mensagem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ConversaActivity extends AppCompatActivity {

    private Bundle bundle;
    private EditText edt_mensagem;
    private ImageButton bt_enviar;

    private DatabaseReference firebase;

    private String nomeUsuarioDestinatario;
    private String idUsuarioRementente;
    private String idUsuarioDestinatario;

    private ListView lv_conversas;
    private ArrayList<Mensagem> mensagens;
    private ArrayAdapter adapter;
    private ValueEventListener valueEventListenerMensagem;

    private MensagensListAdapter mensagensListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversa);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            nomeUsuarioDestinatario = bundle.getString("nome");
            idUsuarioDestinatario = Base64Custom.encode(bundle.getString("email"));

        }
        getSupportActionBar().setTitle(nomeUsuarioDestinatario);

        Preferencias preferencias = new Preferencias(ConversaActivity.this);
        idUsuarioRementente = preferencias.getIdentificador();

        edt_mensagem = (EditText) findViewById(R.id.edt_mensagem);
        bt_enviar = (ImageButton) findViewById(R.id.bt_enviar);
        lv_conversas = (ListView) findViewById(R.id.lv_conversas);

        mensagens = new ArrayList<>();
        mensagensListAdapter = new MensagensListAdapter(ConversaActivity.this, mensagens);

        lv_conversas.setAdapter(mensagensListAdapter);

        firebase = ConfiguracaoFirebase.getFirebase().child("mensagens")
                .child(idUsuarioRementente)
                .child(idUsuarioDestinatario);

        valueEventListenerMensagem = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mensagens.clear();
                for(DataSnapshot dados : dataSnapshot.getChildren()){
                    Mensagem mensagem = dados.getValue(Mensagem.class);
                    mensagens.add(mensagem);
                }

                mensagensListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        firebase.addValueEventListener(valueEventListenerMensagem);

        bt_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textoMensagem = edt_mensagem.getText().toString();
                if (textoMensagem.isEmpty()) {
                    Toast.makeText(ConversaActivity.this, "Digite algo", Toast.LENGTH_LONG).show();
                } else {
                    Mensagem mensagem = new Mensagem();
                    mensagem.setIdUsuario(idUsuarioRementente);
                    mensagem.setMensagem(textoMensagem);
                    salvarMensagem(idUsuarioRementente, idUsuarioDestinatario, mensagem);

                    salvarMensagem(idUsuarioDestinatario, idUsuarioRementente, mensagem);
                    edt_mensagem.setText("");
                }
            }
        });

    }

    public boolean salvarMensagem(String idRemetente, String idDestinatario, Mensagem mensagem) {
        try {
            firebase = ConfiguracaoFirebase.getFirebase().child("mensagens");
            firebase.child(idRemetente).child(idDestinatario).push().setValue(mensagem);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListenerMensagem);
    }
}
