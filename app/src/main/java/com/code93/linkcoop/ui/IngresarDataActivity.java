package com.code93.linkcoop.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.code93.linkcoop.DialogCallback;
import com.code93.linkcoop.R;
import com.code93.linkcoop.StringTools;
import com.code93.linkcoop.Tools;
import com.code93.linkcoop.persistence.models.DataTransaccion;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class IngresarDataActivity extends AppCompatActivity {

    TextInputEditText etData;
    TextInputLayout tilData;
    TextView tvNomTrans;
    DataTransaccion transaccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresar_data);

        etData = findViewById(R.id.etData);
        tilData = findViewById(R.id.tilData);
        tvNomTrans = findViewById(R.id.tvNomTrans);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String nameTrx = getIntent().getStringExtra("nameTrx");
            if (nameTrx != null) {
                tvNomTrans.setText(nameTrx);
            }
            transaccion = (DataTransaccion) getIntent().getSerializableExtra("elemento");
            tilData.setHint(transaccion.getSubTitulo());
            tilData.setStartIconDrawable(transaccion.getDrawable());
            etData.setInputType(transaccion.getInputType());
            etData.setFilters(new InputFilter[]{new
                    InputFilter.LengthFilter(transaccion.getMaxLength())});
            if (transaccion.getValue() != null) {
                etData.setText(transaccion.getValue());
            }
            if (transaccion.getName().trim().toLowerCase().equals("monto")) {
                etData.addTextChangedListener(new TextWatcher() {
                    private boolean isChanged = false;

                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (isChanged) {
                            return;
                        }
                        String str = editable.toString();
                        isChanged = true;
                        String cuttedStr = str;
                        for (int i = str.length() - 1; i >= 0; i--) {
                            char c = str.charAt(i);
                            if ('.' == c) {
                                cuttedStr = str.substring(0, i) + str.substring(i + 1);
                                break;
                            }
                        }
                        int NUM = cuttedStr.length();
                        int zeroIndex = -1;
                        for (int i = 0; i < NUM - 2; i++) {
                            char c = cuttedStr.charAt(i);
                            if (c != '0') {
                                zeroIndex = i;
                                break;
                            } else if (i == NUM - 3) {
                                zeroIndex = i;
                                break;
                            }
                        }
                        if (zeroIndex != -1) {
                            cuttedStr = cuttedStr.substring(zeroIndex);
                        }
                        if (cuttedStr.length() < 3) {
                            cuttedStr = "0" + cuttedStr;
                        }
                        cuttedStr = cuttedStr.substring(0, cuttedStr.length() - 2)
                                + "." + cuttedStr.substring(cuttedStr.length() - 2);

                        etData.setText(cuttedStr);
                        etData.setSelection(etData.length());
                        isChanged = false;
                    }
                });
            }
        } else {
            Tools.showDialogErrorCallback(this, "Error en obtener informacion de la data", new DialogCallback() {
                @Override
                public void onDialogCallback(int value) {
                    finish();
                }
            });
        }
    }

    public void enviarMonto(View view) {
        if (view != null) {
            if (transaccion.getTipo() != null) {
                if (transaccion.getTipo() == DataTransaccion.TipoDato.CEDULA) {
                    boolean validated = StringTools.INSTANCE.ValidaCedulaRuc(etData.getText().toString());
                    if (validated) {
                        Intent data = new Intent();
                        data.putExtra("value", etData.getText().toString());
                        setResult(CommonStatusCodes.SUCCESS, data);
                        finish();
                    } else {
                        etData.setError("Documento de identidad no valido");
                        Toast.makeText(this, "Documento no valido", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Intent data = new Intent();
                    data.putExtra("value", etData.getText().toString());
                    setResult(CommonStatusCodes.SUCCESS, data);
                    finish();
                }
            }
        }
    }
}