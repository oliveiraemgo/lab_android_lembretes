package com.estudos.lembretes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.estudos.lembretes.adapters.LembretesAdapter;
import com.estudos.lembretes.dao.LembretesDao;
import com.estudos.lembretes.models.Lembretes;

import java.util.ArrayList;
import java.util.List;

public class LixeiraActivity extends AppCompatActivity {

    private List<Lembretes> mLembretes = new ArrayList<>();

    private ListView mListView;
    private LembretesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lixeira);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mListView = findViewById(R.id.lixeira_list_view_lembretes);
        registerForContextMenu(mListView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        listar();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        Lembretes lembrete = (Lembretes) mListView.getItemAtPosition(info.position);

        MenuItem menuRestaurar = menu.add("Restaurar");
        menuRestaurar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                restaurar(lembrete);
                return true;
            }
        });
    }

    private void listar(){
        new Thread(() -> {
            try(LembretesDao dao = new LembretesDao(LixeiraActivity.this)){
                mLembretes = dao.listar(true);
            }
            runOnUiThread(() -> {
                mAdapter = new LembretesAdapter(this, mLembretes);
                mListView.setAdapter(mAdapter);
            });
        }).start();
    }

    private void restaurar(Lembretes lembrete){
        lembrete.setExcluido(false);
        try(LembretesDao dao = new LembretesDao(this)){
            dao.salvar(lembrete);
        }
        mLembretes.remove(lembrete);
        mAdapter.notifyDataSetChanged();
    }
}