package com.code93.linkcoop.view

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.code93.linkcoop.*
import com.code93.linkcoop.core.DialogCallback
import com.code93.linkcoop.core.Tools.getLocalDateTime
import com.code93.linkcoop.core.Tools.showDialogErrorCallback
import com.code93.linkcoop.core.Tools.showDialogPositive
import com.code93.linkcoop.databinding.ActivityHomeBinding
import com.code93.linkcoop.network.DownloadCallback
import com.code93.linkcoop.network.DownloadXmlTask
import com.code93.linkcoop.persistence.cache.SP2.Companion.SP_LOGIN
import com.code93.linkcoop.persistence.cache.SP2.Companion.fechaUltimoLogin
import com.code93.linkcoop.persistence.cache.SP2.Companion.getInstance
import com.code93.linkcoop.persistence.cache.SP2.Companion.user_encript
import com.code93.linkcoop.ui.login.LoginActivity
import com.code93.linkcoop.xmlParsers.XmlParser.parse
import dmax.dialog.SpotsDialog
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class HomeActivity : AppCompatActivity(), DownloadCallback {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var navController : NavController
    private lateinit var spotDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        spotDialog = SpotsDialog.Builder()
            .setContext(this)
            .setCancelable(false)
            .setMessage("Conectando...")
            .build()

        val bottomNavigationView = binding.bottomNavigationView
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        bottomNavigationView.setupWithNavController(navController)
    }

    override fun onStart() {
        super.onStart()
        verificarFechaUltimoLogin()
    }

    override fun onResume() {
        super.onResume()
        verificarFechaUltimoLogin()
    }

    private fun verificarFechaUltimoLogin() {
        var day: Long = 0
        var diff: Long = 0
        val fechaUltimoLogin = getInstance(this)!!
            .getString(fechaUltimoLogin, "NO")
        if (fechaUltimoLogin != "NO") {
            val fechaLogin = fechaUltimoLogin!!.substring(0, fechaUltimoLogin.indexOf(" "))
            Log.d("FECHA ULTIMA", fechaLogin)
            val outputPattern = "yyyy-MM-dd HH:mm:ss"
            val outputFormat = SimpleDateFormat(outputPattern)
            try {
                val date1 = outputFormat.parse(fechaUltimoLogin)
                val date2 = outputFormat.parse(getLocalDateTime())
                diff = date2.time - date1.time
                day = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
                Log.d("DIAS DE ULTIMO LOGIN", "" + day)
                if (day >= 2) {
                    //cerrarSesion();
                    solicitarCerrarSesion()
                }
            } catch (e: Exception) {
                //sp2.putBoolean(SP2.Companion.getSP_LOGIN(), false);
            }
        }
    }

    private fun solicitarCerrarSesion() {
        showDialogErrorCallback(
            this,
            "Llevas dos dias sin iniciar sesión. Inicia sesión nuevamente.",
            object : DialogCallback {
                override fun onDialogCallback(value: Int) {
                    cerrarSesion()
                }
            })
    }

    private fun cerrarSesion() {
        spotDialog.show()
        val user_encript = MyApp.sp2!!.getString(user_encript, "")
        val xmlLogOff = ToolsXML.requestLogoff(user_encript)
        val task = DownloadXmlTask(xmlLogOff, this)
        task.execute(xmlLogOff)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onDownloadCallback(response: String) {
        if (response == "Error de conexion") {
            showErrorConexion()
        } else {
            procesarRespuesta(response)
        }
    }

    private fun procesarRespuesta(response: String) {
        try {
            val (_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, response_code, _, _, _, _, _, _, _, _, _, _, _, token_data) = parse(
                response,
                "reply_logoff"
            )
            Log.d("FieldTRX", Objects.requireNonNull(token_data))
            val tokenData = TokenData()
            tokenData.getTokens(Objects.requireNonNull(token_data))
            if (response_code == "00") {
                MyApp.sp2!!.putBoolean(SP_LOGIN, false)
                spotDialog.dismiss()
                showDialogPositive(this, tokenData.B1, object : DialogCallback {
                    override fun onDialogCallback(value: Int) {
                        startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
                        finish()
                    }
                })
            } else {
                spotDialog.dismiss()
                showDialogErrorCallback(this, tokenData.B1, object : DialogCallback {
                    override fun onDialogCallback(value: Int) {
                        MyApp.sp2!!.putBoolean(SP_LOGIN, false)
                        startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
                        finish()
                    }
                })
            }
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun showErrorConexion() {
        spotDialog.dismiss()
        showDialogErrorCallback(this, "Error de conexion", object : DialogCallback {
            override fun onDialogCallback(value: Int) {
                MyApp.sp2!!.putBoolean(SP_LOGIN, false)
                startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
                finish()
            }
        })
    }
}