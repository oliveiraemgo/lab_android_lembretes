package com.estudos.lembretes.models;

import java.io.Serializable;
import java.util.Objects;

public class Lembretes implements Serializable {

    private int id;
    private String descricao;
    private int status;
    private boolean excluido;

    public Lembretes() { }

    public Lembretes(int id, String descricao, int status, boolean excluido){
        this.id = id;
        this.descricao = descricao;
        this.status = status;
        this.excluido = excluido;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isExcluido() {
        return excluido;
    }

    public void setExcluido(boolean excluido) {
        this.excluido = excluido;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lembretes lembretes = (Lembretes) o;
        return id == lembretes.id &&
                status == lembretes.status &&
                excluido == lembretes.excluido &&
                Objects.equals(descricao, lembretes.descricao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, descricao, status, excluido);
    }
}
