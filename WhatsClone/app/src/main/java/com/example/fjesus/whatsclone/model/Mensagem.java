package com.example.fjesus.whatsclone.model;

/**
 * Created by fjesus on 24/04/2017.
 */

public class Mensagem {

    private String idUsuario;
    private String mensagem;


    public Mensagem(){
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
