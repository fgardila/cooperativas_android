package com.code93.linkcoop.view.reportes

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.code93.linkcoop.core.Tools
import com.code93.linkcoop.databinding.FragmentReporteCierreBinding
import com.code93.linkcoop.persistence.models.LogTransacciones
import com.code93.linkcoop.ui.CierreActivity
import com.code93.linkcoop.ui.ImpresionActivity
import com.code93.linkcoop.ui.ReportesActivity
import com.code93.linkcoop.viewmodel.LogTransaccionesViewModel
import dmax.dialog.SpotsDialog

class ReporteCierreFragment : Fragment() {

    private var _binding: FragmentReporteCierreBinding? = null
    private val mBinding get() = _binding!!
    private lateinit var dialog : AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentReporteCierreBinding.inflate(inflater, container, false)
        dialog = SpotsDialog.Builder()
            .setContext(requireContext())
            .setCancelable(false)
            .setMessage("Cargando transacciones")
            .build()

        mBinding.lyReimprimir.setOnClickListener { reimprimirUltimoTicket() }
        mBinding.lyHistorialTransacciones.setOnClickListener { historialTransacciones() }
        mBinding.lyRealizarCierre.setOnClickListener { realizarCierre() }

        return mBinding.root
    }

    private fun reimprimirUltimoTicket() {
        dialog.show()
        val logData = ViewModelProvider(this).get(LogTransaccionesViewModel::class.java)
        logData.readAllData.observe(requireActivity(), Observer { logs ->
            dialog.dismiss()
            cargarData(logs)
        })
    }

    private fun cargarData(logs: List<LogTransacciones>?) {
        val lastTrx = logs!!.size - 1;
        if (lastTrx < 0) {
            Tools.showDialogError(requireContext(), "No se encuentran transacciones")
        } else {
            Log.d("TAG", "Ultima transaccion ${lastTrx}")
            val ultimoLog = logs.get(lastTrx)
            val intent = Intent(requireContext(), ImpresionActivity::class.java)
            intent.putExtra("logTransacciones", ultimoLog)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    private fun historialTransacciones() {
        startActivity(Intent(requireContext(), ReportesActivity::class.java))
    }

    private fun realizarCierre() {
        startActivity(Intent(requireContext(), CierreActivity::class.java))
    }
}