package com.code93.linkcoop.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.code93.linkcoop.R
import com.code93.linkcoop.adapters.MenuCoopAdapter
import com.code93.linkcoop.models.Cooperativa
import com.code93.linkcoop.viewmodel.CooperativaViewModel
import com.google.android.gms.common.api.CommonStatusCodes
import kotlinx.android.synthetic.main.activity_cooperativas.*


class CooperativasActivity : FragmentActivity(), MenuCoopAdapter.OnClickCoop {

    private lateinit var cooperativaViewModel: CooperativaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cooperativas)

        cooperativaViewModel = ViewModelProvider(this).get(CooperativaViewModel::class.java)

        val adapter = MenuCoopAdapter(this, this)
        val recyclerView = rvCooperativas
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        //recyclerView.layoutManager = LinearLayoutManager(this)

        cooperativaViewModel.readAllData.observe(this, Observer { coops ->
            adapter.setDatas(coops)
        })
    }

    override fun onItemClick(cooperativa: Cooperativa?) {
        Toast.makeText(this, "" + cooperativa!!._namec, Toast.LENGTH_LONG).show()
        val intent = Intent(this, TransaccionesActivity::class.java)
        intent.putExtra("coop", cooperativa)
        startActivity(intent)
    }
}