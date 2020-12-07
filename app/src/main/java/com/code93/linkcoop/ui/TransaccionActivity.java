package com.code93.linkcoop.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.code93.linkcoop.DialogCallback;
import com.code93.linkcoop.FieldsTrx;
import com.code93.linkcoop.MyApp;
import com.code93.linkcoop.R;
import com.code93.linkcoop.TokenData;
import com.code93.linkcoop.Tools;
import com.code93.linkcoop.ToolsXML;
import com.code93.linkcoop.adapters.MenuElementosAdapter;
import com.code93.linkcoop.cache.SP2;
import com.code93.linkcoop.models.Cooperativa;
import com.code93.linkcoop.models.DataTransaccion;
import com.code93.linkcoop.models.LoginCooperativas;
import com.code93.linkcoop.models.Transaction;
import com.code93.linkcoop.network.DownloadXmlTask;
import com.code93.linkcoop.xmlParsers.XmlParser;
import com.google.gson.Gson;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TransaccionActivity extends AppCompatActivity implements MenuElementosAdapter.OnClickElemetos {

    private static final int RTA_ELEMENTO = 101;
    RecyclerView rvElementos;
    TextView tvTransaccion;

    Cooperativa cooperativa;
    Transaction transaccion;
    List<DataTransaccion> elementos;
    DataTransaccion elemSelect;

    MenuElementosAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaccion);

        tvTransaccion = findViewById(R.id.tvTransaccion);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            transaccion = (Transaction) getIntent().getParcelableExtra("transaction");
            elementos = getElementos(transaccion.get_namet().trim());
            tvTransaccion.setText(transaccion.get_namet().trim());
        } else {

        }

        rvElementos = findViewById(R.id.rvElementos);

        adapter = new MenuElementosAdapter(this);
        adapter.setElementos(elementos);
        adapter.setOnClickElemento(this);
        rvElementos.setLayoutManager(new LinearLayoutManager(this));
        rvElementos.setAdapter(adapter);

    }

    private List<DataTransaccion> getElementos(String nameTrx) {
        List<DataTransaccion> elementos = new ArrayList<>();
        switch (nameTrx) {
            case "RETIRO AHORROS":
                elementos.add(new DataTransaccion("Monto",
                        InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL,
                        9, "Ingresa el monto", R.drawable.ic_money));
                elementos.add(new DataTransaccion("Numero de cuenta",
                        InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL,
                        12, "Ingresa el numero de cuenta", R.drawable.ic_account_balance));
                elementos.add(new DataTransaccion("OTP Generado",
                        InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL,
                        9, "Ingresa la OTP", R.drawable.ic_textsms));
                elementos.add(new DataTransaccion("Documento de identidad",
                        InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL,
                        15, "Ingresa documento de identidad", R.drawable.ic_account));
                break;
            case "CONSULTA DE SALDOS":
                elementos.add(new DataTransaccion("Numero de cuenta",
                        InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL,
                        12, "Ingresa el numero de cuenta", R.drawable.ic_account_balance));
                elementos.add(new DataTransaccion("Documento de identidad",
                        InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL,
                        15, "Ingresa documento de identidad", R.drawable.ic_account));
                break;
            case "GENERACION OTP":
                elementos.add(new DataTransaccion("Numero de cuenta",
                        InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL,
                        12, "Ingresa el numero de la cuenta a consultar", R.drawable.ic_account_balance));
                break;
            case "DEPOSITO AHORROS":
                elementos.add(new DataTransaccion("Monto",
                        InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL,
                        9, "Ingresa el monto", R.drawable.ic_money));
                elementos.add(new DataTransaccion("Numero de cuenta",
                        InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL,
                        12, "Ingresa el numero de cuenta", R.drawable.ic_account_balance));
                elementos.add(new DataTransaccion("Documento del depositante",
                        InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL,
                        15, "Ingresa documento de identidad", R.drawable.ic_account));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + nameTrx);
        }

        return elementos;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RTA_ELEMENTO) {
            int pos = elementos.indexOf(elemSelect);
            String value = null;
            if (data != null) {
                value = data.getStringExtra("value");
            }
            elementos.set(pos, new DataTransaccion(elemSelect, value));
            //transaccion.setDataTrans(elementos);
            adapter.setElementos(elementos);
            adapter.notifyDataSetChanged();
            /*adapter.setOnClickElemento(this);
            rvElementos.setLayoutManager(new LinearLayoutManager(this));
            rvElementos.setAdapter(adapter);*/

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onItemClick(RecyclerView.ViewHolder item, int position, int id) {
        Intent intent = new Intent(this, IngresarDataActivity.class);
        elemSelect = elementos.get(position);
        intent.putExtra("nameTrx", transaccion.get_namet().trim());
        intent.putExtra("elemento", elemSelect);
        startActivityForResult(intent, RTA_ELEMENTO);
    }

    public void analizarDatos(View view) {
        boolean camposOk = true;
        for (DataTransaccion data : elementos) {
            if (data.getValue() == null || data.getValue().equals("")) {
                Toast.makeText(this, "El " + data.getName().trim() + " no se ha completado.", Toast.LENGTH_SHORT).show();
                camposOk = false;
                break;
            }
        }
        if (camposOk) {
            switch (transaccion.get_namet().trim()) {
                case "RETIRO AHORROS":
                    retiroAhorros();
                    break;
                case "CONSULTA DE SALDOS":
                    consultaSaldo();
                    break;
                case "GENERACION OTP":
                    generacionOtp();
                    break;
                case "DEPOSITO AHORROS":
                   depositoAhorros();
                   break;
                default:
                    throw new IllegalStateException("Unexpected value: ");
            }
        }

    }

    private void retiroAhorros() {
        String monto = "";
        String numeroDeCuenta = "";
        String otp = "";
        String documento = "";
        for (DataTransaccion data : elementos) {
            if (data.getName().equals("Monto")) {
                monto = data.getValue();
                continue;
            }
            if (data.getName().equals("Numero de cuenta")) {
                numeroDeCuenta = data.getValue();
                continue;
            }
            if (data.getName().equals("OTP Generado")) {
                otp = data.getValue();
                continue;
            }
            if (data.getName().equals("Documento de identidad")) {
                documento = data.getValue();
            }
        }

        String xmlLogOff = ToolsXML.requestWithdrawal(transaccion.get_code().trim(), numeroDeCuenta, monto, otp, documento);

        DownloadXmlTask task = new DownloadXmlTask(xmlLogOff, response -> {
            procesarRespuesta("reply_withdrawal", response);
        });
        task.execute(xmlLogOff);
    }

    private void consultaSaldo() {
        String numeroDeCuenta = "";
        String documento = "";
        for (DataTransaccion data : elementos) {
            if (data.getName().equals("Numero de cuenta")) {
                numeroDeCuenta = data.getValue();
                continue;
            }
            if (data.getName().equals("Documento de identidad")) {
                documento = data.getValue();
            }
        }

        String xml = ToolsXML.requestInquiry(transaccion.get_code().trim(), numeroDeCuenta, documento);

        DownloadXmlTask task = new DownloadXmlTask(xml, response -> {
            procesarRespuesta("reply_inquiry", response);
        });
        task.execute(xml);
    }

    private void generacionOtp() {
        String numeroDeCuenta = "";
        for (DataTransaccion data : elementos) {
            if (data.getName().equals("Numero de cuenta")) {
                numeroDeCuenta = data.getValue();
                break;
            }
        }

        String xml = ToolsXML.requestGenerate(transaccion.get_code().trim(), numeroDeCuenta);

        DownloadXmlTask task = new DownloadXmlTask(xml, response -> {
            procesarRespuesta("reply_withdrawal", response);
        });
        task.execute(xml);

    }

    private void depositoAhorros() {

    }

    private void procesarRespuesta(String tagReply, String response) {
        try {
            FieldsTrx fieldsTrx = XmlParser.parse(response, tagReply);
            Log.d("FieldTRX", Objects.requireNonNull(fieldsTrx.getToken_data()));
            TokenData tokenData = new TokenData();
            tokenData.getTokens(Objects.requireNonNull(fieldsTrx.getToken_data()));
            if (fieldsTrx.getResponse_code().equals("000")) {
                Tools.showDialogPositive(TransaccionActivity.this, "Prueba", new DialogCallback() {
                    @Override
                    public void onDialogCallback(int value) {
                        finish();
                    }
                });
            } else {
                //spotDialog.dismiss();
                Tools.showDialogError(this, tokenData.getB1());
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
            Tools.showDialogError(this, "Error al procesar la respuesta.");
        }
    }
}