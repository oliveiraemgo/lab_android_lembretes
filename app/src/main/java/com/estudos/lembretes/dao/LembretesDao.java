package com.estudos.lembretes.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.estudos.lembretes.models.Lembretes;

import java.util.ArrayList;
import java.util.List;

public class LembretesDao extends Dao {

    public LembretesDao(@Nullable Context context) {
        super(context);
    }

    public int salvar(Lembretes lembrete){
        if (lembrete.getId() == 0)
            return inserir(lembrete);
        else
            return atualizar(lembrete);
    }

    private int inserir(Lembretes lembrete){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = toValues(lembrete);
        return (int)db.insertOrThrow("lembretes", null, values);
    }

    private int atualizar(Lembretes lembrete){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = toValues(lembrete);
        String where = "id = ?";
        String[] parametros = new String[]{ String.valueOf(lembrete.getId()) };
        db.update("lembretes", values, where, parametros);
        return lembrete.getId();
    }

    public Lembretes selecionar(int id){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM lembretes WHERE id = ?;";
        String[] parametros = new String[] { String.valueOf(id) };
        Cursor c = db.rawQuery(query, parametros);
        List<Lembretes> lembretes = fromCursor(c);

        if (lembretes.size() == 0)
            return null;
        else
            return lembretes.get(0);
    }

    public List<Lembretes> listar(boolean excluidos){
        SQLiteDatabase db = getReadableDatabase();
        StringBuilder query = new StringBuilder("SELECT * FROM lembretes ");

        if (excluidos)
            query.append("WHERE excluido = 1;");
        else
            query.append("WHERE excluido = 0;");

        Cursor c = db.rawQuery(query.toString(), null);
        return fromCursor(c);
    }

    public void excluirTemporariamente(Lembretes lembrete){
        lembrete.setExcluido(true);
        salvar(lembrete);
    }

    private ContentValues toValues(Lembretes lembrete){
        ContentValues values = new ContentValues();
        values.put("descricao", lembrete.getDescricao());
        values.put("status", lembrete.getStatus());
        values.put("excluido", lembrete.isExcluido() ? 1 : 0);
        return values;
    }

    private List<Lembretes> fromCursor(Cursor c){
        List<Lembretes> lembretes = new ArrayList<>();
        while (c.moveToNext()){

            Lembretes lembrete = new Lembretes(
                    c.getInt(c.getColumnIndex("id")),
                    c.getString(c.getColumnIndex("descricao")),
                    c.getInt(c.getColumnIndex("status")),
                    c.getInt(c.getColumnIndex("excluido")) == 1
            );

            lembretes.add(lembrete);
        }
        c.close();
        return lembretes;
    }
}
