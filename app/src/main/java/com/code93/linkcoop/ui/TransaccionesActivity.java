package com.code93.linkcoop.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.code93.linkcoop.R;
import com.code93.linkcoop.adapters.MenuTransAdapter;
import com.code93.linkcoop.models.Cooperativa;
import com.code93.linkcoop.models.DataTransaccion;
import com.code93.linkcoop.models.Transaccion;
import com.code93.linkcoop.models.Transaction;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.Arrays;

public class TransaccionesActivity extends AppCompatActivity implements MenuTransAdapter.OnClickTrans {

    RecyclerView rvTransacciones;
    TextView tvNomCoop;
    ImageView imgCoop;

    ArrayList<Transaccion> transacciones;

    Cooperativa cooperativa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transacciones);

        rvTransacciones = findViewById(R.id.rvTransacciones);
        tvNomCoop = findViewById(R.id.tvNomCoop);
        imgCoop = findViewById(R.id.imgCoop);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            cooperativa = (Cooperativa) getIntent().getParcelableExtra("coop");
            tvNomCoop.setText(cooperativa.get_namec().trim());
        }

        transacciones = new ArrayList<>();
        MenuTransAdapter adapter = new MenuTransAdapter(cooperativa.get_transaction(), this);
        rvTransacciones.setLayoutManager(new GridLayoutManager(this, 2));
        rvTransacciones.setAdapter(adapter);
    }

    @Override
    public void onItemClick(Transaction transaction) {
        Log.d("onItemClick", transaction.get_namet());

        if (transaction.get_cost() != null) {
            MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(this);
            dialogBuilder.setTitle("Costo de la transaccion")
                    .setMessage("Esta transaccion tiene un costo de " + transaction.get_cost());
            dialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent intent = new Intent(TransaccionesActivity.this, TransaccionActivity.class);
                    intent.putExtra("transaction", transaction);
                    startActivity(intent);
                    finish();
                }
            });
            dialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialogBuilder.show();

        }
    }
}