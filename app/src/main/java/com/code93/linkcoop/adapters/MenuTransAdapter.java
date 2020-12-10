package com.code93.linkcoop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.code93.linkcoop.R;
import com.code93.linkcoop.models.Transaction;

import java.util.List;

public class MenuTransAdapter extends RecyclerView.Adapter<MenuTransAdapter.MenuTransHolder>{

    List<Transaction> transacciones;
    Context context;

    public MenuTransAdapter(List<Transaction> transacciones, OnClickTrans onClickTrans) {
        this.transacciones = transacciones;
        setOnClickTrans(onClickTrans);
    }

    private OnClickTrans listener;

    public interface OnClickTrans {
        void onItemClick(Transaction transaction);
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
        return new MenuTransHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuTransHolder holder, int position) {
        Transaction trans = transacciones.get(position);
        holder.tvNameTrans.setText(trans.get_namet().trim());
        holder.itemView.setOnClickListener(v -> listener.onItemClick(trans));
    }

    @Override
    public int getItemCount() {
        return transacciones.size();
    }

    public static class MenuTransHolder extends RecyclerView.ViewHolder {

        public TextView tvNameTrans;

        public MenuTransHolder(@NonNull View itemView) {
            super(itemView);
            tvNameTrans = itemView.findViewById(R.id.tvNameTrans);

        }
    }
}
