package com.code93.linkcoop.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import com.code93.linkcoop.R;
import com.code93.linkcoop.models.Cooperativa;
import com.code93.linkcoop.models.DataTransaccion;
import com.code93.linkcoop.models.Transaccion;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TarjetaActivity extends AppCompatActivity {

    Cooperativa cooperativa;
    Transaccion transaccion;
    List<DataTransaccion> elementos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarjeta);

        TextView tvNomCoop = findViewById(R.id.tvNomCoop);
        ImageView imgToolLogoCoop = findViewById(R.id.imgToolLogoCoop);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            cooperativa = (Cooperativa)getIntent().getSerializableExtra("Cooperativa");
            transaccion = (Transaccion)getIntent().getSerializableExtra("Transaccion");
            if (transaccion != null) {
                tvNomCoop.setText(transaccion.getNameTrans());
                elementos = transaccion.getDataTrans();
            }
            if (cooperativa != null) {
                if (cooperativa.getUrlImgCoop() != null ) {
                    if (!cooperativa.getUrlImgCoop().trim().isEmpty()) {
                        Picasso.get().load(cooperativa.getUrlImgCoop()).into(imgToolLogoCoop);
                    } else {
                        if (cooperativa.getIdDrawable() != 0) {
                            imgToolLogoCoop.setImageDrawable(getResources().getDrawable(cooperativa.getIdDrawable()));
                        }
                    }
                } else {
                    if (cooperativa.getIdDrawable() != 0) {
                        imgToolLogoCoop.setImageDrawable(getResources().getDrawable(cooperativa.getIdDrawable()));
                    }
                }
            }
        }

        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent = new Intent(TarjetaActivity.this, ImpresionActivity.class);
            intent.putExtra("Cooperativa", cooperativa);
            intent.putExtra("Transaccion", transaccion);
            startActivity(intent);
        }, 5000);


    }
}