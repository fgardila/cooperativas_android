package com.code93.linkcoop.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.code93.linkcoop.R;
import com.code93.linkcoop.models.Cooperativa;

import java.util.ArrayList;
import java.util.List;

public class MenuCoopAdapter extends RecyclerView.Adapter<MenuCoopAdapter.MenuCoopHolder> {

    List<Cooperativa> cooperativas = new ArrayList<>();
    Context context;

    public MenuCoopAdapter(Context context, OnClickCoop listener) {
        this.context = context;
        setOnClickCoop(listener);
    }

    public void setDatas(List<Cooperativa> cooperativas) {
        this.cooperativas = cooperativas;
        notifyDataSetChanged();
    }

    private OnClickCoop listener;

    public interface OnClickCoop {
        void onItemClick(Cooperativa cooperativa);
    }

    public void setOnClickCoop(OnClickCoop listener) {
        this.listener = listener;
    }

    public OnClickCoop getOnClickCoop() {
        return listener;
    }

    @NonNull
    @Override
    public MenuCoopHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coop, parent, false);
        return new MenuCoopHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuCoopHolder holder, int position) {
        Cooperativa coop = cooperativas.get(position);
        holder.tvNameCoop.setText(coop.get_namec().trim());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(coop);
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.d("SIZE LIST", "" + cooperativas.size());
        return cooperativas.size();
    }

    public static class MenuCoopHolder extends RecyclerView.ViewHolder {

        public ImageView imgLogoCoop;
        public TextView tvNameCoop;

        public MenuCoopHolder(@NonNull View itemView) {
            super(itemView);
            imgLogoCoop = itemView.findViewById(R.id.imgLogoCoop);
            tvNameCoop = itemView.findViewById(R.id.tvNameCoop);
        }
    }
}
