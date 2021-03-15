package com.code93.linkcoop.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.code93.linkcoop.AesBase64Wrapper;
import com.code93.linkcoop.BuildConfig;
import com.code93.linkcoop.models.FieldsTrx;
import com.code93.linkcoop.MyApp;
import com.code93.linkcoop.R;
import com.code93.linkcoop.TokenData;
import com.code93.linkcoop.Tools;
import com.code93.linkcoop.ToolsXML;
import com.code93.linkcoop.cache.SP2;
import com.code93.linkcoop.models.Instituciones;
import com.code93.linkcoop.models.LoginCooperativas;
import com.code93.linkcoop.network.DownloadCallback;
import com.code93.linkcoop.network.DownloadXmlTask;
import com.code93.linkcoop.viewmodel.InstitucionesViewModel;
import com.code93.linkcoop.xmlParsers.XmlParser;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.util.ArrayList;
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

    private InstitucionesViewModel viewModel;
    private SP2 sp2;

    private String userE;
    private String pwdE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        viewModel = new ViewModelProvider(this).get(InstitucionesViewModel.class);
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

        TextView tvVersion = findViewById(R.id.tvVersion);
        String version = BuildConfig.VERSION_NAME;
        tvVersion.setText(version);

        TextView tvSerial = findViewById(R.id.tvSerial);
        String serial = "No tiene serial";
        tvSerial.setText(serial);

        TextView tvGoogle = findViewById(R.id.tvGoogle);
        String google = "No tiene google";
        tvGoogle.setText(google);
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
                Tools.showDialogError(Login.this, error);
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
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    private void updateUI(FirebaseUser user) {
        if (user == null) {
            auth.signInAnonymously()
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "signInAnonymously:success");
                            FirebaseUser newUser = auth.getCurrentUser();
                            updateUI(newUser);
                        } else {
                            Log.w("TAG", "signInAnonymously:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
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
        Log.d("Login.java", "Procesar respuesta");
        try {
            FieldsTrx fieldsTrx = XmlParser.parse(response, "reply_logon");
            Log.d("FieldTRX", Objects.requireNonNull(fieldsTrx.getToken_data()));
            TokenData tokenData = new TokenData();
            tokenData.getTokens(Objects.requireNonNull(fieldsTrx.getToken_data()));
            if (fieldsTrx.getResponse_code().equals("00")) {
                MyApp.sp2.putBoolean(SP2.Companion.getSP_LOGIN(), true);
                Gson gson = new Gson();
                String jsson = "{\"comercio\":{\"nombre\":\"DESARROLLO FABIAN ARDILA\",\"ruc\":\"1306396456   \",\"direccion\":\"EL GUABO\"},\"instituciones\":[{\"_id\":1002,\"_namec\":\"COOPERATIVA MIFEX\",\"_product\":\"012001\",\"_service\":\"0030011001\",\"_channel\":2,\"url_imagen\":\"http://190.216.106.14:1992/Img/img_1002.png\",\"_transaction\":[{\"_bitmap\":\"E210010810A050C0\",\"_code\":\"301000\",\"_cost\":\"0.25\",\"_message_code\":\"0200\",\"_namet\":\"RETIRO AHORROS\",\"_tag_request\":\"request_withdrawal\",\"_tag_reply\":\"reply_withdrawal\",\"_subservice\":\"0\",\"referencias\":[{\"buss_type\":\"xml_element\",\"data_type\":\"amount\",\"description\":\"Monto\",\"identificator\":\"transaction_amount\",\"mode\":\"input\",\"value\":\"\"},{\"buss_type\":\"xml_element\",\"data_type\":\"numerico\",\"description\":\"Numero de Cuenta\",\"identificator\":\"reference\",\"mode\":\"input\",\"value\":\"\"},{\"buss_type\":\"token\",\"data_type\":\"numerico\",\"description\":\"OTP Generado\",\"identificator\":\"B8\",\"mode\":\"input\",\"value\":\"\"},{\"buss_type\":\"token\",\"data_type\":\"numerico\",\"description\":\"Documento de identidad\",\"identificator\":\"X4\",\"mode\":\"input\",\"value\":\"\"}]},{\"_bitmap\":\"E200010810A050C0\",\"_code\":\"306000\",\"_cost\":\"0.00\",\"_namet\":\"GENERACION OTP\",\"_message_code\":\"0200\",\"_subservice\":\"1\",\"referencias\":[{\"buss_type\":\"xml_element\",\"data_type\":\"numerico\",\"description\":\"Numero de Cuenta\",\"identificator\":\"referencia\",\"mode\":\"input\",\"value\":\"\"},{\"buss_type\":\"token\",\"data_type\":\"numerico\",\"description\":\"Nombre del Cliente\",\"identificator\":\"X4\",\"mode\":\"input\",\"value\":\"\"}]},{\"_bitmap\":\"E200010810A050C0\",\"_code\":\"306000\",\"_cost\":\"0.00\",\"_namet\":\"GENERACION OTP\",\"_message_code\":\"0200\",\"_subservice\":\"1\",\"referencias\":[{\"buss_type\":\"xml_element\",\"data_type\":\"numerico\",\"description\":\"Numero de Cuenta\",\"identificator\":\"referencia\",\"mode\":\"input\",\"value\":\"\"},{\"buss_type\":\"token\",\"data_type\":\"numerico\",\"description\":\"Nombre del Cliente\",\"identificator\":\"X4\",\"mode\":\"input\",\"value\":\"\"}]}]},{\"_id\":1007,\"_namec\":\"COOPERATIVA COOPROGRESO\",\"_product\":\"999999\",\"_service\":\"0030011001\",\"_channel\":2,\"url_imagen\":\"http://190.216.106.14:1992/Img/img_1007.png\",\"_transaction\":[{\"_bitmap\":\"E200010810A050C0\",\"_code\":\"306000\",\"_cost\":\"0.00\",\"_namet\":\"GENERACION OTP\",\"_message_code\":\"0200\",\"_subservice\":\"1\",\"referencias\":[{\"buss_type\":\"xml_element\",\"data_type\":\"numerico\",\"description\":\"Numero de Cuenta\",\"identificator\":\"referencia\",\"mode\":\"input\",\"value\":\"\"},{\"buss_type\":\"token\",\"data_type\":\"numerico\",\"description\":\"Nombre del Cliente\",\"identificator\":\"X4\",\"mode\":\"input\",\"value\":\"\"}]},{\"_bitmap\":\"E200010810A050C0\",\"_code\":\"306000\",\"_cost\":\"0.00\",\"_namet\":\"GENERACION OTP\",\"_message_code\":\"0200\",\"_subservice\":\"1\",\"referencias\":[{\"buss_type\":\"xml_element\",\"data_type\":\"numerico\",\"description\":\"Numero de Cuenta\",\"identificator\":\"referencia\",\"mode\":\"input\",\"value\":\"\"},{\"buss_type\":\"token\",\"data_type\":\"numerico\",\"description\":\"Nombre del Cliente\",\"identificator\":\"X4\",\"mode\":\"input\",\"value\":\"\"}]}]},{\"_id\":1008,\"_namec\":\"COOPERATIVA CASMEC\",\"_product\":\"999999\",\"_service\":\"0030011001\",\"_channel\":2,\"url_imagen\":\"http://190.216.106.14:1992/Img/img_1003.png\",\"_transaction\":[{\"_bitmap\":\"E200010810A050C0\",\"_code\":\"306000\",\"_cost\":\"0.00\",\"_namet\":\"GENERACION OTP\",\"_message_code\":\"0200\",\"_subservice\":\"1\",\"referencias\":[{\"buss_type\":\"xml_element\",\"data_type\":\"numerico\",\"description\":\"Numero de Cuenta\",\"identificator\":\"referencia\",\"mode\":\"input\",\"value\":\"\"},{\"buss_type\":\"token\",\"data_type\":\"numerico\",\"description\":\"Nombre del Cliente\",\"identificator\":\"X4\",\"mode\":\"input\",\"value\":\"\"}]}]}]}";
                LoginCooperativas logCoop = gson.fromJson(jsson, LoginCooperativas.class);
                MyApp.sp2.putString(SP2.Companion.getComercio_nombre(), logCoop.getComercio().getNombre().trim());
                MyApp.sp2.putString(SP2.Companion.getComercio_ruc(), logCoop.getComercio().getRuc().trim());
                MyApp.sp2.putString(SP2.Companion.getComercio_direccion(), logCoop.getComercio().getDireccion().trim());
                viewModel.deleteAllInstituciones();
                for (Instituciones inst : logCoop.getInstituciones()) {
                    viewModel.addInstituciones(inst);
                }

                spotDialog.dismiss();
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
