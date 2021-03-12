package com.code93.linkcoop.ui

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.code93.linkcoop.R
import com.code93.linkcoop.adapters.ReportesAdapter
import com.code93.linkcoop.models.LogTransacciones
import com.code93.linkcoop.viewmodel.LogTransaccionesViewModel

class ReportesActivity : AppCompatActivity() {

    private lateinit var logData: LogTransaccionesViewModel
    private lateinit var rvReportes : RecyclerView
    private lateinit var reportesAdapter: ReportesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reportes)

        rvReportes = findViewById(R.id.rvReportes)

        reportesAdapter = ReportesAdapter(this)

        logData = ViewModelProvider(this).get(LogTransaccionesViewModel::class.java)

        logData.readAllData.observe(this, Observer { logs ->
            cargarData(logs)
        })

    }

    private fun cargarData(logs: List<LogTransacciones>) {
        /*for (trx in logs) {
            when (trx.transaction.nameTransaction!!.trim { it <= ' ' }) {
                "RETIRO AHORROS" -> retiroAhorros(trx)
                "CONSULTA DE SALDOS" -> consultaSaldo(trx)
                "GENERACION OTP" -> generacionOtp(trx)
                "DEPOSITO AHORROS" -> depositoAhorros(trx)
                else -> {
                    Toast.makeText(this, "Transaccion no listada " + trx.transaction.nameTransaction, Toast.LENGTH_SHORT).show()
                }a
            }
        }*/
        if (logs.isEmpty()) {
            val noItems = findViewById<RelativeLayout>(R.id.noItems)
            noItems.visibility = View.VISIBLE
        } else {
            reportesAdapter.logs = logs
            rvReportes.setHasFixedSize(true)
            rvReportes.layoutManager = LinearLayoutManager(this)
            rvReportes.adapter = reportesAdapter
            reportesAdapter.notifyDataSetChanged()
        }

    }

    private fun depositoAhorros(trx: LogTransacciones) {
    }

    private fun generacionOtp(trx: LogTransacciones) {

    }

    private fun consultaSaldo(trx: LogTransacciones) {

    }

    private fun retiroAhorros(trx: LogTransacciones) {


    }


}