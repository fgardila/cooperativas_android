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

import com.code93.linkcoop.AesBase64Wrapper;
import com.code93.linkcoop.DataElements;
import com.code93.linkcoop.FieldsTrx;
import com.code93.linkcoop.MyApp;
import com.code93.linkcoop.R;
import com.code93.linkcoop.TokenData;
import com.code93.linkcoop.Tools;
import com.code93.linkcoop.ToolsXML;
import com.code93.linkcoop.cache.SP2;
import com.code93.linkcoop.xmlParsers.XmlParser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dmax.dialog.SpotsDialog;

public class Login extends AppCompatActivity {

    private TextInputEditText etEmail;
    private TextInputEditText etPassword;
    private MaterialCheckBox checkbox;
    private TextInputLayout tilEmail;
    private TextInputLayout tilPassword;

    private AlertDialog spotDialog;
    private FirebaseAuth auth;
    private SoapPrimitive resultString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        spotDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Procesando")
                .build();

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        checkbox = findViewById(R.id.checkbox);

        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);

        ArrayList<String> datos = sharedPreferences();
        if (datos.get(0) != null) {
            etEmail.setText(datos.get(0));
            etPassword.setText(datos.get(1));
            checkbox.setChecked(true);
        }

        SP2 sp2 = SP2.Companion.getInstance(this);
        boolean loginStatus = sp2.getBoolean(SP2.Companion.getSP_LOGIN(), false);
        if (loginStatus) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        ImageView imgConnectCoop = findViewById(R.id.imgConnectCoop);
        imgConnectCoop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, MainActivity.class));
                //Tools.showDialogError(Login.this, sp2.getString(SP2.Companion.getSP_LOGIN(), "NULL"));
            }
        });
    }

    /**
     * Encriptar usuario y contraseña.
     * Crear XML
     * Enviar XML
     * Procesar respuesta
     */
    public void iniciarSesion(View view) {
        spotDialog.show();
        boolean isCompleteData = true;

        String user = etEmail.getText().toString();
        String pwd = etPassword.getText().toString();

        if (user.isEmpty()) {
            isCompleteData = false;
            tilEmail.setError("Campo requerido");
        }

        if (pwd.isEmpty()) {
            isCompleteData = false;
            tilPassword.setError("Campo requerido");
        }

        if (isCompleteData) {
            if (checkbox.isChecked()) {
                guardarDatosAcceso(user, pwd);
            } else {
                eliminarDatosAcceso();
            }
            tilEmail.setError("");
            tilPassword.setError("");
            tilEmail.setErrorEnabled(false);
            tilPassword.setErrorEnabled(false);
            AesBase64Wrapper aes = new AesBase64Wrapper();

            String encrypUser = aes.encryptAndEncode(user);
            String encrypPwd = aes.encryptAndEncode(pwd);
            //createXML(encrypUser, encrypPwd);
            logoff(encrypUser, encrypPwd);
        } else {
            spotDialog.dismiss();
            Tools.showDialogError(this, "Complete todos los campos requeridos");
        }

        //MyApp.sp2.incTraceNo();

        //encryp();
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

    private void encryp() {
        AesBase64Wrapper aes = new AesBase64Wrapper();
        Log.d("Mensaje Encriptado", aes.encryptAndEncode("9999"));
    }

    private void createXML(String user, String pwd) {
        ArrayList<DataElements> listFields = new ArrayList<>();
        listFields.add(new DataElements(Tools.NameFields.bitmap.toString(), "C000010810A0004C"));
        listFields.add(new DataElements(Tools.NameFields.message_code.toString(), "0800"));
        listFields.add(new DataElements(Tools.NameFields.transaction_code.toString(), "930002"));
        listFields.add(new DataElements(Tools.NameFields.adquirer_date_time.toString(), Tools.getLocalDateTime()));
        listFields.add(new DataElements(Tools.NameFields.adquirer_sequence.toString(), MyApp.sp2.getTraceNo()));
        listFields.add(new DataElements(Tools.NameFields.terminal_id.toString(), "TPOS-1002-000070"));
        listFields.add(new DataElements(Tools.NameFields.channel_id.toString(), "2"));
        listFields.add(new DataElements(Tools.NameFields.service_code.toString(), "0030011001"));
        listFields.add(new DataElements(Tools.NameFields.product_id.toString(), "012000"));
        listFields.add(new DataElements(Tools.NameFields.user_id.toString(), user));
        listFields.add(new DataElements(Tools.NameFields.password.toString(), pwd));
        String xmlRequestLogon = ToolsXML.createXML("request_logon", listFields);
        sendTrx(xmlRequestLogon);
    }

    private void logoff(String user, String pwd) {
        ArrayList<DataElements> listFields = new ArrayList<>();
        listFields.add(new DataElements(Tools.NameFields.bitmap.toString(), "C000010800A00048"));
        listFields.add(new DataElements(Tools.NameFields.message_code.toString(), "0800"));
        listFields.add(new DataElements(Tools.NameFields.transaction_code.toString(), "930003"));
        listFields.add(new DataElements(Tools.NameFields.adquirer_date_time.toString(), Tools.getLocalDateTime()));
        listFields.add(new DataElements(Tools.NameFields.adquirer_sequence.toString(), MyApp.sp2.getTraceNo()));
        listFields.add(new DataElements(Tools.NameFields.channel_id.toString(), "2"));
        listFields.add(new DataElements(Tools.NameFields.service_code.toString(), "0030011001"));
        listFields.add(new DataElements(Tools.NameFields.product_id.toString(), "012000"));
        listFields.add(new DataElements(Tools.NameFields.user_id.toString(), user));
        String xmlRequestLogon = ToolsXML.createXML("request_logoff", listFields);
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
            MyApp.sp2.incTraceNo();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d("RESPUESTA",  resultString.toString());
            procesarRespuesta(resultString.toString());
        }
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
        spotDialog.dismiss();
        for (TokenData tokenData : listTokens) {
            if (tokenData.getIdToken().equals("B1")){
                Tools.showDialogError(this, tokenData.getDataToken());
                break;
            }
        }
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
