package com.code93.linkcoop.view.cliente

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.code93.linkcoop.R
import com.code93.linkcoop.databinding.ActivitySolicitarDatosClienteBinding

class SolicitarDatosClienteActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivitySolicitarDatosClienteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySolicitarDatosClienteBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
    }
}