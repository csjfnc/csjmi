package com.example.fjesus.whatsclone.model;

/**
 * Created by fjesus on 20/04/2017.
 */

public class Contato {

    private String nome;
    private String email;
    private String identificador;

    public Contato(){

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }
}
