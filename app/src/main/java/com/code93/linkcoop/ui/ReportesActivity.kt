package com.code93.linkcoop.ui

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.code93.linkcoop.R
import com.code93.linkcoop.Tools.showDialogError
import com.code93.linkcoop.models.LogTransacciones
import com.code93.linkcoop.viewmodel.LogTransaccionesViewModel

class ReportesActivity : AppCompatActivity() {

    private lateinit var logData: LogTransaccionesViewModel
    private lateinit var tvCantDepositos : TextView
    private lateinit var tvMontoDepositos : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reportes)

        val tvTransaccion = findViewById<TextView>(R.id.tvTransaccion)
        tvCantDepositos = findViewById(R.id.tvCantDepositos)
        tvMontoDepositos = findViewById(R.id.tvMontoDepositos)

        logData = ViewModelProvider(this).get(LogTransaccionesViewModel::class.java)

        logData.readAllData.observe(this, Observer { logs ->
            cargarData(logs)
        })

    }

    private fun cargarData(logs: List<LogTransacciones>) {
        for (trx in logs) {
            when (trx.transaction._namet.trim { it <= ' ' }) {
                "RETIRO AHORROS" -> retiroAhorros(trx)
                "CONSULTA DE SALDOS" -> consultaSaldo(trx)
                "CONSULTA SALDOS CC" -> consultaSaldo(trx)
                "CONSULTA SALDOS AH" -> consultaSaldo(trx)
                "GENERACION OTP" -> generacionOtp(trx)
                "DEPOSITO AHORROS" -> depositoAhorros(trx)
                else -> {
                    Toast.makeText(this, "Transaccion no listada " + trx.transaction._namet, Toast.LENGTH_SHORT).show()
                }
            }
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