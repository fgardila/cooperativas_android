package com.code93.linkcoop.ui

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.checkbox.MaterialCheckBox
import android.widget.TextView
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import android.os.Bundle
import com.code93.linkcoop.R
import dmax.dialog.SpotsDialog
import android.os.Build
import android.content.SharedPreferences
import com.code93.linkcoop.models.Usuario
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import android.widget.Toast
import android.content.Intent
import com.code93.linkcoop.ui.MainActivity
import android.text.Editable
import android.util.Log
import android.view.View
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject
import com.google.gson.Gson
import com.code93.linkcoop.models.DataUsuarios
import com.code93.linkcoop.Tools
import com.android.volley.VolleyError
import kotlin.Throws
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.code93.linkcoop.volley.VolleyApplication
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*

class LoginActivity : AppCompatActivity() {

    var dialog: AlertDialog? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        dialog = SpotsDialog.Builder().setContext(this).build()
        tvForgotPassword.setOnClickListener {

        }
        val datos = sharedPreferences
        if (datos[0] != null) {
            etEmail.setText(datos[0])
            etPassword.setText(datos[1])
            checkbox.setChecked(true)
        }
        val model = Build.MODEL
        val device = Build.DEVICE
        val product = Build.PRODUCT
        val manufacturer = Build.MANUFACTURER
        val uniqueID: String = UUID.randomUUID().toString()

        val msg = "MODEL = " + model + "\n" +
                "DEVICE = " + device + "\n" +
                "PRODUCT = " + product + "\n" +
                "id = " + uniqueID + "\n" +
                "MANUFACTURER = " + manufacturer
        Tools.showDialogError(this, msg);
    }

    private val sharedPreferences: Array<String?>
        get() {
            val datos = arrayOfNulls<String>(2)
            val prefs = applicationContext.getSharedPreferences("acceso", MODE_PRIVATE)
            datos[0] = prefs.getString("email", null)
            datos[1] = prefs.getString("pwd", null)
            return datos
        }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user == null) {
            auth.signInAnonymously()
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Log.d("TAG", "signInAnonymously:success")
                            val user = auth.currentUser
                            updateUI(user)
                        } else {
                            Log.w("TAG", "signInAnonymously:failure", task.exception)
                            Toast.makeText(baseContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show()
                            //updateUI(null)
                        }
                    }
        } else {
            Toast.makeText(baseContext, "Authentication success. ${user.uid}",
                    Toast.LENGTH_SHORT).show()
        }
    }

    private fun inicarSesion(email: String, password: String) {

    }

    private fun validarUsuario(usuarios: ArrayList<Usuario?>, email: String, password: String) {

    }

    fun iniciarSesion(view: View?) {
        if (view != null) {
            var ret = true
            val email = Objects.requireNonNull(etEmail!!.text).toString()
            val password = Objects.requireNonNull(etPassword!!.text).toString()
            if (email.isEmpty()) {
                ret = false
                tilEmail!!.error = "Complete la informacion de este campo"
            }
            if (password.isEmpty()) {
                ret = false
                tilPassword!!.error = "Complete la informacion de este campo"
            }
            if (ret) inicarSesion(email, password)
        }
    }

    private fun eliminarDatos() {
        val editor = applicationContext.getSharedPreferences("acceso", MODE_PRIVATE).edit()
        editor.putString("email", null)
        editor.putString("pwd", null)
        editor.apply()
    }

    private fun guardarSharedPreferences(email: String, pwd: String) {
        val editor = applicationContext.getSharedPreferences("acceso", MODE_PRIVATE).edit()
        editor.putString("email", email)
        editor.putString("pwd", pwd)
        editor.apply()
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }

    private fun onLoginFailed(error: String) {
        Toast.makeText(this, "" + error, Toast.LENGTH_SHORT).show()
    }

    fun writeXmlFile() {

    }
}