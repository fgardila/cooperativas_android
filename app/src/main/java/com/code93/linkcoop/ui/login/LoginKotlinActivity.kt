package com.code93.linkcoop.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.code93.linkcoop.core.logger.L
import com.code93.linkcoop.core.utils.ConnectivityUtils
import com.code93.linkcoop.databinding.ActivityLoginBinding
import com.code93.linkcoop.viewmodel.CooperativaViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginKotlinActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityLoginBinding

    private val auth = FirebaseAuth.getInstance()
    private val viewModelDatabase: CooperativaViewModel by viewModels()
    private val viewModelLogin: LoginViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        L.d("jflkajfldj")

        val isNetworkAvailable = ConnectivityUtils.isNetworkAvailable(this)
        val isInternetOn = ConnectivityUtils.isInternetOn(this)
        val isWiFiOn = ConnectivityUtils.isWiFiOn(this)

        mBinding.tvVersion.text = isNetworkAvailable.toString()
        mBinding.tvSerial.text = isInternetOn.toString()
        mBinding.tvGoogle.text = isWiFiOn.toString()


        val xmlString = "<?xml version='1.0' encoding='utf-8' ?><request_logon><bitmap>C000010810A0004C</bitmap><message_code>0800</message_code><transaction_code>930002</transaction_code><adquirer_date_time>2021-10-13 15:12:15</adquirer_date_time><adquirer_sequence>8</adquirer_sequence><terminal_id>TPOS-1002-000070</terminal_id><channel_id>2</channel_id><service_code>0030011001</service_code><product_id>012000</product_id><user_id>4P2LqfZrNf+KsSky5wGqOA==\n" +
                "    </user_id><password>4Hwrusd6Xx/RuIT+/tLGWg==\n" +
                "    </password></request_logon>"
        mBinding.btnSignIn.setOnClickListener {
            viewModelLogin.execute(xmlString)
        }

    }
}