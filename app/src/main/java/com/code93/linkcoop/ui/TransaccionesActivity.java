package com.code93.linkcoop.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.code93.linkcoop.R;
import com.code93.linkcoop.adapters.MenuTransAdapter;
import com.code93.linkcoop.models.Cooperativa;
import com.code93.linkcoop.models.DataTransaccion;
import com.code93.linkcoop.models.Transaccion;

import java.util.ArrayList;
import java.util.Arrays;

public class TransaccionesActivity extends AppCompatActivity {

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

        transacciones = new ArrayList<>();
        cargarArrayList();
        MenuTransAdapter adapter = new MenuTransAdapter(transacciones, this);
        adapter.setOnClickTrans((item, position, id) -> {
            Transaccion selectTrans = transacciones.get(position);
            if (!selectTrans.getNameTrans().equals("Historial") || !selectTrans.getNameTrans().equals("Cierre")) {
                Intent intent = new Intent(this, TransaccionActivity.class);
                intent.putExtra("Cooperativa", cooperativa);
                intent.putExtra("Transaccion", selectTrans);
                startActivity(intent);
            } else {
                Toast.makeText(this, "No disponible", Toast.LENGTH_SHORT).show();
            }

        });
        rvTransacciones.setLayoutManager(new GridLayoutManager(this, 2));
        rvTransacciones.setAdapter(adapter);
    }

    private void cargarArrayList() {
        Transaccion retiro = new Transaccion();
        retiro.setNameTrans("Retiro");
        retiro.setDataTrans(Arrays.asList(
                new DataTransaccion("Monto", InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL, 9, "Ingresa el monto")
        ));
        transacciones.add(retiro);
        Transaccion depositos = new Transaccion();
        depositos.setNameTrans("Depositos");
        depositos.setDataTrans(Arrays.asList(
                new DataTransaccion("Monto", InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL, 9, "Ingresa el monto"),
                new DataTransaccion("Numero de cuenta", InputType.TYPE_CLASS_NUMBER, 10, "Ingresa el numero de cuenta")
        ));
        transacciones.add(depositos);
        Transaccion transferencias = new Transaccion();
        transferencias.setNameTrans("Transferencias");
        transferencias.setDataTrans(Arrays.asList(
                new DataTransaccion("Monto", InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL, 9, "Ingresa el monto"),
                new DataTransaccion("Numero de cuenta", InputType.TYPE_CLASS_NUMBER, 10, "Ingresa el numero de cuenta")
        ));
        transacciones.add(transferencias);
        Transaccion recargas = new Transaccion();
        recargas.setNameTrans("Recargas");
        recargas.setDataTrans(Arrays.asList(
                new DataTransaccion("Monto", InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL, 9, "Ingresa el monto"),
                new DataTransaccion("Numero de telefono", InputType.TYPE_CLASS_NUMBER, 10, "Ingresa el numero de telefono")
        ));
        transacciones.add(recargas);
        Transaccion pagos = new Transaccion();
        pagos.setNameTrans("Pagos");
        pagos.setDataTrans(Arrays.asList(
                new DataTransaccion("Monto", InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL, 9, "Ingresa el monto")
        ));
        transacciones.add(pagos);
        transacciones.add(new Transaccion("Historial"));
        transacciones.add(new Transaccion("Cierre"));
    }
}