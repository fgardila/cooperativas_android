package com.code93.linkcoop.ui

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.code93.linkcoop.R
import com.code93.linkcoop.Tools
import com.code93.linkcoop.models.LogTransacciones
import com.code93.linkcoop.viewmodel.LogTransaccionesViewModel
import dmax.dialog.SpotsDialog

class MenuReportesActivity : AppCompatActivity() {

    private lateinit var dialog : AlertDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_reportes)

        dialog = SpotsDialog.Builder()
            .setContext(this)
            .setCancelable(false)
            .setMessage("Cargando transacciones")
            .build()

    }

    fun reimprimirUltimoTicket(view: View) {
        dialog.show()
        val logData = ViewModelProvider(this).get(LogTransaccionesViewModel::class.java)
        logData.readAllData.observe(this, Observer { logs ->
            dialog.dismiss()
            cargarData(logs)
        })
    }

    private fun cargarData(logs: List<LogTransacciones>?) {
        val lastTrx = logs!!.size - 1;
        if (lastTrx < 0) {
            Tools.showDialogError(this@MenuReportesActivity, "No se encuentran transacciones")
        } else {
            Log.d("TAG", "Ultima transaccion ${lastTrx}")
            val ultimoLog = logs.get(lastTrx)
            val intent = Intent(this@MenuReportesActivity, ImpresionActivity::class.java)
            intent.putExtra("logTransacciones", ultimoLog)
            startActivity(intent)
            finish()
        }
    }

    fun historialTransacciones(view: View) {
        startActivity(Intent(this, ReportesActivity::class.java))
    }
    fun realizarCierre(view: View) {
        startActivity(Intent(this, CierreActivity::class.java))
    }
}