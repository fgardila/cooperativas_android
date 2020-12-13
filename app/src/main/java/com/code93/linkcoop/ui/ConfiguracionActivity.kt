package com.code93.linkcoop.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.code93.linkcoop.R

class ConfiguracionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracion)
    }

    fun llavesDeCifrado(view: View) {
        startActivity(Intent(this, LlavesDeCifradoActivity::class.java))
    }

    fun direccionIp(view: View) {
        startActivity(Intent(this, ConfigNetworkActivity::class.java))
    }
}