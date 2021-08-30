package com.code93.linkcoop.view.cliente

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.code93.linkcoop.R
import com.code93.linkcoop.databinding.ActivityClienteBinding

class SolicitarDocumentoActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityClienteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityClienteBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

    }
}