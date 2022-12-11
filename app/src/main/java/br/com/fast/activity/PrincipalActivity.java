package br.com.fast.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Date;
import java.util.List;

import br.com.fast.DAO.EletrodomesticoDataBase;
import br.com.fast.R;
import br.com.fast.Utils.UtilsCalculo;
import br.com.fast.Utils.UtilsData;
import br.com.fast.Utils.UtilsGUI;
import br.com.fast.model.Eletrodomestico;
import br.com.fast.model.Local;
import br.com.fast.model.LocalAdapter;

public class PrincipalActivity extends AppCompatActivity {
    private ListView listViewLocal;
    private LocalAdapter adapter;
    private List<Local> lista;
    private int        posicaoLista = -1;
    private ActionMode mode;
    private View       viewSelecionada;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listViewLocal = findViewById(R.id.ListViewLocal);
        listViewLocal.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        ViewGroup header = (ViewGroup) getLayoutInflater().inflate(R.layout.list_local_header, listViewLocal, false);
        listViewLocal.addHeaderView(header, null, false);


        listViewLocal.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(mode!=null){
                    return false;
                }
                viewSelecionada = view;
                posicaoLista = i;
                view.setBackgroundColor(Color.LTGRAY);
                listViewLocal.setEnabled(false);
                mode = startActionMode(mActionModeCallback);
                return true;
            }
        });

        listViewLocal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Local local = (Local) listViewLocal.getItemAtPosition(i);
                posicaoLista = i;
                abrirLocal(local);


            }
        });
        popularLista();
    }
    public void popularLista(){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                EletrodomesticoDataBase database = EletrodomesticoDataBase.getDataBase(PrincipalActivity.this);

                lista = database.localDAO().buscarTodos();
                //Definindo o gasto
                for(Local local: lista){
                    List<Eletrodomestico> eletros = database.eletroDAO().buscarAtivados(local.getId());
                    local.setValor(UtilsCalculo.calcularGasto(eletros));
                    database.localDAO().atualizar(local);

                }
                PrincipalActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new LocalAdapter(PrincipalActivity.this, lista);
                        listViewLocal.setAdapter(adapter);
                    }
                });
            }
        });

    }


    public void abrirLocal(Local local){
        activity_listEletro.abrirLocal(PrincipalActivity.this, local.getId());
        adapter.notifyDataSetChanged();

    }
    public void adicionar(){

        activity_cadastro_Local.adicionarLocal(this);
    }
    public void alterar(){
        Local local = (Local) listViewLocal.getItemAtPosition(posicaoLista);
        activity_cadastro_Local.alterarLocal(this, local);
    }
    public void excluir(Local local){

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
                                                (PrincipalActivity.this);
                                        database.localDAO().excluir(local);

                                        PrincipalActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                popularLista();
                                                adapter.remover(local);
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

        String mensagem =   getString(R.string.excluir )+ local.getNome()+ getString(R.string.permanentemente);
        UtilsGUI.confirmacao(this, mensagem,listener);




    }
    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            MenuInflater inflater = actionMode.getMenuInflater();
            inflater.inflate(R.menu.menu_especifico,menu);
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
                    Local local = (Local)
                            listViewLocal.getItemAtPosition(posicaoLista);
                    excluir(local);
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
            listViewLocal.setEnabled(true);
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            Bundle bundle = data.getExtras();
            if(bundle != null){
                String nome = bundle.getString(activity_cadastro_Local.NOME, "");
                Date date = UtilsData.formatarString(this,
                            bundle.getString(activity_cadastro_Local.DATA, ""));
                int gasto = Integer.valueOf(bundle.getString(activity_cadastro_Local.GASTO, "0"));
                if(requestCode == activity_cadastro_Local.NOVO){
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            EletrodomesticoDataBase database = EletrodomesticoDataBase.getDataBase
                                                                (PrincipalActivity.this);
                            Local local = new Local(nome,date, gasto);

                            database.localDAO().criar(local);

                            PrincipalActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    popularLista();
                                }
                            });
                        }
                    });

                }else{
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            EletrodomesticoDataBase database = EletrodomesticoDataBase.getDataBase
                                    (PrincipalActivity.this);
                            Local local = (Local) listViewLocal.getItemAtPosition(posicaoLista);

                            local.setNome(nome);
                            local.setData(date);
                            local.setValor(gasto);
                            database.localDAO().atualizar(local);

                            PrincipalActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    popularLista();
                                    posicaoLista = -1;
                                }
                            });
                        }
                    });



                }

                adapter.notifyDataSetChanged();


            }
        }
    }


    public void sobre(){
        Intent intent = new Intent(this, appAutoria.class);
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_local_principal,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuItemCriar:
                adicionar();
                return true;
            case R.id.menuItemSobre:
                sobre();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
            }
    @Override
    public boolean onPrepareOptionsMenu(@NonNull Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }



}