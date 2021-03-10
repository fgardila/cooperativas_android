package com.code93.linkcoop.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.code93.linkcoop.R;
import com.code93.linkcoop.adapters.MenuTransAdapter;
import com.code93.linkcoop.models.Instituciones;
import com.code93.linkcoop.models.Transaction;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class TransaccionesActivity extends AppCompatActivity implements MenuTransAdapter.OnClickTrans {

    RecyclerView rvTransacciones;
    TextView tvNomCoop;
    ImageView imgCoop;

    Instituciones instituciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transacciones);

        rvTransacciones = findViewById(R.id.rvTransacciones);
        tvNomCoop = findViewById(R.id.tvNomCoop);
        imgCoop = findViewById(R.id.imgCoop);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            instituciones = (Instituciones) getIntent().getParcelableExtra("inst");
            tvNomCoop.setText(instituciones.get_namec().trim());
        }

        MenuTransAdapter adapter = new MenuTransAdapter(instituciones.get_transaction(), this);
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
                    intent.putExtra("instituciones", instituciones);
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