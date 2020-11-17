package com.code93.linkcoop.ui

import android.app.AlertDialog
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.code93.linkcoop.DataElements
import com.code93.linkcoop.R
import com.code93.linkcoop.Tools
import com.code93.linkcoop.ToolsXML
import com.code93.linkcoop.models.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_login.*
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.SoapObject
import org.ksoap2.serialization.SoapPrimitive
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpTransportSE
import java.util.*

class LoginActivity : AppCompatActivity() {

    var dialog: AlertDialog? = null
    private lateinit var auth: FirebaseAuth
    lateinit var resultString : SoapPrimitive

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
        //Tools.showDialogError(this, msg);
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
            Log.w("TAG", "LOGIN REALIZADO")
            Toast.makeText(baseContext, "Authentication success. ${user.uid}",
                    Toast.LENGTH_SHORT).show()
        }
    }

    private fun validarUsuario(usuarios: ArrayList<Usuario?>, email: String, password: String) {

    }

    fun iniciarSesion(view: View?) {
        /*if (view != null) {
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
            createXML()
        }*/
        //createXML()

        sendRequest()


    }
    private fun sendRequest() {
        val SOAP_ACTION = "http://tempuri.org/iServiceAsynchronous/Get_Status_Service"
        val METHOD_NAME = "Get_Status_Service"
        val NAMESPACE = "http://tempuri.org/"
        val URL = "http://190.216.106.14:1992/AsynchronousService.svc"

        try {
            val Request : SoapObject = SoapObject(NAMESPACE, METHOD_NAME)
            Request.addProperty("Code", "0")
            val soapEnvelope = SoapSerializationEnvelope(SoapEnvelope.VER11)
            soapEnvelope.dotNet = true
            soapEnvelope.setOutputSoapObject(Request)
            val transport = HttpTransportSE(URL)
            transport.call(SOAP_ACTION, soapEnvelope)
            resultString = soapEnvelope.response as SoapPrimitive
        }catch (e: Exception) {

        }
    }

    private fun createXML() {
        val listFields = mutableListOf<DataElements>()
        listFields.add(DataElements(Tools.NameFields.bitmap.toString(), "C000010810A0004C"))
        listFields.add(DataElements(Tools.NameFields.message_code.toString(), "0800"))
        listFields.add(DataElements(Tools.NameFields.transaction_code.toString(), "930002"))
        listFields.add(DataElements(Tools.NameFields.adquirer_date_time.toString(), "2020-11-03 19:10:14"))
        listFields.add(DataElements(Tools.NameFields.adquirer_sequence.toString(), "1"))
        listFields.add(DataElements(Tools.NameFields.terminal_id.toString(), "TPOS-1002-000070"))
        listFields.add(DataElements(Tools.NameFields.channel_id.toString(), "2"))
        listFields.add(DataElements(Tools.NameFields.service_code.toString(), "0030011001"))
        listFields.add(DataElements(Tools.NameFields.product_id.toString(), "012000"))
        listFields.add(DataElements(Tools.NameFields.user_id.toString(), "Fc2fQTnf8eHbo/tfCfLvKHJpwaFx21TnUEo/1SEgqbE="))
        listFields.add(DataElements(Tools.NameFields.password.toString(), "8iIKhUs7zGEptJJ31ZK91A=="))
        val xmlRequestLogon = ToolsXML.createXML("request_logon", listFields)
        sendTrx(xmlRequestLogon)
    }

    private fun sendTrx(xmlRequestLogon: String) {
        val queue = Volley.newRequestQueue(this)
        val url = "http://190.216.106.14:1992/AsynchronousService.svc?wsdl"

        val stringRequest = object : StringRequest(Method.POST, url,
                Response.Listener { response ->
                    Log.e(
                            "HttpClient", "success! response: $response"
                    )
                },
                Response.ErrorListener { error ->
                    Log.e(
                            "HttpClient", "error: $error"
                    )
                }) {
            override fun getBodyContentType(): String {
                return xmlRequestLogon
            }
        }
        queue.add(stringRequest)
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
}