package com.estudos.lembretes.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Dao extends SQLiteOpenHelper {

    protected static final String dbName = "db_lembretes";
    protected static final int dbVersion = 1;

    public Dao(@Nullable Context context) {
        super(context, dbName, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (String table : getTables())
            db.execSQL(table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private List<String> getTables(){
        List<String> tables = new ArrayList<>();
        tables.add("CREATE TABLE lembretes(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "descricao TEXT," +
                "status INTEGER," +
                "excluido INTEGER" +
                ");");
        return tables;
    }
}
