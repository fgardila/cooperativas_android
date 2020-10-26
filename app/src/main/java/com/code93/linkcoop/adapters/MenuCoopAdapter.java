package com.code93.linkcoop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.code93.linkcoop.R;
import com.code93.linkcoop.models.Cooperativa;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MenuCoopAdapter extends RecyclerView.Adapter<MenuCoopAdapter.MenuCoopHolder>{

    ArrayList<Cooperativa> cooperativas;
    Context context;

    public MenuCoopAdapter(ArrayList<Cooperativa> cooperativas, Context context) {
        this.cooperativas = cooperativas;
        this.context = context;
    }

    private OnClickCoop listener;

    public interface OnClickCoop {
        void onItemClick(RecyclerView.ViewHolder item, int position, int id);
    }

    public void setOnClickCoop(OnClickCoop listener) {
        this.listener = listener;
    }

    public OnClickCoop getOnClickCoop(){
        return listener;
    }

    @NonNull
    @Override
    public MenuCoopHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coop, parent, false);
        return new MenuCoopHolder(view, cooperativas, this);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuCoopHolder holder, int position) {
        Cooperativa coop = cooperativas.get(position);
        if (coop.getUrlImgCoop() != null ) {
            if (!coop.getUrlImgCoop().trim().isEmpty()) {
                Picasso.get().load(coop.getUrlImgCoop()).into(holder.imgLogoCoop);
                //holder.imgLogoCoop.setImageDrawable(context.getResources().getDrawable(R.drawable.logo_alianza));
            } else {
                if (coop.getIdDrawable() != 0) {
                    holder.imgLogoCoop.setImageDrawable(context.getResources().getDrawable(coop.getIdDrawable()));
                }
            }
        } else {
            if (coop.getIdDrawable() != 0) {
                holder.imgLogoCoop.setImageDrawable(context.getResources().getDrawable(coop.getIdDrawable()));
            }
        }
    }

    @Override
    public int getItemCount() {
        return cooperativas.size();
    }

    public static class MenuCoopHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        List<Cooperativa> coops;
        MenuCoopAdapter adapter;
        public ImageView imgLogoCoop;

        public MenuCoopHolder(@NonNull View itemView, List<Cooperativa> coops, MenuCoopAdapter adapter) {
            super(itemView);
            this.coops = coops;
            this.adapter = adapter;
            imgLogoCoop = itemView.findViewById(R.id.imgLogoCoop);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            final OnClickCoop listener = adapter.getOnClickCoop();
            int id = v.getId();
            if (listener != null) {
                listener.onItemClick(this, getAdapterPosition(), id);
            }
        }
    }
}
