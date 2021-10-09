package com.code93.linkcoop.view.cliente

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.code93.linkcoop.*
import com.code93.linkcoop.Tools.showDialogError
import com.code93.linkcoop.Tools.showDialogErrorCallback
import com.code93.linkcoop.databinding.FragmentSolicitarDocumentoListBinding
import com.code93.linkcoop.network.DownloadCallback
import com.code93.linkcoop.network.DownloadXmlTask
import com.code93.linkcoop.persistence.cache.DataTrans
import com.code93.linkcoop.persistence.models.ClienteData
import com.code93.linkcoop.persistence.models.DataTransaccion
import com.code93.linkcoop.persistence.models.FieldsTrx
import com.code93.linkcoop.ui.TransaccionActivity
import com.code93.linkcoop.xmlParsers.XmlParser.parse
import dmax.dialog.SpotsDialog
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.util.*

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
                R.drawable.ic_account
            )
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
            transaccionDocumento()
        }
    }

    var fieldsTrxSend: FieldsTrx = FieldsTrx()

    private fun transaccionDocumento() {
        val xml = ToolsXML.requestContinousDocument(DataTrans.clienteData.document)

        try {
            fieldsTrxSend = parse(xml, "request_continous")
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
            val (_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, response_code, _, _, _, _, _, _, _, _, _, _, _, token_data) = parse(
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
                } else if (response_code == "23") {
                    val intent = Intent(requireContext(), SolicitarDatosClienteActivity::class.java)
                    requireActivity().startActivity(intent)
                    finish()
                } else {
                    spotDialog.dismiss()
                    showDialogErrorCallback(
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
                showDialogError(requireContext(), "No llegaron Tokens")
            }
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
            try {
                val (_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, token_data) = parse(
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
                    showDialogErrorCallback(
                        requireContext(),
                        tokenData.B1,
                        object : DialogCallback {
                            override fun onDialogCallback(value: Int) {
                                finish()
                            }
                        })
                } else {
                    spotDialog.dismiss()
                    showDialogError(requireContext(), "No llegaron Tokens")
                }
            } catch (o: XmlPullParserException) {
                o.printStackTrace()
                spotDialog.dismiss()
                showDialogError(requireContext(), "Error al procesar la respuesta.")
            } catch (o: IOException) {
                o.printStackTrace()
                spotDialog.dismiss()
                showDialogError(requireContext(), "Error al procesar la respuesta.")
            }
        } catch (e: IOException) {
            e.printStackTrace()
            try {
                val (_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, token_data) = parse(
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
                    showDialogErrorCallback(
                        requireContext(),
                        tokenData.B1,
                        object : DialogCallback {
                            override fun onDialogCallback(value: Int) {
                                finish()
                            }
                        })
                } else {
                    spotDialog.dismiss()
                    showDialogError(requireContext(), "No llegaron Tokens")
                }
            } catch (o: XmlPullParserException) {
                o.printStackTrace()
                spotDialog.dismiss()
                showDialogError(requireContext(), "Error al procesar la respuesta.")
            } catch (o: IOException) {
                o.printStackTrace()
                spotDialog.dismiss()
                showDialogError(requireContext(), "Error al procesar la respuesta.")
            }
        }
    }

    private fun finish() {
        requireActivity().finish()
    }

    private fun showErrorConexion() {
        spotDialog.dismiss()
        showDialogError(requireContext(), "Error de conexion")
    }

    override fun onClickData(data: DataTransaccion, position: Int) {
        val action =
            SolicitarDocumentoFragmentDirections.actionSolicitarDocumentoFragmentToDataIngresoFragment(
                data
            )
        Navigation.findNavController(mBinding.root).navigate(action)
    }
}