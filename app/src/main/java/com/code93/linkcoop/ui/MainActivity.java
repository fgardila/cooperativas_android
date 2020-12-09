package com.code93.linkcoop.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.code93.linkcoop.AesBase64Wrapper;
import com.code93.linkcoop.DialogCallback;
import com.code93.linkcoop.models.FieldsTrx;
import com.code93.linkcoop.MyApp;
import com.code93.linkcoop.R;
import com.code93.linkcoop.TokenData;
import com.code93.linkcoop.Tools;
import com.code93.linkcoop.ToolsXML;
import com.code93.linkcoop.adapters.MenuCoopAdapter;
import com.code93.linkcoop.cache.SP2;
import com.code93.linkcoop.models.Cooperativa;
import com.code93.linkcoop.models.LoginCooperativas;
import com.code93.linkcoop.network.DownloadCallback;
import com.code93.linkcoop.network.DownloadXmlTask;
import com.code93.linkcoop.viewmodel.CooperativaViewModel;
import com.code93.linkcoop.xmlParsers.XmlParser;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import dmax.dialog.SpotsDialog;


public class MainActivity extends AppCompatActivity implements DownloadCallback {

    CooperativaViewModel viewModel;

    private AlertDialog spotDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(CooperativaViewModel.class);

        spotDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Procesando")
                .build();

        TextView userLogin = findViewById(R.id.userLogin);
        userLogin.setText(SP2.Companion.getInstance(this).getString("email", ""));

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void clickCerrarSesion(View view) {
        spotDialog.show();
        AesBase64Wrapper aes = new AesBase64Wrapper();

        String xmlLogOff = ToolsXML.requestLogoff(aes.encryptAndEncode("lnavarrete"));

        DownloadXmlTask task = new DownloadXmlTask(xmlLogOff, this);
        task.execute(xmlLogOff);
    }

    public void procesarRespuesta(String response) {
        try {
            FieldsTrx fieldsTrx = XmlParser.parse(response, "reply_logoff");
            Log.d("FieldTRX", Objects.requireNonNull(fieldsTrx.getToken_data()));
            TokenData tokenData = new TokenData();
            tokenData.getTokens(Objects.requireNonNull(fieldsTrx.getToken_data()));
            if (fieldsTrx.getResponse_code().equals("00")) {
                MyApp.sp2.putBoolean(SP2.Companion.getSP_LOGIN(), false);
                spotDialog.dismiss();
                Tools.showDialogPositive(this, tokenData.getB1(), value -> {
                    startActivity(new Intent(MainActivity.this, Login.class));
                    finish();
                });
            } else {
                spotDialog.dismiss();
                Tools.showDialogErrorCallback(this, tokenData.getB1(), value -> {
                    MyApp.sp2.putBoolean(SP2.Companion.getSP_LOGIN(), false);
                    startActivity(new Intent(MainActivity.this, Login.class));
                    finish();
                });
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDownloadCallback(@NotNull String response) {
        procesarRespuesta(response);
    }

    public void visualizarCooperativas(View view) {
        startActivity(new Intent(this, CooperativasActivity.class));
    }

    public void sobreAplicacion(View view) {
        startActivity(new Intent(this, ImpresionActivity.class));
    }

    public void configuracion(View view) {
        startActivity(new Intent(this, ConfiguracionActivity.class));
    }

    public void reportes(View view) {
        startActivity(new Intent(this, ReportesActivity.class));
    }
}