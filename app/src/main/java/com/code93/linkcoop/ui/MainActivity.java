package com.code93.linkcoop.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.TextView;

import com.code93.linkcoop.DataField;
import com.code93.linkcoop.R;
import com.code93.linkcoop.Tools;
import com.code93.linkcoop.adapters.MenuCoopAdapter;
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

        mainImgBienvenido = findViewById(R.id.mainImgBienvenido);

        cooperativas = new ArrayList<>();
        rvCooperativas = findViewById(R.id.rvCooperativas);
        cargarArrayList();
        MenuCoopAdapter adapterMenus = new MenuCoopAdapter(cooperativas, this);
        adapterMenus.setOnClickCoop((item, position, id) -> {
            Cooperativa selectCoop = cooperativas.get(position);
            //Toast.makeText(MainActivity.this, "Select Coop " + selectCoop.getNombreCoop() , Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, TransaccionesActivity.class);
            intent.putExtra("Cooperativa", selectCoop);
            startActivity(intent);
        });
        rvCooperativas.setLayoutManager(new GridLayoutManager(this, 2));
        rvCooperativas.setAdapter(adapterMenus);
    }

    private void cargarArrayList() {
        cooperativas.add(new Cooperativa("Andalucia",
                "https://firebasestorage.googleapis.com/v0/b/connectcoop-ecuador.appspot.com/o/logosCoop%2Flogo_andalucia.png?alt=media&token=1d368515-75b6-4853-960b-b33fcfa84c77"));
        cooperativas.add(new Cooperativa("La 29",
                "https://firebasestorage.googleapis.com/v0/b/connectcoop-ecuador.appspot.com/o/logosCoop%2Flogo_la29.png?alt=media&token=3d8d4dff-3a35-477d-bc6d-ecde8ebf21da"));
        cooperativas.add(new Cooperativa("Alianza del Valle",
                "https://firebasestorage.googleapis.com/v0/b/connectcoop-ecuador.appspot.com/o/logosCoop%2Flogo_alianza.png?alt=media&token=3ee5e42b-d7ba-46e1-92bc-7eff6c68d310"));
        cooperativas.add(new Cooperativa("Cooperativa Policia Nacional",
                "https://firebasestorage.googleapis.com/v0/b/connectcoop-ecuador.appspot.com/o/logosCoop%2Flogo_cpn_black.png?alt=media&token=38acc228-35f1-4408-b286-d30bacba5d0f"));
        cooperativas.add(new Cooperativa("Cooprogrerso",
                "https://firebasestorage.googleapis.com/v0/b/connectcoop-ecuador.appspot.com/o/logosCoop%2Flogo_cooprogreso.png?alt=media&token=c0bb3ee0-892c-4859-a71b-478d70fa42ad"));

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void prueba(View view) {
        SegundoPlano tarea = new SegundoPlano();
        tarea.execute();
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