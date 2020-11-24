package com.code93.linkcoop.ui;

import androidx.appcompat.app.AppCompatActivity;
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
import com.code93.linkcoop.network.DownloadCallback;
import com.code93.linkcoop.network.DownloadXmlTask;
import com.code93.linkcoop.xmlParsers.XmlParser;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        /*String userLogin = SP2.Companion.getInstance(this).getString("userLogin", "");
        if (!userLogin.equals("")) {
            String xmlLogOff = ToolsXML.requestLogoff(userLogin);

            DownloadXmlTask task = new DownloadXmlTask(xmlLogOff, this);
            task.execute(xmlLogOff);



            *//*SegundoPlano tarea = new SegundoPlano(xmlLogOff);
            tarea.execute();*//*
        } else {
               
        }*/
        AesBase64Wrapper aes = new AesBase64Wrapper();

        String xmlLogOff = ToolsXML.requestLogoff(aes.encryptAndEncode("inavarrete"));

        DownloadXmlTask task = new DownloadXmlTask(xmlLogOff, this);
        task.execute(xmlLogOff);
    }

    public void procesarRespuesta(String response) {
        XmlParser logon = XmlParser.INSTANCE;
        InputStream inputStream = new ByteArrayInputStream(response.getBytes());
        try {
            FieldsTrx fieldsTrx = logon.parse(inputStream, "reply_logoff");
            Log.d("FieldTRX", Objects.requireNonNull(fieldsTrx.getToken_data()));
            TokenData tokenData = new TokenData();
            List<TokenData> listTokens =  tokenData.getTokens(Objects.requireNonNull(fieldsTrx.getToken_data()));
            Log.d("ListToken", listTokens.get(0).getIdToken());
            if (fieldsTrx.getResponse_code().equals("00")) {
                MyApp.sp2.putBoolean(SP2.Companion.getSP_LOGIN(), true);
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                showMensaje(listTokens);
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showMensaje(List<TokenData> listTokens) {
        for (TokenData tokenData : listTokens) {
            if (tokenData.getIdToken().equals("B1")){
                Tools.showDialogError(this, tokenData.getDataToken());
                break;
            }
        }
    }

    @Override
    public void onDownloadCallback(@NotNull String response) {
        procesarRespuesta(response);
    }
}