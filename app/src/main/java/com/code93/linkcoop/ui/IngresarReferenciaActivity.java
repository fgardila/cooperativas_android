package com.code93.linkcoop.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.code93.linkcoop.DialogCallback;
import com.code93.linkcoop.R;
import com.code93.linkcoop.Tools;
import com.code93.linkcoop.models.Referencias;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class IngresarReferenciaActivity extends AppCompatActivity {

    TextInputEditText etData;
    TextInputLayout tilData;
    TextView tvNomTrans;
    Referencias referencia;

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
            referencia = (Referencias) getIntent().getParcelableExtra("referencia");
            tilData.setHint(referencia.getDescription());
            if (referencia.getData_type().trim().toLowerCase().equals("amount")) {
                tilData.setStartIconDrawable(R.drawable.ic_money);
            } else {
                tilData.setStartIconDrawable(R.drawable.ic_account_balance);
            }

            /*etData.setInputType(transaccion.getInputType());
            etData.setFilters(new InputFilter[]{new
                    InputFilter.LengthFilter(transaccion.getMaxLength())});*/
            if (referencia.getValue() != null) {
                etData.setText(referencia.getValue());
            }
            setFormatMonto();

        } else {
            Tools.showDialogErrorCallback(this, "Error en obtener informacion de la data", new DialogCallback() {
                @Override
                public void onDialogCallback(int value) {
                    finish();
                }
            });
        }
    }

    private void setFormatMonto() {
        if (referencia.getData_type().trim().toLowerCase().equals("amount")) {
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
    }

    public void enviarMonto(View view) {
        if (view != null) {
            Intent data = new Intent();
            referencia.setValue(etData.getText().toString());
            data.putExtra("value", referencia);
            setResult(CommonStatusCodes.SUCCESS, data);
            finish();
        }
    }
}