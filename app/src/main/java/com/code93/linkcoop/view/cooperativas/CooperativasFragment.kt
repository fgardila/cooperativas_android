package com.code93.linkcoop.view.cooperativas

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.code93.linkcoop.MyApp
import com.code93.linkcoop.adapters.MenuCoopAdapter
import com.code93.linkcoop.databinding.FragmentCooperativasBinding
import com.code93.linkcoop.databinding.FragmentReporteCierreBinding
import com.code93.linkcoop.persistence.cache.SP2
import com.code93.linkcoop.persistence.models.Cooperativa
import com.code93.linkcoop.ui.TransaccionesActivity
import com.code93.linkcoop.viewmodel.CooperativaViewModel

class CooperativasFragment : Fragment(), MenuCoopAdapter.OnClickCoop {

    private var _binding: FragmentCooperativasBinding? = null
    private val mBinding get() = _binding!!

    private lateinit var cooperativaViewModel: CooperativaViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCooperativasBinding.inflate(inflater, container, false)
        cooperativaViewModel = ViewModelProvider(this).get(CooperativaViewModel::class.java)

        mBinding.tvNegocio.text = MyApp.sp2!!.getString(SP2.comercio_nombre, "")

        val adapter = MenuCoopAdapter(requireContext(), this)
        mBinding.rvCooperativas.adapter = adapter

        mBinding.rvCooperativas.layoutManager = GridLayoutManager(requireContext(), 2)
        //recyclerView.layoutManager = LinearLayoutManager(this)

        cooperativaViewModel.readAllData.observe(requireActivity(), Observer { coops ->
            adapter.setDatas(coops)
        })

        return mBinding.root
    }

    override fun onItemClick(cooperativa: Cooperativa?) {
        //Toast.makeText(this, "" + cooperativa!!._namec, Toast.LENGTH_LONG).show()
        val intent = Intent(requireContext(), TransaccionesActivity::class.java)
        intent.putExtra("coop", cooperativa)
        startActivity(intent)
    }
}