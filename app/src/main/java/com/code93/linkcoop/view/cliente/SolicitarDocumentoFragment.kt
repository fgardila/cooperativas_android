package com.code93.linkcoop.view.cliente

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.code93.linkcoop.DataElements
import com.code93.linkcoop.R
import com.code93.linkcoop.ToolsXML
import com.code93.linkcoop.databinding.FragmentSolicitarDocumentoBinding
import com.code93.linkcoop.databinding.FragmentSolicitarDocumentoListBinding
import com.code93.linkcoop.persistence.cache.DataTrans
import com.code93.linkcoop.persistence.models.ClienteData
import com.code93.linkcoop.persistence.models.DataTransaccion
import dmax.dialog.SpotsDialog

class SolicitarDocumentoFragment : Fragment(), CallbackData {

    private lateinit var spotDialog: AlertDialog

    private var columnCount = 1
    private var _binding: FragmentSolicitarDocumentoListBinding? = null
    private val mBinding get() = _binding!!
    private val listDataTransaccion = mutableListOf<DataTransaccion>()
    private val args: SolicitarDocumentoFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSolicitarDocumentoListBinding.inflate(inflater, container, false)

        spotDialog = SpotsDialog.Builder()
            .setContext(requireContext())
            .setCancelable(false)
            .setMessage("Conectando...")
            .build()

        mBinding.tvTransaccion.text = "Validación cliente"

        val dataValue = args.dataValue

        if (dataValue != null) {
            listDataTransaccion.clear()
            listDataTransaccion.add(dataValue)
            val clientData = ClienteData(document = dataValue.value)
            DataTrans.clienteData = clientData
        } else {
            val doc = DataTransaccion(
                "Documento de identidad",
                InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL,
                15,
                "Ingresa documento de identidad",
                DataTransaccion.TipoDato.CEDULA,
                R.drawable.ic_account)
            listDataTransaccion.clear()
            listDataTransaccion.add(doc)

        }

        with(mBinding.rvElementos) {
            layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
            adapter = DataSolicitarAdapter(listDataTransaccion, this@SolicitarDocumentoFragment)
        }

        mBinding.btnContinuar.setOnClickListener {
            analizarDatos()
        }
        return mBinding.root
    }

    private fun analizarDatos() {
        var camposOk = true
        for (data in listDataTransaccion) {
            if (data.value == null || data.value == "") {
                Toast.makeText(requireContext(), "El " + data.name.trim { it <= ' ' } + " no se ha completado.",
                    Toast.LENGTH_SHORT).show()
                camposOk = false
                break
            }
        }
        if (camposOk) {
            spotDialog.show()
            transaccionDocumento()
        }
    }

    private fun transaccionDocumento() {
        val xml = ToolsXML.requestContinousDocument(DataTrans.clienteData.document)


    }

    override fun onClickData(data: DataTransaccion) {
        val action = SolicitarDocumentoFragmentDirections.actionSolicitarDocumentoFragmentToDataIngresoFragment(data)
        Navigation.findNavController(mBinding.root).navigate(action)

    }
}