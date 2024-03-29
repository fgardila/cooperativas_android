package com.code93.linkcoop.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.code93.linkcoop.DialogCallback;
import com.code93.linkcoop.cache.SP2;
import com.code93.linkcoop.models.Comercio;
import com.code93.linkcoop.models.FieldsTrx;
import com.code93.linkcoop.R;
import com.code93.linkcoop.TokenData;
import com.code93.linkcoop.Tools;
import com.code93.linkcoop.ToolsXML;
import com.code93.linkcoop.adapters.MenuElementosAdapter;
import com.code93.linkcoop.models.Cooperativa;
import com.code93.linkcoop.models.DataTransaccion;
import com.code93.linkcoop.models.LogTransacciones;
import com.code93.linkcoop.models.Transaction;
import com.code93.linkcoop.network.DownloadXmlTask;
import com.code93.linkcoop.viewmodel.FieldsTrxViewModel;
import com.code93.linkcoop.viewmodel.LogTransaccionesViewModel;
import com.code93.linkcoop.xmlParsers.XmlParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dmax.dialog.SpotsDialog;

public class TransaccionActivity extends AppCompatActivity implements MenuElementosAdapter.OnClickElemetos {

    private static final int RTA_ELEMENTO = 101;
    RecyclerView rvElementos;
    TextView tvTransaccion;

    Comercio comercio;
    Cooperativa cooperativa;
    Transaction transaccion;
    List<DataTransaccion> elementos;
    DataTransaccion elemSelect;

    MenuElementosAdapter adapter;

    private LogTransaccionesViewModel viewModel;

    private AlertDialog spotDialog;

    final String depositoAhorros = "DEPOSITO AHORROS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaccion);

        viewModel = new ViewModelProvider(this).get(LogTransaccionesViewModel.class);

        tvTransaccion = findViewById(R.id.tvTransaccion);

        spotDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Procesando")
                .build();

