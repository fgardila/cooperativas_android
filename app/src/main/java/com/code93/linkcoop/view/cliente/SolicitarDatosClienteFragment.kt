package com.code93.linkcoop.view.cliente

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.code93.linkcoop.R
import com.code93.linkcoop.ToolsXML
import com.code93.linkcoop.databinding.FragmentSolicitarDatosClienteBinding
import com.code93.linkcoop.network.DownloadCallback
import com.code93.linkcoop.network.DownloadXmlTask
import com.code93.linkcoop.persistence.cache.DataTrans
import com.code93.linkcoop.persistence.models.DataTransaccion
import com.code93.linkcoop.persistence.models.FieldsTrx
import com.code93.linkcoop.xmlParsers.XmlParser
import dmax.dialog.SpotsDialog
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.lang.Exception


class SolicitarDatosClienteFragment : Fragment(), CallbackData {

    private lateinit var spotDialog: AlertDialog

    private var columnCount = 1
    private var listDataTransaccion = ArrayDatosIngreso.listDataTransaccion

    private var _binding: FragmentSolicitarDatosClienteBinding? = null
    private val mBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSolicitarDatosClienteBinding.inflate(inflater, container, false)

        spotDialog = SpotsDialog.Builder()
            .setContext(requireContext())
            .setCancelable(false)
            .setMessage("Conectando...")
            .build()

        mBinding.tvTransaccion.text = "Creacion de cliente"

        try {
            val args: SolicitarDatosClienteFragmentArgs by navArgs()
            val position = args.position
            val value = args.value
            listDataTransaccion[position].value = value
        } catch (e: Exception) {
            Log.e("ERROR", "No llegan args")
        }

        with(mBinding.rvElementos) {
            layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
            adapter = DataSolicitarAdapter(listDataTransaccion, this@SolicitarDatosClienteFragment)
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
                Toast.makeText(
                    requireContext(),
                    "El " + data.name.trim { it <= ' ' } + " no se ha completado.",
                    Toast.LENGTH_SHORT).show()
                camposOk = false
                break
            }
        }
        if (camposOk) {
            spotDialog.show()
            transaccionCrearCliente()
        }
    }

    var fieldsTrxSend: FieldsTrx = FieldsTrx()

    private fun transaccionCrearCliente() {
        val xml = ToolsXML.requestContinousDocument(DataTrans.clienteData.document)

        try {
            fieldsTrxSend = XmlParser.parse(xml, "request_inquiry")
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val task = DownloadXmlTask(xml,
            object : DownloadCallback {
                override fun onDownloadCallback(response: String) {
                    if (response == "Error de conexion") showErrorConexion() else procesarRespuesta(
                        "reply_inquiry",
                        response
                    )
                }
            })
        task.execute(xml)
    }

    private fun procesarRespuesta(replyTag: String, response: String) {

    }

    private fun showErrorConexion() {
        TODO("Not yet implemented")
    }

    override fun onClickData(data: DataTransaccion, position: Int) {
        val action =
            SolicitarDatosClienteFragmentDirections.actionSolicitarDatosClienteFragmentToSolicitarDatosClienteIngresoFragment(
                position = position,
                dataTransaccion = data
            )
        Navigation.findNavController(mBinding.root).navigate(action)
    }
}