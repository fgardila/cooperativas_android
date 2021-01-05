package com.code93.linkcoop.ui

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.code93.linkcoop.R
import com.code93.linkcoop.viewmodel.CooperativaViewModel
import com.code93.linkcoop.viewmodel.LogTransaccionesViewModel
import kotlin.math.log

class ReportesActivity : AppCompatActivity() {

    private lateinit var logData: LogTransaccionesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reportes)

        val tvTransaccion = findViewById<TextView>(R.id.tvTransaccion)

        logData = ViewModelProvider(this).get(LogTransaccionesViewModel::class.java)

        logData.readAllData.observe(this, Observer { logs ->

        })

    }


}