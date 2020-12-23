package com.estudos.lembretes;

import android.content.Intent;
import android.os.Bundle;

import com.estudos.lembretes.adapters.LembretesAdapter;
import com.estudos.lembretes.dao.LembretesDao;
import com.estudos.lembretes.models.Lembretes;
import com.estudos.lembretes.status.StatusLembretes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;
    private LembretesAdapter mAdapter;

    private List<Lembretes> mLembretes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = findViewById(R.id.main_list_view_lembretes);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Lembretes lembrete = (Lembretes) parent.getItemAtPosition(position);
                formulario(lembrete);
            }
        });
        registerForContextMenu(mListView);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                formulario(null);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.main_menu_lixeira){
            lixeira();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info =(AdapterView.AdapterContextMenuInfo) menuInfo;
        Lembretes lembrete = (Lembretes) mListView.getItemAtPosition(info.position);

        // excluir
        MenuItem menuExcluir = menu.add("Excluir");
        menuExcluir.setOnMenuItemClickListener(x -> {
            moverParaLixeira(lembrete);
            return true;
        });

        if (lembrete.getStatus() == StatusLembretes.PENDENTE.getValue()) {
            // finalizar
            MenuItem menuFinalizar = menu.add("Finalizar");
            menuFinalizar.setOnMenuItemClickListener(x -> {
                alterarStatus(lembrete, StatusLembretes.FINALIZADO);
                return true;
            });
        }else {
            // pendência
            MenuItem menuPendencia = menu.add("Marcar como pendência");
            menuPendencia.setOnMenuItemClickListener(x -> {
                alterarStatus(lembrete, StatusLembretes.PENDENTE);
                return true;
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        listar();
    }

    private void listar(){
        new Thread(() -> {
            try(LembretesDao dao = new LembretesDao(MainActivity.this)){
                mLembretes = dao.listar(false);
            }
            runOnUiThread(() -> {
                mAdapter = new LembretesAdapter(this, mLembretes);
                mListView.setAdapter(mAdapter);
            });
        }).start();
    }

    private void formulario(Lembretes lembrete){
        Intent intent = new Intent(this, FormularioActivity.class);
        if (lembrete != null)
            intent.putExtra("lembrete", lembrete);

        startActivity(intent);
    }

    private void lixeira(){
        Intent intent = new Intent(this, LixeiraActivity.class);
        startActivity(intent);
    }

    private void moverParaLixeira(Lembretes lembrete){
        try(LembretesDao dao = new LembretesDao(this)){
            dao.excluirTemporariamente(lembrete);
        }
        mLembretes.remove(lembrete);
        mAdapter.notifyDataSetChanged();
    }

    private void alterarStatus(Lembretes lembrete, StatusLembretes status){
        lembrete.setStatus(status.getValue());
        try(LembretesDao dao = new LembretesDao(this)){
            dao.salvar(lembrete);
        }
        mAdapter.notifyDataSetChanged();
    }
}