        Bundle extras = getIntent().getExtras();
        SP2 sp2 = SP2.Companion.getInstance(this);
        if (extras != null) {
            comercio = new Comercio();
            comercio.setNombre(sp2.getString(SP2.Companion.getComercio_nombre(), ""));
            comercio.setRuc(sp2.getString(SP2.Companion.getComercio_ruc(), ""));
            comercio.setDireccion(sp2.getString(SP2.Companion.getComercio_direccion(), ""));
            transaccion = (Transaction) getIntent().getParcelableExtra("transaction");
            cooperativa = (Cooperativa) getIntent().getParcelableExtra("cooperativa");
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
            case "CONSULTA SALDOS CC":
            case "CONSULTA SALDOS AH":
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
            case depositoAhorros:
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
                Tools.showDialogErrorCallback(this, "Transacción no disponible " + nameTrx, new DialogCallback() {
                    @Override
                    public void onDialogCallback(int value) {
                        finish();
                    }
                });
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
            adapter.setElementos(elementos);
            adapter.notifyDataSetChanged();


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
                Toast.makeText(this, "El " + data.getName().trim() + " no se ha completado.",
                        Toast.LENGTH_SHORT).show();
                camposOk = false;
                break;
            }
        }
        if (camposOk) {
            spotDialog.show();
            switch (transaccion.get_namet().trim()) {
                case "RETIRO AHORROS":
                    retiroAhorros();
                    break;
                case "CONSULTA DE SALDOS":
                case "CONSULTA SALDOS CC":
                case "CONSULTA SALDOS AH":
                    consultaSaldo();
                    break;
                case "GENERACION OTP":
                    generacionOtp();
                    break;
                case depositoAhorros:
                   depositoAhorros();
                   break;
                default:
                    spotDialog.dismiss();
                    Tools.showDialogError(this, "Transaccion no disponible");
            }
        }

    }

    FieldsTrx fieldsTrxSend = new FieldsTrx();

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

        String xml = ToolsXML.requestWithdrawal(transaccion, cooperativa, numeroDeCuenta,
                monto, otp, documento);

        try {
            fieldsTrxSend = XmlParser.parse(xml, "request_withdrawal");
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        DownloadXmlTask task = new DownloadXmlTask(xml, response -> {
            if (response.equals("Error de conexion"))
                showErrorConexion();
            else
                procesarRespuesta("reply_withdrawal", response);

        });
        task.execute(xml);
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

        String xml = ToolsXML.requestInquiry(transaccion, cooperativa, numeroDeCuenta, documento);

        try {
            fieldsTrxSend = XmlParser.parse(xml, "request_inquiry");
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }


        DownloadXmlTask task = new DownloadXmlTask(xml, response -> {
            if (response.equals("Error de conexion"))
                showErrorConexion();
            else
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

        String xml = ToolsXML.requestGenerate(transaccion, cooperativa, numeroDeCuenta);

        DownloadXmlTask task = new DownloadXmlTask(xml, response -> {
            if (response.equals("Error de conexion"))
                showErrorConexion();
            else
                procesarRespuesta("reply_generate", response);
        });
        task.execute(xml);

    }

    private void depositoAhorros() {
        String monto = "";
        String numeroDeCuenta = "";
        String documento = "";
        for (DataTransaccion data : elementos) {
            switch (data.getName()) {
                case "Monto":
                    monto = data.getValue();
                    break;
                case "Numero de cuenta":
                    numeroDeCuenta = data.getValue();
                    break;
                case "Documento del depositante":
                    documento = data.getValue();
                    break;
                default:
                    Tools.showDialogError(TransaccionActivity.this, "Referencia no disponible");
            }
        }

        String xml = ToolsXML.requestDeposit(transaccion, cooperativa, numeroDeCuenta,
                monto, documento);

        try {
            fieldsTrxSend = XmlParser.parse(xml, "request_deposit");
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        DownloadXmlTask task = new DownloadXmlTask(xml, response -> {
            if (response.equals("Error de conexion"))
                showErrorConexion();
            else
                procesarRespuesta("reply_deposit", response);
        });
        task.execute(xml);
    }

    private void procesarRespuesta(String tagReply, String response) {
        try {
            FieldsTrx fieldsTrx = XmlParser.parse(response, tagReply);
            if (!fieldsTrx.getToken_data().equals("")) {
                Log.d("FieldTRX", Objects.requireNonNull(fieldsTrx.getToken_data()));
                TokenData tokenData = new TokenData();
                tokenData.getTokens(Objects.requireNonNull(fieldsTrx.getToken_data()));

                LogTransacciones logTransacciones = new LogTransacciones(
                        0, comercio, cooperativa, transaccion, fieldsTrxSend, fieldsTrx);

                viewModel.addLogTransacciones(logTransacciones);
                if (fieldsTrx.getResponse_code().equals("000")) {
                    spotDialog.dismiss();
                    switch (transaccion.get_namet().trim()) {
                        case "RETIRO AHORROS":
                            procesarRetiroAhorros(logTransacciones, tokenData);
                            break;
                        case "CONSULTA DE SALDOS":
                        case "CONSULTA SALDOS CC":
                        case "CONSULTA SALDOS AH":
                            procesarConsultaSaldo(logTransacciones, tokenData);
                            break;
                        case "GENERACION OTP":
                            procesarGeneracionOtp(logTransacciones, tokenData);
                            break;
                        case depositoAhorros:
                            procesarDepositoAhorros(logTransacciones, tokenData);
                            break;
                        default:
                            Tools.showDialogError(TransaccionActivity.this, "Transaccion no disponible");
                    }
                } else {
                    spotDialog.dismiss();
                    Tools.showDialogErrorCallback(this, tokenData.getB1(), value -> finish());
                }
            } else {
                spotDialog.dismiss();
                Tools.showDialogError(this, "No llegaron Tokens");
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
            try {
                FieldsTrx fieldsTrx = XmlParser.parse(response, "default_reply_error");
                if (!fieldsTrx.getToken_data().equals("")) {
                    spotDialog.dismiss();
                    Log.d("FieldTRX", Objects.requireNonNull(fieldsTrx.getToken_data()));
                    TokenData tokenData = new TokenData();
                    tokenData.getTokens(Objects.requireNonNull(fieldsTrx.getToken_data()));
                    Tools.showDialogErrorCallback(this, tokenData.getB1(), value -> finish());
                } else {
                    spotDialog.dismiss();
                    Tools.showDialogError(this, "No llegaron Tokens");
                }
            } catch (XmlPullParserException | IOException o) {
                o.printStackTrace();
                spotDialog.dismiss();
                Tools.showDialogError(this, "Error al procesar la respuesta.");
            }
        }
    }

    private void procesarRetiroAhorros(LogTransacciones logTransacciones, TokenData tokenData) {
        Tools.showDialogPositive(this, tokenData.getB1(), value -> {
            Intent intent = new Intent(TransaccionActivity.this, ImpresionActivity.class);
            intent.putExtra("logTransacciones", logTransacciones);
            startActivity(intent);
            finish();
        });
    }

    private void procesarConsultaSaldo(LogTransacciones logTransacciones, TokenData tokenData) {
        Tools.showDialogPositive(this, tokenData.getB1(), value -> {
            Intent intent = new Intent(TransaccionActivity.this, ImpresionActivity.class);
            intent.putExtra("logTransacciones", logTransacciones);
            startActivity(intent);
            finish();
        });
    }

    private void procesarGeneracionOtp(LogTransacciones logTransacciones, TokenData tokenData) {
        Tools.showDialogPositive(this, tokenData.getB1(), value -> {
            Intent intent = new Intent(TransaccionActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void procesarDepositoAhorros(LogTransacciones logTransacciones, TokenData tokenData) {
        Tools.showDialogPositive(this, tokenData.getB1(), value -> {
            Intent intent = new Intent(TransaccionActivity.this, ImpresionActivity.class);
            intent.putExtra("logTransacciones", logTransacciones);
            finish();
        });
    }

    private void showErrorConexion() {
        spotDialog.dismiss();
        Tools.showDialogError(this, "Error de conexion");
    }
}