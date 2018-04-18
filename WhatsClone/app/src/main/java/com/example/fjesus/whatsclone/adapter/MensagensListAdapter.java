package com.example.fjesus.whatsclone.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.fjesus.whatsclone.R;
import com.example.fjesus.whatsclone.helper.Preferencias;
import com.example.fjesus.whatsclone.model.Mensagem;

import java.util.List;

/**
 * Created by fjesus on 25/04/2017.
 */

public class MensagensListAdapter extends ArrayAdapter<Mensagem> {

    private List<Mensagem> mensagems;
    private Context context;

    public MensagensListAdapter(@NonNull Context context,  @NonNull List<Mensagem> mensagems) {
        super(context, 0, mensagems);
        this.context = context;
        this.mensagems = mensagems;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Preferencias preferencias = new Preferencias(context);
        String idUsuarioEnviou = preferencias.getIdentificador();

        TextView msg_enviada;

        View view = null;
        if(mensagems != null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if(idUsuarioEnviou.equals(mensagems.get(position).getIdUsuario())){
                view = layoutInflater.inflate(R.layout.linha_mensagens_recebidas, parent, false);
            }else{
                view = layoutInflater.inflate(R.layout.linha_mensagens_enviadas, parent, false);
            }

            msg_enviada = (TextView) view.findViewById(R.id.msg_enviada);
            msg_enviada.setText(mensagems.get(position).getMensagem());
        }

        return view;
    }
}
