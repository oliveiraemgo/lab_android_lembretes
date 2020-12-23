package com.estudos.lembretes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.estudos.lembretes.dao.LembretesDao;
import com.estudos.lembretes.models.Lembretes;
import com.estudos.lembretes.status.StatusLembretes;

public class FormularioActivity extends AppCompatActivity {

    private Lembretes mLembrete;
    private EditText mEditTextDescricao;
    private EditText mEditTextTempoGasto;
    private EditText mEditTextStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEditTextDescricao = findViewById(R.id.formulario_edit_text_descricao);
        mEditTextStatus = findViewById(R.id.formulario_edit_text_status);
        mEditTextTempoGasto = findViewById(R.id.formulario_edit_text_tempo_gasto);
        ((Button)findViewById(R.id.formulario_button_salvar)).setOnClickListener(x -> {
            salvar();
        });
        Button btnFinalizar = findViewById(R.id.formulario_button_finalizar);
        btnFinalizar.setOnClickListener(x -> {
            finalizar();
        });

        mLembrete = (Lembretes) getIntent().getSerializableExtra("lembrete");

        boolean inclusao = mLembrete == null;

        if (inclusao){
            mLembrete = new Lembretes();
            btnFinalizar.setEnabled(false);
        }

        if (mLembrete.getStatus() == StatusLembretes.FINALIZADO.getValue()){
            btnFinalizar.setEnabled(false);
        }

        mEditTextDescricao.setText(mLembrete.getDescricao());
        mEditTextStatus.setText(StatusLembretes.values()[mLembrete.getStatus()].name());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_formulario, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.menu_formulario_excluir){
            moverParaLixeira();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void salvar(){
        if (!validar())
            return;

        mLembrete.setDescricao(mEditTextDescricao.getText().toString());
        try(LembretesDao dao = new LembretesDao(this)){
            dao.salvar(mLembrete);
        }
        finish();
    }

    private boolean validar(){
        if (mEditTextDescricao.getText().toString().isEmpty()){
            Toast.makeText(this, "Por favor, insira a descrição.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void finalizar(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Deseja finalizar este lembrete?");

        builder.setNegativeButton("Não", (a, b) -> { });
        builder.setPositiveButton("Sim", (a, b) -> { });

        AlertDialog alert = builder.create();
        alert.show();

        alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLembrete.setStatus(StatusLembretes.FINALIZADO.getValue());
                try(LembretesDao dao = new LembretesDao(FormularioActivity.this)) {
                    dao.salvar(mLembrete);
                }
                finish();
            }
        });

        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
    }

    private void moverParaLixeira(){
        if (mLembrete.getId() == 0)
            return;
        try(LembretesDao dao = new LembretesDao(this)){
            dao.excluirTemporariamente(mLembrete);
        }
    }
}