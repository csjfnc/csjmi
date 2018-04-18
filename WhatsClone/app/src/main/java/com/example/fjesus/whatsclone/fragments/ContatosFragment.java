package com.example.fjesus.whatsclone.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.fjesus.whatsclone.R;
import com.example.fjesus.whatsclone.activity.ConversaActivity;
import com.example.fjesus.whatsclone.adapter.ContatosListAdapter;
import com.example.fjesus.whatsclone.config.ConfiguracaoFirebase;
import com.example.fjesus.whatsclone.helper.Preferencias;
import com.example.fjesus.whatsclone.model.Contato;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fjesus on 18/04/2017.
 */

public class ContatosFragment extends Fragment{


    private ListView listContatos;
    private ArrayAdapter arrayAdapter;
    private List<Contato> contatos;
    private DatabaseReference reference;
    private ValueEventListener valueEventListener;
    private ContatosListAdapter contatosListAdapter;



    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_contatos, container, false);

        listContatos = (ListView) view.findViewById(R.id.listContatos);

        contatos = new ArrayList<>();
        //arrayAdapter = new ArrayAdapter(getActivity(), R.layout.linha_contatos, contatos);
        contatosListAdapter = new ContatosListAdapter(getActivity(), contatos);

        Preferencias preferencias = new Preferencias(getActivity());
        String id = preferencias.getIdentificador();

        listContatos.setAdapter(contatosListAdapter);
        reference = ConfiguracaoFirebase.getFirebase().child("contatos").child(id);

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                contatos.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    Contato contato = snapshot.getValue(Contato.class);
                    contatos.add(contato);
                }

                contatosListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        listContatos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ConversaActivity.class);

                Contato contato = contatos.get(position);
                intent.putExtra("nome", contato.getNome());
                intent.putExtra("email", contato.getEmail());

                startActivity(intent);

            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        reference.addValueEventListener(valueEventListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        reference.removeEventListener(valueEventListener);
        Log.i("stop", "stop");
    }
}
