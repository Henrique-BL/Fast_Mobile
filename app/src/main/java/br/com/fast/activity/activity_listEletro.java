package br.com.fast.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Comparator;
import java.util.List;

import br.com.fast.DAO.EletrodomesticoDataBase;
import br.com.fast.Utils.UtilsGUI;
import br.com.fast.model.Eletrodomestico;
import br.com.fast.model.EletrodomesticoAdapter;
import br.com.fast.R;
import br.com.fast.model.Local;

public class activity_listEletro extends AppCompatActivity {

    private EletrodomesticoAdapter adapter;
    private int                        posicaoLista = -1;
    private int                        local;
    private ActionMode                 mode;
    private View                       viewSelecionada;
    private ListView                   listViewEletro;

    private Menu                       optionsMenu;
    private static final String ARQUIVO = "br.com.fast.PREFERENCIAORDENACAO";
    private static final String ORDEM = "ORDEM";
    private static final String LOCAL = "LOCAL";
    private              int ordemID = R.id.menuItemAlCres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_eletro);
        setTitle(getString(R.string.lista_title));
        listViewEletro = findViewById(R.id.ListViewEletro);
        listViewEletro.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        local = Integer.valueOf(getIntent().getExtras().getString(LOCAL));
        listViewEletro.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(mode !=null){
                    return false;
                }
                posicaoLista = i;
                viewSelecionada = view;
                view.setBackgroundColor(Color.LTGRAY);
                listViewEletro.setEnabled(false);

                mode = startActionMode(mActionModeCallback);


                return true;
            }
        });

        popularLista();
        lerPreferencias();


    }

    public static void abrirLocal(AppCompatActivity activity, int  localID){
        Intent intent = new Intent(activity, activity_listEletro.class);
        intent.putExtra(LOCAL, String.valueOf(localID));
        activity.startActivity(intent);
    }

    private void popularLista(){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                EletrodomesticoDataBase dataBase = EletrodomesticoDataBase.getDataBase(activity_listEletro.this);

                List<Eletrodomestico> listaEletros = dataBase.eletroDAO().buscarPorLocal(local);

                activity_listEletro.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new EletrodomesticoAdapter(activity_listEletro.this, listaEletros);
                        listViewEletro.setAdapter(adapter);
                    }
                });
            }
        });
    }
    public void adicionar(){
        activity_cadastro_eletro.adicionarEletro(this);

    }
    public void alterar(){
        Eletrodomestico eletrodomestico = (Eletrodomestico)
                                           listViewEletro.getItemAtPosition(posicaoLista);
        activity_cadastro_eletro.alterarEletro(this,eletrodomestico);
    }
    public void excluir(Eletrodomestico eletrodomestico){
        DialogInterface.OnClickListener listener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch(which){
                            case DialogInterface.BUTTON_POSITIVE:

                                AsyncTask.execute(new Runnable() {
                                    @Override
                                    public void run() {

                                        EletrodomesticoDataBase database = EletrodomesticoDataBase.getDataBase
                                                (activity_listEletro.this);
                                        database.eletroDAO().excluir(eletrodomestico);

                                        activity_listEletro.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                popularLista();
                                                adapter.remover(eletrodomestico);
                                            }
                                        });
                                    }
                                });

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:

                                break;
                        }
                    }
                };

        String mensagem =   getString(R.string.excluir )+ eletrodomestico.getNome()+ getString(R.string.permanentemente);
        UtilsGUI.confirmacao(this, mensagem,listener);


    }


    public void sobre(View view){
        Intent intent = new Intent(this, appAutoria.class);
        startActivity(intent);
    }


    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            MenuInflater inflater = actionMode.getMenuInflater();
            inflater.inflate(R.menu.menu_especifico, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.menuItemAlterar:
                    alterar();
                    actionMode.finish();
                    return true;
                case R.id.menuItemExcluir:

                    Eletrodomestico eletrodomestico = (Eletrodomestico)
                            listViewEletro.getItemAtPosition(posicaoLista);
                    excluir(eletrodomestico);
                    actionMode.finish();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            if(viewSelecionada!=null){
                viewSelecionada.setBackgroundColor(Color.TRANSPARENT);
            }
            mode = null;
            viewSelecionada = null;
            listViewEletro.setEnabled(true);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){
            Bundle bundle = data.getExtras();

            if(bundle != null){

                String nome = bundle.getString(activity_cadastro_eletro.NOME, getString(R.string.sem_nome)) ;
                int potencia = Integer.valueOf(bundle.getString(activity_cadastro_eletro.POTENCIA));
                int voltsID = bundle.getInt(activity_cadastro_eletro.VOLTS);
                boolean estado = bundle.getBoolean(activity_cadastro_eletro.ESTADO);
                int tempo = Integer.valueOf(bundle.getString(activity_cadastro_eletro.TEMPO));
                switch (voltsID){
                    case R.id.radioButtonEletro110:
                        voltsID = 110;
                        break;
                    case R.id.radioButtonEletro220:
                        voltsID = 220;
                        break;
                    default:
                        voltsID = 0;
                }

                if(requestCode == activity_cadastro_eletro.NOVO){
                    int finalVoltsID1 = voltsID;
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {

                            EletrodomesticoDataBase dataBase = EletrodomesticoDataBase.getDataBase(activity_listEletro.this);
                            dataBase.eletroDAO().criar(new Eletrodomestico(nome, potencia, finalVoltsID1,estado, tempo, local));
                            Local novo = dataBase.localDAO().buscarID(local);
                            activity_listEletro.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    popularLista();
                                }
                            });
                        }
                    });

                }else{
                    int finalVoltsID = voltsID;
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            EletrodomesticoDataBase dataBase = EletrodomesticoDataBase.getDataBase(activity_listEletro.this);
                            Eletrodomestico eletrodomestico = (Eletrodomestico)
                                    listViewEletro.getItemAtPosition(posicaoLista);
                            eletrodomestico.setNome(nome);
                            eletrodomestico.setPotencia(potencia);
                            eletrodomestico.setVolts(finalVoltsID);
                            eletrodomestico.setEstado(estado);
                            eletrodomestico.setTempo(tempo);

                            dataBase.eletroDAO().atualizar(eletrodomestico);
                            activity_listEletro.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mudarOrdem();
                                    posicaoLista = -1;
                                  
                                }
                            });
                        }
                    });




                }

            }
        }
    }

    public void lerPreferencias(){
        SharedPreferences preferences = getSharedPreferences(ARQUIVO, Context.MODE_PRIVATE);
        ordemID = preferences.getInt(ORDEM,ordemID);
        mudarOrdem();

    }
    public void editarPreferencias(int novaOrdem){
        SharedPreferences preferences = getSharedPreferences(ARQUIVO,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(ORDEM,novaOrdem);
        editor.commit();

        ordemID = novaOrdem;

        mudarOrdem();
    }
    public void mudarOrdem(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    EletrodomesticoDataBase dataBase = EletrodomesticoDataBase.getDataBase(activity_listEletro.this);
                    List<Eletrodomestico> listaEletros = dataBase.eletroDAO().buscarPorLocal(local);
                    listaEletros.sort(new Comparator<Eletrodomestico>() {
                        @Override
                        public int compare(Eletrodomestico t1, Eletrodomestico t2) {
                            switch (ordemID){
                                case R.id.menuItemPotCres:
                                    return Integer.valueOf(t1.getPotencia()).compareTo
                                            (Integer.valueOf(t2.getPotencia()));
                                case R.id.menuItemPotDecres:
                                    return Integer.valueOf(t2.getPotencia()).compareTo
                                            (Integer.valueOf(t1.getPotencia()));
                                case R.id.menuItemAlCres:
                                    return t1.getNome().compareTo(t2.getNome());
                                case R.id.menuItemAlDecres:
                                    return t2.getNome().compareTo(t1.getNome());
                                default:
                                    return 0;

                            }

                        }
                    });
                    activity_listEletro.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.atualizar(listaEletros);

                        }
                    });
                }
            });


        }
    }
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        optionsMenu = menu;
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(@NonNull Menu menu) {
        MenuItem item;
        switch (ordemID){

            case R.id.menuItemAlCres:
                item = menu.findItem(R.id.menuItemAlCres);
                break;
            case R.id.menuItemAlDecres:
                item = menu.findItem(R.id.menuItemAlDecres);
                break;
            case R.id.menuItemPotCres:
                item = menu.findItem(R.id.menuItemPotCres);

                break;
            case R.id.menuItemPotDecres:
                item = menu.findItem(R.id.menuItemPotDecres);
                break;
            default:
                return false;
        }
        item.setChecked(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        item.setChecked(true);
        switch (item.getItemId()){
            case R.id.menuItemAdicionar:
                adicionar();
                return  true;
            case R.id.menuItemSobre:
                Intent intent = new Intent(this, appAutoria.class);
                startActivity(intent);
                return true;
            case R.id.menuItemAlCres:
            case R.id.menuItemAlDecres:
            case R.id.menuItemPotCres:
            case R.id.menuItemPotDecres:
                editarPreferencias(item.getItemId());

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

