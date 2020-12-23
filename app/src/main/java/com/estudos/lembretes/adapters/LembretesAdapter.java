package com.estudos.lembretes.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.estudos.lembretes.R;
import com.estudos.lembretes.models.Lembretes;
import com.estudos.lembretes.status.StatusLembretes;

import java.util.List;

public class LembretesAdapter extends BaseAdapter {

    private List<Lembretes> mLembretes;
    private LayoutInflater mInflater;

    public LembretesAdapter(Context context, List<Lembretes> lembretes){
        mLembretes = lembretes;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mLembretes.size();
    }

    @Override
    public Object getItem(int position) {
        return mLembretes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mLembretes.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(R.layout.item_lista_lembretes, null);
        TextView txt = convertView.findViewById(R.id.item_lista_lembretes_descricao);
        txt.setText(mLembretes.get(position).getDescricao());

        if (mLembretes.get(position).getStatus() == StatusLembretes.PENDENTE.getValue())
            convertView.findViewById(R.id.item_lista_lembretes_check).setVisibility(View.INVISIBLE);

        return convertView;
    }
}
