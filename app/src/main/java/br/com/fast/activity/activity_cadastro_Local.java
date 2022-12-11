package br.com.fast.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.fast.R;
import br.com.fast.Utils.UtilsData;
import br.com.fast.model.Local;

public class activity_cadastro_Local extends AppCompatActivity {
    private EditText editTextNome;
    private EditText editTextData;
    private Calendar calendarData;
    public static final String ID = "ID";
    public static final String NOME = "NOME";
    public static final String DATA = "DATA";
    public static final String GASTO = "GASTO";
    public static final String MODO = "MODO";
    public static final int    NOVO = 1;
    public static final int    ALTERAR = 2;

    private int                modo =  0 ;
    private Local local;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_local);
        editTextNome = findViewById(R.id.editTextLocalNome);
        editTextData = findViewById(R.id.editTextTextLocalDataVencimento);
        editTextData.setFocusable(false);
        calendarData = Calendar.getInstance();


        //Definindo botão  lateral UP
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle!=null){
            modo = bundle.getInt(MODO, NOVO);
            if(modo==NOVO){
                setTitle(getString(R.string.novo_local));
            }else{
                setTitle(getString(R.string.edicaoEletro));
                String nome = bundle.getString(NOME,"");
                String data = bundle.getString(DATA, "");
                editTextNome.setText(nome);
                editTextData.setText(data);


            }
        }

        //Configuração de data
        DatePickerDialog.OnDateSetListener listenerData = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int ano, int mes, int dia) {
                calendarData.set(ano,mes,dia);
                String data = UtilsData.formatarData(activity_cadastro_Local.this,
                        calendarData.getTime());
                editTextData.setText(data);
            }
        };
        editTextData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog picker = new DatePickerDialog(activity_cadastro_Local.this,
                        listenerData,calendarData.get(Calendar.YEAR),calendarData.get(Calendar.MONTH),
                        calendarData.get(Calendar.DAY_OF_MONTH));
                picker.show();
            }
        });
    }
    public static void adicionarLocal(AppCompatActivity activity){
        Intent intent = new Intent(activity, activity_cadastro_Local.class);
        intent.putExtra(MODO,NOVO);
        activity.startActivityForResult(intent,NOVO);

    }
    public static void alterarLocal(AppCompatActivity activity, Local local){
        Intent intent = new Intent(activity, activity_cadastro_Local.class);
        intent.putExtra(MODO, ALTERAR);

        intent.putExtra(ID, local.getId());
        intent.putExtra(NOME, local.getNome());
        intent.putExtra(DATA, UtilsData.formatarData(activity, local.getData()));
        intent.putExtra(GASTO, local.getValor());

        activity.startActivityForResult(intent, ALTERAR);
    }
    public void salvar(){
        if(editTextNome.getText().toString().trim() != ""){
            Intent intent = new Intent();
            intent.putExtra(NOME, editTextNome.getText().toString());
            intent.putExtra(DATA, editTextData.getText().toString());
            setResult(Activity.RESULT_OK,intent);
            finish();
        }else{
            Toast.makeText(this, R.string.erroCampos, Toast.LENGTH_LONG).show();
        }
    }
    public void limpar(){
        editTextNome.setText("");
        editTextData.setText("");
        editTextNome.requestFocus();
        Toast.makeText(this, R.string.camposLimpos, Toast.LENGTH_LONG).show();

    }
    public void finalizar(){
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    @Override
    public void onBackPressed() {
        finalizar();
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cadastro, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuItemSalvar:
                salvar();
                return true;

            case R.id.menuItemLimpar:
                limpar();
                return  true;

            case android.R.id.home:
                finalizar();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}