package com.code93.linkcoop.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.code93.linkcoop.R;
import com.code93.linkcoop.adapters.MenuElementosAdapter;
import com.code93.linkcoop.models.Cooperativa;
import com.code93.linkcoop.models.DataTransaccion;
import com.code93.linkcoop.models.Transaccion;

import java.util.List;

public class TransaccionActivity extends AppCompatActivity implements MenuElementosAdapter.OnClickElemetos {

    private static final int RTA_ELEMENTO = 101;
    RecyclerView rvElementos;
    TextView tvTransaccion;

    Cooperativa cooperativa;
    Transaccion transaccion;
    List<DataTransaccion> elementos;
    DataTransaccion elemSelect;

    MenuElementosAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaccion);

        tvTransaccion = findViewById(R.id.tvTransaccion);

//        Bundle extras = getIntent().getExtras();
//        if (extras != null) {
//            cooperativa = (Cooperativa)getIntent().getSerializableExtra("Cooperativa");
//            transaccion = (Transaccion)getIntent().getSerializableExtra("Transaccion");
//            if (transaccion != null) {
//                tvTransaccion.setText(transaccion.getNameTrans());
//                elementos = transaccion.getDataTrans();
//            }
//            if (cooperativa != null) {
//                if (cooperativa.getUrlImgCoop() != null ) {
//                    if (!cooperativa.getUrlImgCoop().trim().isEmpty()) {
//                        //Picasso.get().load(cooperativa.getUrlImgCoop()).into(imgToolLogoCoop);
//                    } else {
//                        if (cooperativa.getIdDrawable() != 0) {
//                            imgToolLogoCoop.setImageDrawable(getResources().getDrawable(cooperativa.getIdDrawable()));
//                        }
//                    }
//                } else {
//                    if (cooperativa.getIdDrawable() != 0) {
//                        imgToolLogoCoop.setImageDrawable(getResources().getDrawable(cooperativa.getIdDrawable()));
//                    }
//                }
//            }
//        }

        rvElementos = findViewById(R.id.rvElementos);

        /*adapter = new MenuElementosAdapter(elementos, this);
        adapter.setOnClickElemento(this);
        rvElementos.setLayoutManager(new LinearLayoutManager(this));
        rvElementos.setAdapter(adapter);*/

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RTA_ELEMENTO) {
            int pos = elementos.indexOf(elemSelect);
            String value = data.getStringExtra("value");
            elementos.set(pos, new DataTransaccion(elemSelect, value));
            transaccion.setDataTrans(elementos);
            adapter = new MenuElementosAdapter(elementos, this);
            adapter.setOnClickElemento(this);
            rvElementos.setLayoutManager(new LinearLayoutManager(this));
            rvElementos.setAdapter(adapter);

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onItemClick(RecyclerView.ViewHolder item, int position, int id) {
        Intent intent = new Intent(this, IngresarMontoActivity.class);
        elemSelect = elementos.get(position);
        intent.putExtra("elemento", elemSelect);
        startActivityForResult(intent, RTA_ELEMENTO);
    }
}