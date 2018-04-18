package com.example.fjesus.whatsclone.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.fjesus.whatsclone.R;
import com.example.fjesus.whatsclone.model.Contato;
import java.util.List;

/**
 * Created by fjesus on 24/04/2017.
 */

public class ContatosListAdapter extends BaseAdapter {

    private List<Contato> contatos;
    private Context context;

    public ContatosListAdapter(Context context, List<Contato> contatos){
        this.context = context;
        this.contatos = contatos;
    }

    @Override
    public int getCount() {
        return contatos.size();
    }

    @Override
    public Object getItem(int position) {
        return contatos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;
        if(contatos != null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.linha_contatos, parent, false);

            TextView linha_nome = (TextView) view.findViewById(R.id.linha_nome);
            TextView linha_email = (TextView) view.findViewById(R.id.linha_email);

            linha_nome.setText(contatos.get(position).getNome());
            linha_email.setText(contatos.get(position).getEmail());

        }

        return view;
    }
}
