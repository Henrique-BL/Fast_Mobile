package br.com.fast.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import br.com.fast.model.Eletrodomestico;
import br.com.fast.R;

public class activity_cadastro_eletro extends AppCompatActivity {

    public static final String ID = "ID";
    public static final String NOME = "NOME";
    public static final String POTENCIA = "POTENCIA";
    public static final String VOLTS = "VOLTS";
    public static final String ESTADO = "ESTADO";
    public static final String TEMPO = "TEMPO";
    public static final String LOCAL = "LOCAL";

    public static final String MODO = "MODO";
    public static final int    NOVO = 1;
    public static final int    ALTERAR = 2;

    private int                modo =  0 ;
    private Eletrodomestico eletrodomestico;

    private EditText editTextEletroNome, editTextEletroPotencia;
    private CheckBox checkBoxEletroAtivo;
    private RadioGroup radioGroupEletro;
    private Spinner spinnerEletroTempo;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        editTextEletroNome = findViewById(R.id.editTextEletroNome);
        editTextEletroPotencia = findViewById(R.id.editTextTextEletroPotencia);
        checkBoxEletroAtivo = findViewById(R.id.checkBoxEletroAtivo);
        radioGroupEletro = findViewById(R.id.radioGroupEletro);
        spinnerEletroTempo = findViewById(R.id.spinnerEletroTempo);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        popularSpinner();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null){
            modo = bundle.getInt(MODO,NOVO);
            if(modo == NOVO){
                setTitle(getString(R.string.cadastro_title));

            }else{
                setTitle(getString(R.string.edicaoEletro));
                eletrodomestico = new Eletrodomestico(bundle.getString(NOME), bundle.getInt(POTENCIA),
                                                     bundle.getInt(VOLTS),bundle.getBoolean(ESTADO), bundle.getInt(TEMPO),
                                                    bundle.getInt(LOCAL));
                editTextEletroNome.setText(eletrodomestico.getNome());
                editTextEletroPotencia.setText(String.valueOf(eletrodomestico.getPotencia()));
                checkBoxEletroAtivo.setChecked(bundle.getBoolean(ESTADO));

                if(eletrodomestico.getVolts() == 110){
                    radioGroupEletro.check(R.id.radioButtonEletro110);
                }else{
                    radioGroupEletro.check(R.id.radioButtonEletro220);
                }

                List<Integer> horas = Arrays.stream(getResources().getIntArray(R.array.horas)).
                                                    boxed().collect(Collectors.toList());
                spinnerEletroTempo.setSelection(horas.indexOf(eletrodomestico.getTempo()));
            }
        }
    }

    public void popularSpinner(){

        List<Integer> lista = new ArrayList<>();
        for(int hora = 1; hora<=24;hora++) {
            lista.add(hora);
        }
        ArrayAdapter<Integer> adaptador = new ArrayAdapter<>(this,
                                            android.R.layout.simple_list_item_1,lista);
        spinnerEletroTempo.setAdapter(adaptador);
    }

    public static void adicionarEletro(AppCompatActivity activity){
        Intent intent = new Intent(activity, activity_cadastro_eletro.class);
        intent.putExtra(MODO, NOVO);
        activity.startActivityForResult(intent, NOVO);

    }
    public static void alterarEletro(AppCompatActivity activity, Eletrodomestico eletro){
        Intent intent = new Intent(activity, activity_cadastro_eletro.class);
        intent.putExtra(MODO, ALTERAR);

        intent.putExtra(ID, eletro.getId());
        intent.putExtra(NOME, eletro.getNome());
        intent.putExtra(POTENCIA, eletro.getPotencia());
        intent.putExtra(VOLTS, eletro.getVolts());
        intent.putExtra(ESTADO, eletro.getEstado());
        intent.putExtra(TEMPO,eletro.getTempo());
        intent.putExtra(LOCAL, eletro.getLocalID());

        activity.startActivityForResult(intent, ALTERAR);

    }
    public void salvar(){
        if(verificarDados() == 1){

            Intent intent = new Intent();
            intent.putExtra(NOME, editTextEletroNome.getText().toString());
            intent.putExtra(POTENCIA, editTextEletroPotencia.getText().toString());
            int id = radioGroupEletro.getCheckedRadioButtonId();
            if(id != -1){
                intent.putExtra(VOLTS, id);
            }
            intent.putExtra(ESTADO, checkBoxEletroAtivo.isChecked());
            intent.putExtra(TEMPO,spinnerEletroTempo.getSelectedItem().toString());
            setResult(Activity.RESULT_OK,intent);

            finish();



        }else{
            Toast.makeText(this, R.string.erroCampos, Toast.LENGTH_LONG).show();
        }
    }
    public void limpar(){
        editTextEletroNome.setText("");
        editTextEletroPotencia.setText("");
        checkBoxEletroAtivo.setChecked(false);
        radioGroupEletro.clearCheck();
        Toast.makeText(this, R.string.camposLimpos, Toast.LENGTH_LONG).show();

    }
    public int verificarDados(){
        switch(radioGroupEletro.getCheckedRadioButtonId()){
            case R.id.radioButtonEletro110:
                break;
            case R.id.radioButtonEletro220:
                break;
            default:
                radioGroupEletro.requestFocus();
                return 0;
        }

        if(editTextEletroNome.getText().toString().trim().length() == 0){
            editTextEletroNome.requestFocus();
            return 0;
        }

        if(editTextEletroPotencia.getText().toString().trim().length() == 0){
            editTextEletroPotencia.requestFocus();
            return 0;
        }

        if(spinnerEletroTempo.getSelectedItem() == null){
            spinnerEletroTempo.requestFocus();
            return 0;
        }

        return 1;
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuItemSalvar:
                salvar();
                return true;
            case R.id.menuItemLimpar:
                limpar();
                return true;
            case android.R.id.home:
                finalizar();
                return  true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}