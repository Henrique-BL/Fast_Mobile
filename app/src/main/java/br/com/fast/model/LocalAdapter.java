package br.com.fast.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import br.com.fast.R;
import br.com.fast.Utils.UtilsData;

public class LocalAdapter extends BaseAdapter {

    Context context;
    List<Local> localList;

    public static class LocalHolder{
        public TextView textViewLocalNome;
        public TextView textViewLocalNomeData;
        public TextView textViewLocalValor;
    }

    public LocalAdapter(Context context, List<Local> localList){
        this.context = context;
        this.localList = localList;
    }
    @Override
    public int getCount() {
        return localList.size();
    }

    @Override
    public Object getItem(int i) {
        return localList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LocalHolder holder;
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_local, viewGroup, false);

            holder = new LocalHolder();
            holder.textViewLocalNome = view.findViewById(R.id.textViewLocalNome);
            holder.textViewLocalNomeData = view.findViewById(R.id.textViewLocalData);
            holder.textViewLocalValor  = view.findViewById(R.id.textViewLocalValor);
            view.setTag(holder);

        }else{
            holder = (LocalHolder) view.getTag();
        }

        holder.textViewLocalNome.setText(localList.get(i).getNome());
        holder.textViewLocalNomeData.setText(UtilsData.formatarData(view.getContext(),
                                            localList.get(i).getData()));

        holder.textViewLocalValor.setText("R$ "+new DecimalFormat("#,##0.00").
                                          format(localList.get(i).getValor()));
        return view;
    }

    public void remover(Local local){
        localList.remove(local);
        notifyDataSetChanged();
    }
    public void atualizar(List<Local> lista){
        this.localList = lista;
        notifyDataSetChanged();
    }
}
