package com.code93.linkcoop.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.code93.linkcoop.adapters.MenuReferenciasAdapter;
import com.code93.linkcoop.cache.SP2;
import com.code93.linkcoop.models.Comercio;
import com.code93.linkcoop.models.FieldsTrx;
import com.code93.linkcoop.R;
import com.code93.linkcoop.TokenData;
import com.code93.linkcoop.Tools;
import com.code93.linkcoop.ToolsXML;
import com.code93.linkcoop.models.Cooperativa;
import com.code93.linkcoop.models.Instituciones;
import com.code93.linkcoop.models.LogTransacciones;
import com.code93.linkcoop.models.Referencias;
import com.code93.linkcoop.models.Transaction;
import com.code93.linkcoop.network.DownloadXmlTask;
import com.code93.linkcoop.viewmodel.LogTransaccionesViewModel;
import com.code93.linkcoop.xmlParsers.XmlParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import dmax.dialog.SpotsDialog;

public class TransaccionActivity extends AppCompatActivity implements MenuReferenciasAdapter.OnClickElemetos {

    private static final int RTA_ELEMENTO = 101;
    RecyclerView rvElementos;
    TextView tvTransaccion;

    Comercio comercio;
    Cooperativa cooperativa;
    Instituciones instituciones;
    Transaction transaccion;
    List<Referencias> referencias;
    Referencias refeSelect;

    MenuReferenciasAdapter adapter;

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
            instituciones = (Instituciones) getIntent().getParcelableExtra("instituciones");
            //referencias = getElementos(transaccion.get_namet().trim());
            referencias = transaccion.getReferencias();
            tvTransaccion.setText(transaccion.getNameTransaction().trim());
        } else {

        }

        rvElementos = findViewById(R.id.rvElementos);

        adapter = new MenuReferenciasAdapter(this);
        adapter.setReferencias(referencias);
        adapter.setOnClickElemento(this);
        rvElementos.setLayoutManager(new LinearLayoutManager(this));
        rvElementos.setAdapter(adapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RTA_ELEMENTO) {
            int pos = referencias.indexOf(refeSelect);
            Referencias value = null;
            if (data != null) {
                value = data.getParcelableExtra("value");
                referencias.set(pos, value);
                adapter.setReferencias(referencias);
                adapter.notifyDataSetChanged();
            }



        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onItemClick(RecyclerView.ViewHolder item, int position, int id) {
        Intent intent = new Intent(this, IngresarReferenciaActivity.class);
        refeSelect = referencias.get(position);
        intent.putExtra("nameTrx", transaccion.getNameTransaction().trim());
        intent.putExtra("referencia", refeSelect);
        startActivityForResult(intent, RTA_ELEMENTO);
    }

    public void analizarDatos(View view) {
        boolean camposOk = true;
        for (Referencias refe : referencias) {
            if (refe.getValue() == null || refe.getValue().equals("")) {
                Toast.makeText(this, "El " + refe.getDescription().trim() + " no se ha completado.",
                        Toast.LENGTH_SHORT).show();
                camposOk = false;
                break;
            }
        }
        if (camposOk) {
            spotDialog.show();

            procesarTransaccion();

            /*switch (transaccion.getNameTransaction().trim()) {
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
            }*/
        }

    }

    private void procesarTransaccion() {

        String xml = ToolsXML.requestLinkCoop(transaccion, instituciones, referencias);

        Log.d("XML TRANSACCION: ", xml);

        try {
            fieldsTrxSend = XmlParser.parse(xml, transaccion.getTagRequest());
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        DownloadXmlTask task = new DownloadXmlTask(xml, response -> {
            if (response.equals("Error de conexion"))
                showErrorConexion();
            else
                procesarRespuesta(transaccion.getTagReply(), response);

        });
        task.execute(xml);


    }

    FieldsTrx fieldsTrxSend = new FieldsTrx();

    private void retiroAhorros() {
        String monto = "";
        String numeroDeCuenta = "";
        String otp = "";
        String documento = "";
        /*for (Referencias data : referencias) {
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
        }*/

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
        /*for (DataTransaccion data : referencias) {
            if (data.getName().equals("Numero de cuenta")) {
                numeroDeCuenta = data.getValue();
                continue;
            }
            if (data.getName().equals("Documento de identidad")) {
                documento = data.getValue();
            }
        }*/

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
        /*for (DataTransaccion data : referencias) {
            if (data.getName().equals("Numero de cuenta")) {
                numeroDeCuenta = data.getValue();
                break;
            }
        }*/

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
        /*for (DataTransaccion data : referencias) {
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
                    throw new IllegalStateException("Unexpected value: " + data.getName());
            }
        }*/

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
                        0, comercio, instituciones, transaccion, fieldsTrxSend, fieldsTrx);

                viewModel.addLogTransacciones(logTransacciones);
                if (fieldsTrx.getResponse_code().equals("000")) {
                    spotDialog.dismiss();
                    switch (transaccion.getNameTransaction().trim()) {
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
                            throw new IllegalStateException("Unexpected value: ");
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