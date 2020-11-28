package com.code93.linkcoop.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.code93.linkcoop.AesBase64Wrapper;
import com.code93.linkcoop.FieldsTrx;
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


public class MainActivity extends AppCompatActivity implements DownloadCallback {

    ArrayList<Cooperativa> cooperativas;
    RecyclerView rvCooperativas;
    TextView mainImgBienvenido;
    SoapPrimitive resultString;

    CooperativaViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(CooperativaViewModel.class);

        TextView userLogin = findViewById(R.id.userLogin);
        userLogin.setText(SP2.Companion.getInstance(this).getString("email", ""));

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void prueba(View view) {
        /*SegundoPlano tarea = new SegundoPlano();
        tarea.execute();*/

    }

    public void clickCerrarSesion(View view) {
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
                //spotDialog.dismiss();
                MyApp.sp2.putBoolean(SP2.Companion.getSP_LOGIN(), false);
                Tools.showDialogPositive(this, tokenData.getB1(), value -> {
                    startActivity(new Intent(MainActivity.this, Login.class));
                    finish();
                });
            } else {
                //spotDialog.dismiss();
                Tools.showDialogError(this, tokenData.getB1());
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
        String json = "{\"cooperativas\":[{\"_id\":\"5000\",\"_namec\":\"COOPERATIVA MIFEX\",\"_transaction\":[{\"_code\":\"301000\",\"_namet\":\"CONSULTA DE SALDOS\",\"_cost\":\"0.50\"},{\"_code\":\"306000\",\"_namet\":\"GENERACION OTP\",\"_cost\":\"0.00\"},{\"_code\":\"501020\",\"_namet\":\"RETIRO DE AHORROS\",\"_cost\":\"0.53\"}]},{\"_id\":\"5001\",\"_namec\":\"COOPERATIVA DE LOS MAESTROS\",\"_transaction\":[{\"_code\":\"301000\",\"_namet\":\"CONSULTA DE SALDOS\",\"_cost\":\"0.50\"},{\"_code\":\"306000\",\"_namet\":\"GENERACION OTP\",\"_cost\":\"0.00\"},{\"_code\":\"501020\",\"_namet\":\"RETIRO DE AHORROS\",\"_cost\":\"0.53\"},{\"_code\":\"001000\",\"_namet\":\"DEPOSITO DE AHORROS\",\"_cost\":\"0.45\"}]}]}";
        Gson gson = new Gson();
        LoginCooperativas logCoop = gson.fromJson(json, LoginCooperativas.class);
        Cooperativa coop1 = logCoop.getCooperativas().get(0);
        Log.d("COOP", coop1.get_namec());


        for (Cooperativa coop : logCoop.getCooperativas()) {
            viewModel.addCooperativa(coop);
            viewModel.updateCooperativa(coop);
        }
    }
}