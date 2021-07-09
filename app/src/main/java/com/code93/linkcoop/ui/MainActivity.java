package com.code93.linkcoop.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.code93.linkcoop.DialogCallback;
import com.code93.linkcoop.models.FieldsTrx;
import com.code93.linkcoop.MyApp;
import com.code93.linkcoop.R;
import com.code93.linkcoop.TokenData;
import com.code93.linkcoop.Tools;
import com.code93.linkcoop.ToolsXML;
import com.code93.linkcoop.cache.SP2;
import com.code93.linkcoop.network.DownloadCallback;
import com.code93.linkcoop.network.DownloadXmlTask;
import com.code93.linkcoop.viewmodel.CooperativaViewModel;
import com.code93.linkcoop.xmlParsers.XmlParser;

import org.jetbrains.annotations.NotNull;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import dmax.dialog.SpotsDialog;


public class MainActivity extends AppCompatActivity implements DownloadCallback {

    CooperativaViewModel viewModel;
    private AlertDialog spotDialog;
    TextView tvUltimoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(CooperativaViewModel.class);

        spotDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Procesando")
                .build();

        tvUltimoLogin = findViewById(R.id.tvUltimoLogin);
        obtenerUltimoLogin();

        TextView userLogin = findViewById(R.id.userLogin);
        userLogin.setText(SP2.Companion.getInstance(this).getString("email", ""));
        TextView comercioName = findViewById(R.id.comercioName);
        comercioName.setText(MyApp.sp2.getString(SP2.Companion.getComercio_nombre(), ""));

    }

    private void obtenerUltimoLogin() {
        String fechaUltimoLogin = SP2.Companion.getInstance(this).getString(SP2.Companion.getFechaUltimoLogin(), "");

        String formatUltimoLogin = fechaUltimoLogin;

        tvUltimoLogin.setText(formatUltimoLogin);

    }

    @Override
    protected void onStart() {
        super.onStart();
        verificarFechaUltimoLogin();
    }

    @Override
    protected void onPause() {
        super.onPause();
        verificarFechaUltimoLogin();
    }

    public void clickCerrarSesion(View view) {
        cerrarSesion();
    }

    private void cerrarSesion(){
        spotDialog.show();
        String user_encript = MyApp.sp2.getString(SP2.Companion.getUser_encript(), "");
        String xmlLogOff = ToolsXML.requestLogoff(user_encript);
        DownloadXmlTask task = new DownloadXmlTask(xmlLogOff, this);
        task.execute(xmlLogOff);
    }

    @Override
    public void onDownloadCallback(@NotNull String response) {
        if (response.equals("Error de conexion")) {
            showErrorConexion();
        } else {
            procesarRespuesta(response);
        }

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

    private void showErrorConexion() {
        spotDialog.dismiss();
        Tools.showDialogErrorCallback(this, "Error de conexion", value -> {
            MyApp.sp2.putBoolean(SP2.Companion.getSP_LOGIN(), false);
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });
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
        //startActivity(new Intent(this, ReportesActivity.class));
        startActivity(new Intent(this, MenuReportesActivity.class));
    }

    int cont = 0;

    @Override
    public void onBackPressed() {
        if (cont == 0) {
            cont += 1;
        } else {
            finishAffinity();
        }
    }

    private void verificarFechaUltimoLogin() {
        long day = 0;
        long diff = 0;

        String fechaUltimoLogin = SP2.Companion.getInstance(this).getString(SP2.Companion.getFechaUltimoLogin(), "NO");
        if (!fechaUltimoLogin.equals("NO")) {
            String fechaLogin = fechaUltimoLogin.substring(0, fechaUltimoLogin.indexOf(" "));
            Log.d("FECHA ULTIMA", fechaLogin);
            String outputPattern = "yyyy-MM-dd HH:mm:ss";
            SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
            try {
                Date date1 = outputFormat.parse(fechaUltimoLogin);
                Date date2 = outputFormat.parse(Tools.getLocalDateTime());
                diff = date2.getTime() - date1.getTime();
                day = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

                Log.d("DIAS DE ULTIMO LOGIN", ""+day);

                if (day >= 2) {
                    //cerrarSesion();
                    solicitarCerrarSesion();
                }
            }catch (Exception e) {
                //sp2.putBoolean(SP2.Companion.getSP_LOGIN(), false);
            }
        } else {
            //sp2.putBoolean(SP2.Companion.getSP_LOGIN(), false);
        }
    }

    private void solicitarCerrarSesion() {
        Tools.showDialogErrorCallback(this, "Llevas dos dias sin iniciar sesión. Inicia sesión nuevamente.", new DialogCallback() {
            @Override
            public void onDialogCallback(int value) {
                cerrarSesion();
            }
        });
    }
}