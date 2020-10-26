package com.code93.linkcoop.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.code93.linkcoop.R;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.material.textfield.TextInputEditText;

public class IngresarMontoActivity extends AppCompatActivity {

    TextInputEditText etMonto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresar_monto);

        etMonto = findViewById(R.id.etMonto);

    }

    public void enviarMonto(View view) {
        if (view != null) {
            Intent data = new Intent();
            data.putExtra("value", etMonto.getText().toString());
            setResult(CommonStatusCodes.SUCCESS, data);
            finish();
        }
    }
}