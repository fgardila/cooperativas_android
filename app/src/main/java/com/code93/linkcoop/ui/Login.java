package com.code93.linkcoop.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.code93.linkcoop.AesBase64Wrapper;
import com.code93.linkcoop.DataElements;
import com.code93.linkcoop.R;
import com.code93.linkcoop.Tools;
import com.code93.linkcoop.ToolsXML;
import com.code93.linkcoop.adapters.MenuCoopAdapter;
import com.code93.linkcoop.models.Cooperativa;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.ktx.Firebase;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class Login extends AppCompatActivity {

    private TextInputEditText etEmail;
    private TextInputEditText etPassword;
    private MaterialCheckBox checkbox;

    private AlertDialog spotDialog;
    private FirebaseAuth auth;
    private SoapPrimitive resultString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        spotDialog = new SpotsDialog.Builder().setContext(this).build();

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        checkbox = findViewById(R.id.checkbox);

        ArrayList<String> datos = sharedPreferences();
        if (datos.get(0) != null) {
            etEmail.setText(datos.get(0));
            etPassword.setText(datos.get(1));
            checkbox.setChecked(true);
        }

        ImageView imgConnectCoop = findViewById(R.id.imgConnectCoop);
        imgConnectCoop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, MainActivity.class));
            }
        });
    }

    private ArrayList<String> sharedPreferences() {
        ArrayList<String> data = new ArrayList<>();
        SharedPreferences preferences = this.getSharedPreferences("acceso", MODE_PRIVATE);
        data.add(0, preferences.getString("email", null));
        data.add(1, preferences.getString("pwd", null));
        return data;
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser user) {
        if (user == null) {
            // Sign Anonymously
            auth.signInAnonymously()
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d("TAG", "signInAnonymously:success");
                                FirebaseUser newUser = auth.getCurrentUser();
                                updateUI(user);
                            } else {
                                Log.w("TAG", "signInAnonymously:failure", task.getException());
                                Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Log.w("TAG", "LOGIN REALIZADO");
            Toast.makeText(this, "Authentication success. ${user.uid}",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void eliminarDatosAcceso() {
        SharedPreferences.Editor editor = this.getSharedPreferences("acceso", MODE_PRIVATE).edit();
        editor.putString("email", null);
        editor.putString("pwd", null);
        editor.apply();
    }

    private void guardarDatosAcceso(String email, String pwd) {
        SharedPreferences.Editor editor = this.getSharedPreferences("acceso", MODE_PRIVATE).edit();
        editor.putString("email", email);
        editor.putString("pwd", pwd);
        editor.apply();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void iniciarSesion(View view) {
        //createXML();
        encryp();
    }

    private void encryp() {
        AesBase64Wrapper aes = new AesBase64Wrapper();
        Log.d("Mensaje Encriptado", aes.encryptAndEncode("9999"));
    }

    private void createXML() {
        ArrayList<DataElements> listFields = new ArrayList<>();
        listFields.add(new DataElements(Tools.NameFields.bitmap.toString(), "C000010810A0004C"));
        listFields.add(new DataElements(Tools.NameFields.message_code.toString(), "0800"));
        listFields.add(new DataElements(Tools.NameFields.transaction_code.toString(), "930002"));
        listFields.add(new DataElements(Tools.NameFields.adquirer_date_time.toString(), "2020-11-16 19:10:14"));
        listFields.add(new DataElements(Tools.NameFields.adquirer_sequence.toString(), "1"));
        listFields.add(new DataElements(Tools.NameFields.terminal_id.toString(), "TPOS-1002-000070"));
        listFields.add(new DataElements(Tools.NameFields.channel_id.toString(), "2"));
        listFields.add(new DataElements(Tools.NameFields.service_code.toString(), "0030011001"));
        listFields.add(new DataElements(Tools.NameFields.product_id.toString(), "012000"));
        listFields.add(new DataElements(Tools.NameFields.user_id.toString(), "Fc2fQTnf8eHbo/tfCfLvKHJpwaFx21TnUEo/1SEgqbE="));
        listFields.add(new DataElements(Tools.NameFields.password.toString(), "8iIKhUs7zGEptJJ31ZK91A=="));
        String xmlRequestLogon = ToolsXML.createXML("request_logon", listFields);
        sendTrx(xmlRequestLogon);
    }

    private void sendTrx(String xmlRequestLogon) {
        SegundoPlano tarea = new SegundoPlano(xmlRequestLogon);
        tarea.execute();
    }

    private class SegundoPlano extends AsyncTask<Void, Void, Void> {

        String xmlSend;

        public SegundoPlano(String xml) {
            this.xmlSend = xml;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            sendRequest(xmlSend);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d("RESPUESTA",  resultString.toString());
            procesarRespuesta(resultString.toString());
        }
    }

    public void procesarRespuesta(String response) {

    }

    private void sendRequest(String xmlSend) {
        String SOAP_ACTION = "http://tempuri.org/iServiceAsynchronous/Invoque_Async_Service";
        String METHOD_NAME = "Invoque_Async_Service";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://190.216.106.14:1992/AsynchronousService.svc";

        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("XmlRequest", xmlSend);
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
