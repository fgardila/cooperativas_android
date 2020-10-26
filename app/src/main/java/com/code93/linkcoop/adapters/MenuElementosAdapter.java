package com.code93.linkcoop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.code93.linkcoop.R;
import com.code93.linkcoop.models.DataTransaccion;

import java.util.List;

public class MenuElementosAdapter extends RecyclerView.Adapter<MenuElementosAdapter.MenuElementosHolder>{

    List<DataTransaccion> elementos;
    Context context;

    public MenuElementosAdapter(List<DataTransaccion> elementos, Context context) {
        this.elementos = elementos;
        this.context = context;
    }

    private OnClickElemetos listener;

    public interface OnClickElemetos {
        void onItemClick(RecyclerView.ViewHolder item, int position, int id);
    }

    public void setOnClickElemento(OnClickElemetos listener) {
        this.listener = listener;
    }

    public OnClickElemetos getOnClickElemento(){
        return listener;
    }

    @NonNull
    @Override
    public MenuElementosHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_elemento, parent, false);
        return new MenuElementosHolder(view, elementos, this);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuElementosHolder holder, int position) {
        DataTransaccion eleme = elementos.get(position);
        holder.nameElemento.setText(eleme.getName());
        if (eleme.getValue() != null) {
            holder.tvValue.setText(eleme.getValue());
        }
    }

    @Override
    public int getItemCount() {
        return elementos.size();
    }

    public static class MenuElementosHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        List<DataTransaccion> ele;
        MenuElementosAdapter adapter;
        TextView nameElemento;
        TextView tvValue;
        LinearLayout linear;
        LinearLayout lyNombre;

        public MenuElementosHolder(@NonNull View itemView, List<DataTransaccion> ele, MenuElementosAdapter adapter) {
            super(itemView);
            this.ele = ele;
            this.adapter = adapter;
            tvValue = itemView.findViewById(R.id.tvValue);
            nameElemento = itemView.findViewById(R.id.nameElemento);
            linear = itemView.findViewById(R.id.linear);
            lyNombre = itemView.findViewById(R.id.lyNombre);
            linear.setOnClickListener(this);
            lyNombre.setOnClickListener(this);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            final OnClickElemetos listener = adapter.getOnClickElemento();
            int id = v.getId();
            if (listener != null) {
                listener.onItemClick(this, getAdapterPosition(), id);
            }
        }
    }
}
