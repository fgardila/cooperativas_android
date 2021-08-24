package com.code93.linkcoop.ui

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.code93.linkcoop.*
import com.code93.linkcoop.persistence.models.*
import com.code93.linkcoop.network.DownloadCallback
import com.code93.linkcoop.network.DownloadXmlTask
import com.code93.linkcoop.view.HomeActivity
import com.code93.linkcoop.viewmodel.LogTransaccionesViewModel
import com.code93.linkcoop.xmlParsers.XmlParser
import com.google.gson.Gson
import dmax.dialog.SpotsDialog
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.util.*

class CierreActivity : AppCompatActivity() {

    private lateinit var logData: LogTransaccionesViewModel
    private lateinit var logsData : List<LogTransacciones>
    private lateinit var dialog : AlertDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cierre)

        dialog = SpotsDialog.Builder()
            .setContext(this)
            .setCancelable(false)
            .setMessage("Cargando transacciones")
            .build()

        dialog.show()

        logData = ViewModelProvider(this).get(LogTransaccionesViewModel::class.java)

        logData.readAllData.observe(this, Observer { logs ->
            logsData = logs
            dialog.dismiss()
            cargarData(logs)
        })
    }

    private fun cargarData(logs: List<LogTransacciones>?) {
        val cantidadTransacciones = logs!!.size

        val listTrxCode : MutableList<Transaction> = ArrayList()
        for (log in logs) {
            Log.d("TRX NAME", "" + log.transaction._namet)
            listTrxCode.add(log.transaction)
        }
        val set: Set<Transaction> = HashSet(listTrxCode)
        listTrxCode.clear()
        listTrxCode.addAll(set)
        val listCierreData : MutableList<CierreData> = ArrayList()
        for (code in listTrxCode) {
            val cierreData = CierreData()
            var values : Double = 0.0
            var commissions : Double = 0.0
            for (log in logsData) {
                if (log.fieldsTrxSend.transaction_code == code._code) {
                    cierreData._count ++
                    if (log.fieldsTrxSend.transaction_amount.isNotEmpty()){
                        values += parseDouble(log.fieldsTrxSend.transaction_amount)
                    }
                    if (log.fieldsTrxSend.commision_amount.isNotEmpty()) {
                        commissions += parseDouble(log.fieldsTrxSend.commision_amount)
                    }
                }
            }
            cierreData._code = code._code
            cierreData._value = values.toString()
            cierreData._commission = commissions.toString()
            listCierreData.add(cierreData)
        }

        for (cierre in listCierreData) {
            Log.d("Cierre: ", "Cierre ${cierre._code} monto total ${cierre._value}")
        }

        val ticket = StringBuilder()
        ticket.append("Link COOP\n")
        ticket.append("\n")
        ticket.append("Total de transacciones : ${cantidadTransacciones}\n")
        ticket.append("\n")
        for (cierre in listCierreData) {
            var nameTrx : String = ""
            for (trx in listTrxCode) {
                if (trx._code == cierre._code) {
                    nameTrx = trx._namet
                    break
                } else {
                    nameTrx = "ERROR"
                }
            }
            ticket.append("Transacción: ${nameTrx} " +
                    "\nNumero de transacciones: ${cierre._count} " +
                    "\nMonto total ${cierre._value}" +
                    "\nMonto comisiones ${cierre._commission}\n\n")
        }
        ticket.append("\n")
        ticket.append("\n")
        ticket.append("\n")
        ticket.append("\n")
        ticket.append("\n")

        val textView = findViewById<TextView>(R.id.textViewRecibo)
        textView.text = ticket.toString()

        val btnCierre = findViewById<Button>(R.id.btnCierre)
        btnCierre.setOnClickListener {
            dialog = SpotsDialog.Builder()
                .setContext(this)
                .setCancelable(false)
                .setMessage("Realizando cierre")
                .build()

            dialog.show()
            val gson = Gson()
            val cierreTransaccion = CierreTransaccion(listCierreData.toList())
            val jsonCierre : String = gson.toJson(cierreTransaccion)
            realizarTransaccionCierre(jsonCierre)
        }
    }

    private fun realizarTransaccionCierre(jsonCierre: String) {
        val xml = ToolsXML.requestClose(jsonCierre)
        val task = DownloadXmlTask(xml, object : DownloadCallback {
            override fun onDownloadCallback(response: String) {
                if (response == "Error de conexion")
                    showErrorConexion()
                else
                    procesarRespuesta(response)
            }
        })
        task.execute(xml)
    }

    private fun showErrorConexion() {
        dialog.dismiss()
        Tools.showDialogErrorCallback(this, "Error de conexion", object : DialogCallback {
            override fun onDialogCallback(value: Int) {
                //logData.deleteAllLogTransaccioness()
                startActivity(Intent(this@CierreActivity, HomeActivity::class.java))
                finish()
            }
        })
    }

    private fun procesarRespuesta(response: String) {
        try {
            val fieldsTrx : FieldsTrx = XmlParser.parse(response, "reply_close")
            Log.d("FieldTRX", Objects.requireNonNull(fieldsTrx.token_data))
            val tokenData = TokenData()
            tokenData.getTokens(Objects.requireNonNull(fieldsTrx.token_data))
            if (fieldsTrx.response_code == "00") {
                dialog.dismiss()
                logData.deleteAllLogTransaccioness()
                Tools.showDialogPositive(this, tokenData.B1, object : DialogCallback {
                    override fun onDialogCallback(value: Int) {
                        startActivity(Intent(this@CierreActivity, LoginActivity::class.java))
                        finish()
                    }
                })
            } else {
                dialog.dismiss()
                if (fieldsTrx.response_code == "72") {
                    Tools.showDialogErrorCancelCallback(
                        this,
                        "${tokenData.B1} ¿BORRAR REPORTE DE TRANSACCIONES?",
                        object : DialogCallback {
                            override fun onDialogCallback(value: Int) {
                                if (value == 0) {
                                    logData.deleteAllLogTransaccioness()
                                }
                                startActivity(
                                    Intent(
                                        this@CierreActivity,
                                        LoginActivity::class.java
                                    )
                                )
                                finish()
                            }
                        })
                } else {
                    Tools.showDialogErrorCallback(this, tokenData.B1, object : DialogCallback {
                        override fun onDialogCallback(value: Int) {
                            startActivity(Intent(this@CierreActivity, LoginActivity::class.java))
                            finish()
                        }
                    })
                }

            }
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun parseDouble(amount: String) : Double {
        return if (amount.equals(""))
            0.0
        else {
            amount.toDouble()
        }
    }
}