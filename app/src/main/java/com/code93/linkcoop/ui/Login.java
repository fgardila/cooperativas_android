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
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.code93.linkcoop.AesBase64Wrapper;
import com.code93.linkcoop.DataElements;
import com.code93.linkcoop.DialogCallback;
import com.code93.linkcoop.models.FieldsTrx;
import com.code93.linkcoop.MyApp;
import com.code93.linkcoop.R;
import com.code93.linkcoop.TokenData;
import com.code93.linkcoop.Tools;
import com.code93.linkcoop.ToolsXML;
import com.code93.linkcoop.cache.SP;
import com.code93.linkcoop.cache.SP2;
import com.code93.linkcoop.models.Cooperativa;
import com.code93.linkcoop.models.LoginCooperativas;
import com.code93.linkcoop.network.DownloadCallback;
import com.code93.linkcoop.network.DownloadXmlTask;
import com.code93.linkcoop.viewmodel.CooperativaViewModel;
import com.code93.linkcoop.xmlParsers.XmlParser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
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

    private CooperativaViewModel viewModel;
    private SP2 sp2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        viewModel = new ViewModelProvider(this).get(CooperativaViewModel.class);
        sp2 = SP2.Companion.getInstance(this);

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
        if (datos.get(1) != null) {
            etEmail.setText(datos.get(0));
            etPassword.setText(datos.get(1));
            checkbox.setChecked(true);
        }

        ImageView imgConnectCoop = findViewById(R.id.imgConnectCoop);
        imgConnectCoop.setOnClickListener(v -> {
            startActivity(new Intent(Login.this, MainActivity.class));
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
            sendLogin(encrypUser, encrypPwd);
        } else {
            spotDialog.dismiss();
            Tools.showDialogError(this, "Complete todos los campos requeridos");
        }
    }

    private void sendLogin(String encrypUser, String encrypPwd) {
        String xmlLogOff = ToolsXML.requestLogon(encrypUser, encrypPwd);

        DownloadXmlTask task = new DownloadXmlTask(xmlLogOff, response -> {
            procesarRespuesta(response);
        });
        task.execute(xmlLogOff);
    }

    private ArrayList<String> sharedPreferences() {
        ArrayList<String> data = new ArrayList<>();
        data.add(0, sp2.getString(SP2.Companion.getUser(), ""));
        data.add(1, sp2.getString(SP2.Companion.getPwd(), ""));
        return data;
    }

    private void eliminarDatosAcceso() {
        sp2.putString(SP2.Companion.getPwd(), null);
    }

    private void guardarDatosAcceso(String email, String pwd) {
        sp2.putBoolean(SP2.Companion.getSaveLogin(), true);
        sp2.putString(SP2.Companion.getUser(), email);
        sp2.putString(SP2.Companion.getPwd(), pwd);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        updateUI(currentUser);

        boolean loginStatus = sp2.getBoolean(SP2.Companion.getSP_LOGIN(), false);
        if (loginStatus) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
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

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void procesarRespuesta(String response) {
        try {
            FieldsTrx fieldsTrx = XmlParser.parse(response, "reply_logon");
            Log.d("FieldTRX", Objects.requireNonNull(fieldsTrx.getToken_data()));
            TokenData tokenData = new TokenData();
            tokenData.getTokens(Objects.requireNonNull(fieldsTrx.getToken_data()));
            if (fieldsTrx.getResponse_code().equals("00")) {
                spotDialog.dismiss();
                MyApp.sp2.putBoolean(SP2.Companion.getSP_LOGIN(), true);
                Gson gson = new Gson();
                LoginCooperativas logCoop = gson.fromJson(fieldsTrx.getBuffer_data(), LoginCooperativas.class);
                viewModel.deleteAllCooperativas();
                for (Cooperativa coop : logCoop.getCooperativas()) {
                    viewModel.addCooperativa(coop);
                }

                Tools.showDialogPositive(this, tokenData.getB1(), value -> {
                    startActivity(new Intent(Login.this, MainActivity.class));
                    finish();
                });
            } else {
                spotDialog.dismiss();
                Tools.showDialogError(this, tokenData.getB1());
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
    }
}
