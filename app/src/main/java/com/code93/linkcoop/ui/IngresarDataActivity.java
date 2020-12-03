package com.code93.linkcoop.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.code93.linkcoop.DialogCallback;
import com.code93.linkcoop.R;
import com.code93.linkcoop.Tools;
import com.code93.linkcoop.models.DataTransaccion;
import com.code93.linkcoop.models.Transaction;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class IngresarDataActivity extends AppCompatActivity {

    TextInputEditText etData;
    TextInputLayout tilData;
    TextView tvNomTrans;

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
            DataTransaccion transaccion = (DataTransaccion) getIntent().getSerializableExtra("elemento");
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
                /*numeroTotal = Double.valueOf(amountServiceCost);
                numeroTotal = numeroTotal + Double.valueOf(cuttedStr);
                String auxNum = String.valueOf(numeroTotal);

                if (auxNum.length() >= 3) {
                    String[] parts = auxNum.split("\\.");
                    String p = parts[1];
                    if (p.length() == 1)
                        auxNum += "0";
                }
                tv_Total.setText(auxNum);*/
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
            Intent data = new Intent();
            data.putExtra("value", etData.getText().toString());
            setResult(CommonStatusCodes.SUCCESS, data);
            finish();
        }
    }
}