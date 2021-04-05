package com.code93.linkcoop.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.code93.linkcoop.models.FieldsTrx;
import com.code93.linkcoop.MyApp;
import com.code93.linkcoop.R;
import com.code93.linkcoop.TokenData;
import com.code93.linkcoop.Tools;
import com.code93.linkcoop.ToolsXML;
import com.code93.linkcoop.cache.SP2;
import com.code93.linkcoop.network.DownloadCallback;
import com.code93.linkcoop.network.DownloadXmlTask;
import com.code93.linkcoop.viewmodel.InstitucionesViewModel;
import com.code93.linkcoop.xmlParsers.XmlParser;

import org.jetbrains.annotations.NotNull;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Objects;

import dmax.dialog.SpotsDialog;


public class MainActivity extends AppCompatActivity implements DownloadCallback {

    InstitucionesViewModel viewModel;

    private AlertDialog spotDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(InstitucionesViewModel.class);

        spotDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Procesando")
                .build();

        TextView userLogin = findViewById(R.id.userLogin);
        userLogin.setText(SP2.Companion.getInstance(this).getString("email", ""));
        TextView comercioName = findViewById(R.id.comercioName);
        comercioName.setText(MyApp.sp2.getString(SP2.Companion.getComercio_nombre(), ""));

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void clickCerrarSesion(View view) {
        spotDialog.show();
        String user_encript = MyApp.sp2.getString(SP2.Companion.getUser_encript(), "");
        String xmlLogOff = ToolsXML.requestLogoff(user_encript);
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
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                });
            } else {
                spotDialog.dismiss();
                Tools.showDialogErrorCallback(this, tokenData.getB1(), value -> {
                    MyApp.sp2.putBoolean(SP2.Companion.getSP_LOGIN(), false);
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                });
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDownloadCallback(@NotNull String response) {
        if (response.equals("Error de conexion")) {
            showErroConexion();
        } else {
            procesarRespuesta(response);
        }

    }

    private void showErroConexion() {
        spotDialog.dismiss();
        Tools.showDialogError(this, "Error de conexion");
    }

    public void visualizarCooperativas(View view) {
        startActivity(new Intent(this, CooperativasActivity.class));
    }

    public void sobreAplicacion(View view) {


    }

    public void configuracion(View view) {
        startActivity(new Intent(this, ConfiguracionActivity.class));
    }

    public void reportes(View view) {
        startActivity(new Intent(this, ReportesActivity.class));
    }
}