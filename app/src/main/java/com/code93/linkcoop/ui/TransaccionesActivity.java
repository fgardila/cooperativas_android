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
import com.code93.linkcoop.persistence.cache.DataTrans;
import com.code93.linkcoop.persistence.models.Cooperativa;
import com.code93.linkcoop.persistence.models.Transaction;
import com.code93.linkcoop.view.cliente.SolicitarDatosClienteActivity;
import com.code93.linkcoop.view.cliente.SolicitarDocumentoActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class TransaccionesActivity extends AppCompatActivity implements MenuTransAdapter.OnClickTrans {

    RecyclerView rvTransacciones;
    TextView tvNomCoop;
    ImageView imgCoop;

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

        MenuTransAdapter adapter = new MenuTransAdapter(cooperativa.get_transaction(), this);
        rvTransacciones.setLayoutManager(new GridLayoutManager(this, 2));
        rvTransacciones.setAdapter(adapter);
    }

    @Override
    public void onItemClick(Transaction transaction) {
        Log.d("onItemClick", transaction.get_namet());

        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(this);
        dialogBuilder.setTitle("Costo de la transaccion")
                .setMessage("Esta transaccion tiene un costo de " + transaction.get_cost());
        dialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //Intent intent = new Intent(TransaccionesActivity.this, TransaccionActivity.class);
                Intent intent = new Intent(TransaccionesActivity.this, SolicitarDocumentoActivity.class);
                intent.putExtra("transaction", transaction);
                intent.putExtra("cooperativa", cooperativa);
                DataTrans.INSTANCE.setTransaction(transaction);
                DataTrans.INSTANCE.setCooperativa(cooperativa);
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