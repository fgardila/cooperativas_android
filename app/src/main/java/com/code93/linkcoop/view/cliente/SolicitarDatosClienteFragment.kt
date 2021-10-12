package com.code93.linkcoop.view.cliente

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
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
import com.code93.linkcoop.*
import com.code93.linkcoop.core.DialogCallback
import com.code93.linkcoop.core.Tools
import com.code93.linkcoop.databinding.FragmentSolicitarDatosClienteBinding
import com.code93.linkcoop.network.DownloadCallback
import com.code93.linkcoop.network.DownloadXmlTask
import com.code93.linkcoop.persistence.cache.DataTrans
import com.code93.linkcoop.persistence.models.ClienteData
import com.code93.linkcoop.persistence.models.DataTransaccion
import com.code93.linkcoop.persistence.models.FieldsTrx
import com.code93.linkcoop.ui.TransaccionActivity
import com.code93.linkcoop.xmlParsers.XmlParser
import dmax.dialog.SpotsDialog
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.lang.Exception
import java.util.*


class SolicitarDatosClienteFragment : Fragment(), CallbackData {

    private lateinit var spotDialog: AlertDialog
    private lateinit var mAdapter: DataSolicitarAdapter

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

        mAdapter = DataSolicitarAdapter(listDataTransaccion, this@SolicitarDatosClienteFragment)

        with(mBinding.rvElementos) {
            layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
            adapter = mAdapter
        }

        mBinding.btnContinuar.setOnClickListener {
            analizarDatos()
        }

        mBinding.btnLimpiar.setOnClickListener {
            limpiarCampos()
        }

        return mBinding.root
    }

    private fun limpiarCampos() {
        listDataTransaccion = ArrayDatosIngreso.listDataTransaccion
        mAdapter.values = listDataTransaccion
        mAdapter.notifyDataSetChanged()
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
            val data = ClienteData()
            data.firtName = listDataTransaccion[0].value
            data.lastName = listDataTransaccion[1].value
            data.phoneNumber = listDataTransaccion[2].value
            data.document = listDataTransaccion[3].value
            data.emailAddress = listDataTransaccion[4].value

            DataTrans.clienteData = data

            spotDialog.show()
            transaccionCrearCliente()
        }
    }

    var fieldsTrxSend: FieldsTrx = FieldsTrx()

    private fun transaccionCrearCliente() {
        val xml = ToolsXML.requestCrearCliente(DataTrans.clienteData)

        try {
            fieldsTrxSend = XmlParser.parse(xml, "request_continous")
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val task = DownloadXmlTask(xml,
            object : DownloadCallback {
                override fun onDownloadCallback(response: String) {
                    if (response == "Error de conexion") showErrorConexion() else procesarRespuesta(
                        "reply_continous",
                        response
                    )
                }
            })
        task.execute(xml)
    }

    private fun procesarRespuesta(replyTag: String, response: String) {
        try {
            val (_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, response_code, _, _, _, _, _, _, _, _, _, _, _, token_data) = XmlParser.parse(
                response,
                replyTag
            )
            if (token_data != "") {
                Log.d("FieldTRX", Objects.requireNonNull(token_data))
                val tokenData = TokenData()
                tokenData.getTokens(Objects.requireNonNull(token_data))
                if (response_code == "00") {
                    val intent = Intent(requireContext(), TransaccionActivity::class.java)
                    requireActivity().startActivity(intent)
                    finish()
                } else {
                    spotDialog.dismiss()
                    Tools.showDialogErrorCallback(
                        requireContext(),
                        tokenData.B1,
                        object : DialogCallback {
                            override fun onDialogCallback(value: Int) {
                                finish()
                            }
                        })
                }
            } else {
                spotDialog.dismiss()
                Tools.showDialogError(requireContext(), "No llegaron Tokens")
            }
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
            try {
                val (_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, token_data) = XmlParser.parse(
                    response,
                    "default_reply_error"
                )
                if (token_data != "") {
                    spotDialog.dismiss()
                    Log.d(
                        "FieldTRX", Objects.requireNonNull(
                            token_data
                        )
                    )
                    val tokenData = TokenData()
                    tokenData.getTokens(Objects.requireNonNull(token_data))
                    Tools.showDialogErrorCallback(
                        requireContext(),
                        tokenData.B1,
                        object : DialogCallback {
                            override fun onDialogCallback(value: Int) {
                                finish()
                            }
                        })
                } else {
                    spotDialog.dismiss()
                    Tools.showDialogError(requireContext(), "No llegaron Tokens")
                }
            } catch (o: XmlPullParserException) {
                o.printStackTrace()
                spotDialog.dismiss()
                Tools.showDialogError(requireContext(), "Error al procesar la respuesta.")
            } catch (o: IOException) {
                o.printStackTrace()
                spotDialog.dismiss()
                Tools.showDialogError(requireContext(), "Error al procesar la respuesta.")
            }
        } catch (e: IOException) {
            e.printStackTrace()
            try {
                val (_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, token_data) = XmlParser.parse(
                    response,
                    "default_reply_error"
                )
                if (token_data != "") {
                    spotDialog.dismiss()
                    Log.d(
                        "FieldTRX", Objects.requireNonNull(
                            token_data
                        )
                    )
                    val tokenData = TokenData()
                    tokenData.getTokens(Objects.requireNonNull(token_data))
                    Tools.showDialogErrorCallback(
                        requireContext(),
                        tokenData.B1,
                        object : DialogCallback {
                            override fun onDialogCallback(value: Int) {
                                finish()
                            }
                        })
                } else {
                    spotDialog.dismiss()
                    Tools.showDialogError(requireContext(), "No llegaron Tokens")
                }
            } catch (o: XmlPullParserException) {
                o.printStackTrace()
                spotDialog.dismiss()
                Tools.showDialogError(requireContext(), "Error al procesar la respuesta.")
            } catch (o: IOException) {
                o.printStackTrace()
                spotDialog.dismiss()
                Tools.showDialogError(requireContext(), "Error al procesar la respuesta.")
            }
        }
    }

    private fun finish() {
        requireActivity().finish()
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