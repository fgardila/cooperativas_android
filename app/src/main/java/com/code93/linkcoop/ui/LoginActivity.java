package com.code93.linkcoop.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.code93.linkcoop.AesBase64Wrapper;
import com.code93.linkcoop.BuildConfig;
import com.code93.linkcoop.ToolsZ90;
import com.code93.linkcoop.persistence.models.FieldsTrx;
import com.code93.linkcoop.MyApp;
import com.code93.linkcoop.R;
import com.code93.linkcoop.TokenData;
import com.code93.linkcoop.Tools;
import com.code93.linkcoop.ToolsXML;
import com.code93.linkcoop.persistence.cache.SP2;
import com.code93.linkcoop.persistence.models.Cooperativa;
import com.code93.linkcoop.persistence.models.LoginCooperativas;
import com.code93.linkcoop.network.DownloadCallback;
import com.code93.linkcoop.network.DownloadXmlTask;
import com.code93.linkcoop.view.HomeActivity;
import com.code93.linkcoop.viewmodel.CooperativaViewModel;
import com.code93.linkcoop.xmlParsers.XmlParser;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.Gson;
import com.zcs.sdk.SdkResult;
import com.zcs.sdk.Sys;

import org.jetbrains.annotations.NotNull;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import dmax.dialog.SpotsDialog;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etEmail;
    private TextInputEditText etPassword;
    private MaterialCheckBox checkbox;
    private TextInputLayout tilEmail;
    private TextInputLayout tilPassword;

    private AlertDialog spotDialog;
    private FirebaseAuth auth;

    private CooperativaViewModel viewModel;
    private SP2 sp2;

    private String userE;
    private String pwdE;

    private TextView tvGoogle;

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
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        });

        TextView tvVersion = findViewById(R.id.tvVersion);
        String version = BuildConfig.VERSION_NAME;
        tvVersion.setText("Versión: " + version);

        tvGoogle = findViewById(R.id.tvGoogle);

        TextView tvSerial = findViewById(R.id.tvSerial);
        if (Build.MODEL.contains("Z90")) {
            tvSerial.setText("Serial: " + getSn());
        } else {
            tvSerial.setText("Serial: No soporta");
        }

    }

    /**
     * Encriptar usuario y contraseña.
     * Crear XML
     * Enviar XML
     * Procesar respuesta
     */
    public void iniciarSesion(View view) {
        realizarInicioDeSesion();
    }

    private void realizarInicioDeSesion() {
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
            new Thread(this::encriptarUsuario).start();

        } else {
            spotDialog.dismiss();
            Tools.showDialogError(this, "Complete todos los campos requeridos");
        }
    }

    private void encriptarUsuario() {
        String user = etEmail.getText().toString();
        AesBase64Wrapper aes = new AesBase64Wrapper();
        aes.encryptAndEncode(user, this, new DownloadCallback() {
            @Override
            public void onDownloadCallback(@NotNull String response) {
                userE = response;
                Log.d("UserEncrip: ", response);
                if (response.contains("Error")) {
                    showError(response);
                } else {
                    encriptarContrasena();
                }
            }
        });
    }

    private void encriptarContrasena() {
        String pwd = etPassword.getText().toString();
        AesBase64Wrapper aes = new AesBase64Wrapper();
        aes.encryptAndEncode(pwd, this, new DownloadCallback() {
            @Override
            public void onDownloadCallback(@NotNull String response) {
                pwdE = response;
                Log.d("pwdE: ", response);
                MyApp.sp2.putString(SP2.Companion.getUser_encript(), userE);
                sendLogin(userE, pwdE);
            }
        });
    }

    private void sendLogin(String encrypUser, String encrypPwd) {
        String xmlLogOff = ToolsXML.requestLogon(encrypUser, encrypPwd);

        DownloadXmlTask task = new DownloadXmlTask(xmlLogOff, response -> {
            if (response.equals("Error de conexion"))
                showError("Error de conexion");
            else
                procesarRespuesta(response);
        });
        task.execute(xmlLogOff);
    }

    private void showError(String error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                spotDialog.dismiss();
                Tools.showDialogError(LoginActivity.this, error);
            }
        });
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

        if (!Tools.isNetworkAvailable(this)) {
            Tools.showDialogError(this, "No tiene conexion a internet");
        }

        FirebaseUser currentUser = auth.getCurrentUser();
        updateUI(currentUser);

        boolean loginStatus = sp2.getBoolean(SP2.Companion.getSP_LOGIN(), false);
        if (loginStatus) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }
    }

    private void updateUI(FirebaseUser user) {
        if (user == null) {
            // Sign Anonymously
            auth.signInAnonymously()
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "signInAnonymously:success");
                            FirebaseUser newUser = auth.getCurrentUser();
                            updateUI(newUser);
                        } else {
                            Log.w("TAG", "signInAnonymously:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            String googleText = "Google UID: " + user.getUid();
            tvGoogle.setText(googleText);
            if (ToolsZ90.isZ90()) {
                FirebaseCrashlytics.getInstance().setUserId(ToolsZ90.getSn(this));
            } else {
                FirebaseCrashlytics.getInstance().setUserId(user.getUid());
            }
            Log.w("TAG", "LOGIN REALIZADO");
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void procesarRespuesta(String response) {
        Log.d("Login.java", "Procesar respuesta");
        try {
            FieldsTrx fieldsTrx = XmlParser.parse(response, "reply_logon");
            Log.d("FieldTRX", Objects.requireNonNull(fieldsTrx.getToken_data()));
            TokenData tokenData = new TokenData();
            tokenData.getTokens(Objects.requireNonNull(fieldsTrx.getToken_data()));
            if (fieldsTrx.getResponse_code().equals("00")) {
                viewModel.deleteAllCooperativas();
                MyApp.sp2.putBoolean(SP2.Companion.getSP_LOGIN(), true);
                MyApp.sp2.putString(SP2.Companion.getFechaUltimoLogin(), Tools.getLocalDateTime());
                Gson gson = new Gson();
                LoginCooperativas logCoop = gson.fromJson(fieldsTrx.getBuffer_data(), LoginCooperativas.class);
                MyApp.sp2.putString(SP2.Companion.getComercio_nombre(), logCoop.getComercio().getNombre().trim());
                MyApp.sp2.putString(SP2.Companion.getComercio_ruc(), logCoop.getComercio().getRuc().trim());
                MyApp.sp2.putString(SP2.Companion.getComercio_direccion(), logCoop.getComercio().getDireccion().trim());
                for (Cooperativa coop : logCoop.getCooperativas()) {
                    viewModel.addCooperativa(coop);
                }

                spotDialog.dismiss();
                Tools.showDialogPositive(this, tokenData.getB1(), value -> {
                    FirebaseCrashlytics.getInstance().log("Inicio de sesion exitoso " + etEmail.getText().toString());
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();
                });

            } else {
                if (fieldsTrx.getResponse_code().equals("12")) {
                    cerrarSesion();
                } else {
                    spotDialog.dismiss();
                    Tools.showDialogError(this, tokenData.getB1());
                }
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
    }

    public void cerrarSesion() {
        String user_encript = MyApp.sp2.getString(SP2.Companion.getUser_encript(), "");
        String xmlLogOff = ToolsXML.requestLogoff(user_encript);
        DownloadXmlTask task = new DownloadXmlTask(xmlLogOff, response -> {
            if (response.equals("Error de conexion")) {
                showError("Error de conexion");
            } else {
                procesarRespuestaLogOff(response);
            }
        });
        task.execute(xmlLogOff);
    }

    private void procesarRespuestaLogOff(String response) {
        try {
            FieldsTrx fieldsTrx = XmlParser.parse(response, "reply_logoff");
            Log.d("FieldTRX", Objects.requireNonNull(fieldsTrx.getToken_data()));
            TokenData tokenData = new TokenData();
            tokenData.getTokens(Objects.requireNonNull(fieldsTrx.getToken_data()));
            if (fieldsTrx.getResponse_code().equals("00")) {
                MyApp.sp2.putBoolean(SP2.Companion.getSP_LOGIN(), false);
                spotDialog.dismiss();
                Tools.showDialogPositive(this, tokenData.getB1(), value -> realizarInicioDeSesion());
            } else {
                spotDialog.dismiss();
                Tools.showDialogError(this, tokenData.getB1());
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
    }

    private String getSn() {
        // Config the SDK base info
        Sys sys = MyApp.sDriverManager.getBaseSysDevice();
        String[] pid = new String[1];
        int status = sys.getPid(pid);
        int count = 0;
        while (status != SdkResult.SDK_OK && count < 3) {
            count++;
            int sysPowerOn = sys.sysPowerOn();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            final int i = sys.sdkInit();
        }
        return pid[0];
    }
}
