package com.code93.linkcoop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.code93.linkcoop.R;
import com.code93.linkcoop.models.Transaccion;

import java.util.List;

public class MenuTransAdapter extends RecyclerView.Adapter<MenuTransAdapter.MenuTransHolder>{

    List<Transaccion> transacciones;
    Context context;

    public MenuTransAdapter(List<Transaccion> transacciones, Context context) {
        this.transacciones = transacciones;
        this.context = context;
    }

    private OnClickTrans listener;

    public interface OnClickTrans {
        void onItemClick(RecyclerView.ViewHolder item, int position, int id);
    }

    public void setOnClickTrans(OnClickTrans listener) {
        this.listener = listener;
    }

    public OnClickTrans getOnClickTrans(){
        return listener;
    }

    @NonNull
    @Override
    public MenuTransHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trans, parent, false);
        return new MenuTransHolder(view, transacciones, this);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuTransHolder holder, int position) {
        Transaccion trans = transacciones.get(position);
        holder.tvNameTrans.setText(trans.getNameTrans());
    }

    @Override
    public int getItemCount() {
        return transacciones.size();
    }

    public static class MenuTransHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        List<Transaccion> trans;
        MenuTransAdapter adapter;
        public TextView tvNameTrans;

        public MenuTransHolder(@NonNull View itemView, List<Transaccion> trans, MenuTransAdapter adapter) {
            super(itemView);
            this.trans = trans;
            this.adapter = adapter;
            tvNameTrans = itemView.findViewById(R.id.tvNameTrans);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            final OnClickTrans listener = adapter.getOnClickTrans();
            int id = v.getId();
            if (listener != null) {
                listener.onItemClick(this, getAdapterPosition(), id);
            }
        }
    }
}
