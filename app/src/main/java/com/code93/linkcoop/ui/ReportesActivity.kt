package com.code93.linkcoop.ui

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.code93.linkcoop.*
import com.code93.linkcoop.Tools.showDialogErrorCallback
import com.code93.linkcoop.Tools.showDialogErrorCancelCallback
import com.code93.linkcoop.Tools.showDialogPositive
import com.code93.linkcoop.adapters.ReportesAdapter
import com.code93.linkcoop.adapters.ReportesCallback
import com.code93.linkcoop.models.CierreData
import com.code93.linkcoop.models.CierreTransaccion
import com.code93.linkcoop.models.FieldsTrx
import com.code93.linkcoop.models.LogTransacciones
import com.code93.linkcoop.network.DownloadCallback
import com.code93.linkcoop.network.DownloadXmlTask
import com.code93.linkcoop.viewmodel.LogTransaccionesViewModel
import com.code93.linkcoop.xmlParsers.XmlParser.parse
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import dmax.dialog.SpotsDialog
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.util.*

class ReportesActivity : AppCompatActivity() {

    private lateinit var logData: LogTransaccionesViewModel
    private lateinit var rvReportes : RecyclerView
    private lateinit var reportesAdapter: ReportesAdapter
    private lateinit var logsData : List<LogTransacciones>

    private lateinit var dialog : AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reportes)

        dialog = SpotsDialog.Builder()
                .setContext(this)
                .setCancelable(false)
                .setMessage("Cargando transacciones")
                .build()

        dialog.show()

        rvReportes = findViewById(R.id.rvReportes)

        reportesAdapter = ReportesAdapter(this, object : ReportesCallback {
            override fun onReporteCallback(imprimir: Boolean, value: LogTransacciones) {
                if (imprimir) {
                    val intent = Intent(this@ReportesActivity, ImpresionActivity::class.java)
                    intent.putExtra("logTransacciones", value)
                    startActivity(intent)
                    finish()
                }
            }

        })

        logData = ViewModelProvider(this).get(LogTransaccionesViewModel::class.java)

        logData.readAllData.observe(this, Observer { logs ->
            logsData = logs
            dialog.dismiss()
            cargarData(logs)
        })

    }

    private fun cargarData(logs: List<LogTransacciones>) {
        if (logs.isEmpty()) {
            val btnRealizarCierre = findViewById<Button>(R.id.btnRealizarCierre)
            btnRealizarCierre.visibility = View.INVISIBLE
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

    fun realizarCierre(view: View) {
        dialog = SpotsDialog.Builder()
                .setContext(this)
                .setCancelable(false)
                .setMessage("Realizando cierre")
                .build()
        dialog.show()

        val gson = Gson()

        val listTrxCode : MutableList<String> = ArrayList()
        FirebaseCrashlytics.getInstance().log("ReportesActivity LOGDATA: " + gson.toJson(logsData))
        for (log in logsData) {
            listTrxCode.add(log.fieldsTrxSend.transaction_code)
        }

        val set: Set<String> = HashSet(listTrxCode)
        listTrxCode.clear()
        listTrxCode.addAll(set)

        val listCierreData : MutableList<CierreData> = ArrayList()
        for (code in listTrxCode) {
            val cierreData = CierreData()
            var values : Double = 0.0
            var commissions : Double = 0.0
            for (log in logsData) {
                if (log.fieldsTrxSend.transaction_code == code) {
                    cierreData._count ++
                    if (log.fieldsTrxSend.transaction_amount.isNotEmpty()){
                        values += parseDouble(log.fieldsTrxSend.transaction_amount)
                    }
                    if (log.fieldsTrxSend.commision_amount.isNotEmpty()) {
                        commissions += parseDouble(log.fieldsTrxSend.commision_amount)
                    }
                }
            }
            cierreData._code = code
            cierreData._value = values.toString()
            cierreData._commission = commissions.toString()
            listCierreData.add(cierreData)
        }

        for (cierre in listCierreData) {
            Log.d("Cierre: ", "Cierre ${cierre._code} monto total ${cierre._value}")
        }

        val cierreTransaccion = CierreTransaccion(listCierreData.toList())

        val jsonCierre : String = gson.toJson(cierreTransaccion)
        realizarTransaccionCierre(jsonCierre)
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
        showDialogErrorCallback(this, "Error de conexion", object : DialogCallback {
            override fun onDialogCallback(value: Int) {
                //logData.deleteAllLogTransaccioness()
                startActivity(Intent(this@ReportesActivity, MainActivity::class.java))
                finish()
            }
        })
    }

    private fun procesarRespuesta(response: String) {
        try {
            val fieldsTrx : FieldsTrx = parse(response, "reply_close")
            Log.d("FieldTRX", Objects.requireNonNull(fieldsTrx.token_data))
            val tokenData = TokenData()
            tokenData.getTokens(Objects.requireNonNull(fieldsTrx.token_data))
            if (fieldsTrx.response_code == "00") {
                dialog.dismiss()
                logData.deleteAllLogTransaccioness()
                showDialogPositive(this, tokenData.B1, object : DialogCallback {
                    override fun onDialogCallback(value: Int) {
                        startActivity(Intent(this@ReportesActivity, LoginActivity::class.java))
                        finish()
                    }
                })
            } else {
                dialog.dismiss()
                if (fieldsTrx.response_code == "72") {
                    showDialogErrorCancelCallback(this, "${tokenData.B1} ¿BORRAR REPORTE DE TRANSACCIONES?", object : DialogCallback {
                        override fun onDialogCallback(value: Int) {
                            if (value == 0) {
                                logData.deleteAllLogTransaccioness()
                            }
                            startActivity(Intent(this@ReportesActivity, LoginActivity::class.java))
                            finish()
                        }
                    })
                } else {
                    showDialogErrorCallback(this, tokenData.B1, object : DialogCallback {
                        override fun onDialogCallback(value: Int) {
                            startActivity(Intent(this@ReportesActivity, LoginActivity::class.java))
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