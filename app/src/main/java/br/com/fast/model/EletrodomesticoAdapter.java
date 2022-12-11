package br.com.fast.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.fast.R;
import br.com.fast.model.Eletrodomestico;

public class EletrodomesticoAdapter extends BaseAdapter {
    Context context;
    List<Eletrodomestico> eletros;

    private static class EletrodomesticoHolder{
        public TextView textViewNome;
        public TextView textViewPotencia;
        public TextView textViewVolts;
        public TextView textViewEstado;
        public TextView textViewTempo;

    }

    public EletrodomesticoAdapter(Context context, List<Eletrodomestico> eletros){
        this.context = context;
        this.eletros = eletros;
    }

    @Override
    public int getCount() {
        return eletros.size();
    }

    @Override
    public Object getItem(int i) {
        return eletros.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        EletrodomesticoHolder holder;

        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_eletro, viewGroup, false);
            holder = new EletrodomesticoHolder();

            holder.textViewNome = view.findViewById(R.id.textViewNome);
            holder.textViewEstado = view.findViewById(R.id.textViewEstado);
            holder.textViewPotencia = view.findViewById(R.id.textViewPotencia);
            holder.textViewVolts = view.findViewById(R.id.textViewVolts);
            holder.textViewTempo = view.findViewById(R.id.textViewTempo);

            view.setTag(holder);
        }else{
            holder = (EletrodomesticoHolder) view.getTag();
        }

        holder.textViewNome.setText(eletros.get(i).getNome());
        holder.textViewEstado.setText(eletros.get(i).getEstado()?
                        context.getString(R.string.ativado)     :
                        context.getString(R.string.desativado));
        holder.textViewPotencia.setText(Integer.toString(eletros.get(i).getPotencia()));
        holder.textViewVolts.setText(Integer.toString(eletros.get(i).getVolts()));
        holder.textViewTempo.setText(Integer.toString(eletros.get(i).getTempo()));


        return view;
    }

    public void remover(Eletrodomestico eletrodomestico){
        eletros.remove(eletrodomestico);
        notifyDataSetChanged();
    }
    public void atualizar(List<Eletrodomestico> lista){
        this.eletros = lista;
        notifyDataSetChanged();
    }
}
