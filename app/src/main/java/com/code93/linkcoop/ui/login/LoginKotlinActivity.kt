package com.code93.linkcoop.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.code93.linkcoop.databinding.ActivityLoginBinding
import com.code93.linkcoop.viewmodel.CooperativaViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginKotlinActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityLoginBinding

    private val auth = FirebaseAuth.getInstance()
    private val viewModelDatabase: CooperativaViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(mBinding.root)



    }
}