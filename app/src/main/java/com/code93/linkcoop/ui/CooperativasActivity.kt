package com.code93.linkcoop.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.code93.linkcoop.R
import com.code93.linkcoop.adapters.MenuCoopAdapter
import com.code93.linkcoop.viewmodel.CooperativaViewModel
import kotlinx.android.synthetic.main.activity_cooperativas.*

class CooperativasActivity : FragmentActivity() {

    private lateinit var cooperativaViewModel: CooperativaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cooperativas)

        cooperativaViewModel = ViewModelProvider(this).get(CooperativaViewModel::class.java)

        val adapter = MenuCoopAdapter(this)
        val recyclerView = rvCooperativas
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        cooperativaViewModel.readAllData.observe(this, Observer { coops ->
            adapter.setDatas(coops)
        })



    }
}