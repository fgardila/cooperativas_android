package com.code93.linkcoop.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.code93.linkcoop.R;
import com.code93.linkcoop.adapters.MenuCoopAdapter;
import com.code93.linkcoop.cache.SP2;
import com.code93.linkcoop.models.Cooperativa;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlSerializer;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

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

    }

    private class SegundoPlano extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            sendRequest();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mainImgBienvenido.setText("RTA = " + resultString.toString());
        }
    }

    private void sendRequest() {
        String SOAP_ACTION = "http://tempuri.org/iServiceAsynchronous/Get_Status_Service";
        String METHOD_NAME = "Get_Status_Service";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://190.216.106.14:1992/AsynchronousService.svc";

        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("Code", "0");
            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);
            HttpTransportSE transport = new HttpTransportSE(URL);
            transport.call(SOAP_ACTION, soapEnvelope);
            resultString = (SoapPrimitive) soapEnvelope.getResponse();
        } catch (Exception e) {

        }
    }
}