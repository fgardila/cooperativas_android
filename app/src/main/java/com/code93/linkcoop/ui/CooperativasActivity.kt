package com.code93.linkcoop.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.code93.linkcoop.R
import com.code93.linkcoop.adapters.MenuCoopAdapter
import com.code93.linkcoop.models.Cooperativa
import com.code93.linkcoop.models.Instituciones
import com.code93.linkcoop.viewmodel.InstitucionesViewModel


class CooperativasActivity : FragmentActivity(), MenuCoopAdapter.OnClickCoop {

    private lateinit var institucionesViewModel: InstitucionesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cooperativas)

        institucionesViewModel = ViewModelProvider(this).get(InstitucionesViewModel::class.java)

        val rvCooperativas = findViewById<RecyclerView>(R.id.rvCooperativas)

        val adapter = MenuCoopAdapter(this, this)
        val recyclerView = rvCooperativas
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        //recyclerView.layoutManager = LinearLayoutManager(this)

        institucionesViewModel.readAllData.observe(this, Observer { coops ->
            adapter.setDatas(coops)
        })
    }

    override fun onItemClick(instituciones: Instituciones?) {
        Log.d("CooperativasActivity.kt", "Instituacion ${instituciones!!._namec}" )
        val intent = Intent(this, TransaccionesActivity::class.java)
        intent.putExtra("Institucion", instituciones)
        startActivity(intent)
    }
}