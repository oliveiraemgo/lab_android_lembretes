package com.estudos.lembretes.status;

public enum StatusLembretes {

    PENDENTE(0),
    FINALIZADO(1);

    private int value;

    StatusLembretes(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
