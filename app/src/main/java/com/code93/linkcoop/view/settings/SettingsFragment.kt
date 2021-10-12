package com.code93.linkcoop.view.settings

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.code93.linkcoop.*
import com.code93.linkcoop.core.DialogCallback
import com.code93.linkcoop.core.Tools.showDialogErrorCallback
import com.code93.linkcoop.core.Tools.showDialogPositive
import com.code93.linkcoop.databinding.FragmentSettingsBinding
import com.code93.linkcoop.network.DownloadCallback
import com.code93.linkcoop.network.DownloadXmlTask
import com.code93.linkcoop.persistence.cache.SP2.Companion.SP_LOGIN
import com.code93.linkcoop.persistence.cache.SP2.Companion.comercio_nombre
import com.code93.linkcoop.persistence.cache.SP2.Companion.fechaUltimoLogin
import com.code93.linkcoop.persistence.cache.SP2.Companion.user_encript
import com.code93.linkcoop.ui.ConfiguracionActivity
import com.code93.linkcoop.ui.login.LoginActivity
import com.code93.linkcoop.xmlParsers.XmlParser.parse
import dmax.dialog.SpotsDialog
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.util.*


class SettingsFragment : Fragment(), DownloadCallback {

    private lateinit var dialog : AlertDialog

    private var _binding: FragmentSettingsBinding? = null
    private val mBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        dialog = SpotsDialog.Builder()
            .setContext(requireContext())
            .setCancelable(false)
            .setMessage("Procesando")
            .build()

        mBinding.comercioName.text = MyApp.sp2!!.getString(comercio_nombre, "")
        obtenerUltimoLogin()

        mBinding.lyConfiguracion.setOnClickListener { configuracion() }
        mBinding.lyCerrarSesion.setOnClickListener { cerrarSesion() }
        mBinding.lyAbout.setOnClickListener { about() }

        return mBinding.root
    }

    private fun configuracion() {
        startActivity(Intent(requireContext(), ConfiguracionActivity::class.java))
    }

    private fun cerrarSesion() {
        dialog.show()
        val user_encript = MyApp.sp2!!.getString(user_encript, "")
        val xmlLogOff = ToolsXML.requestLogoff(user_encript)
        val task = DownloadXmlTask(xmlLogOff, this)
        task.execute(xmlLogOff)
    }

    private fun about() {

    }

    private fun obtenerUltimoLogin() {
        val fechaUltimoLogin = MyApp.sp2!!.getString(fechaUltimoLogin, "")
        val formatUltimoLogin = fechaUltimoLogin
        mBinding.tvUltimoLogin.text = (formatUltimoLogin)
    }

    override fun onDownloadCallback(response: String) {
        if (response == "Error de conexion") {
            showErrorConexion()
        } else {
            procesarRespuesta(response)
        }
    }

    private fun procesarRespuesta(response: String) {
        try {
            val (_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, response_code, _, _, _, _, _, _, _, _, _, _, _, token_data) = parse(
                response,
                "reply_logoff"
            )
            Log.d("FieldTRX", Objects.requireNonNull(token_data))
            val tokenData = TokenData()
            tokenData.getTokens(Objects.requireNonNull(token_data))
            if (response_code == "00") {
                MyApp.sp2!!.putBoolean(SP_LOGIN, false)
                dialog.dismiss()
                showDialogPositive(requireContext(), tokenData.B1, object : DialogCallback {
                    override fun onDialogCallback(value: Int) {
                        startActivity(Intent(requireContext(), LoginActivity::class.java))
                        requireActivity().finish()
                    }
                })
            } else {
                dialog.dismiss()
                showDialogErrorCallback(requireContext(), tokenData.B1, object : DialogCallback {
                    override fun onDialogCallback(value: Int) {
                        MyApp.sp2!!.putBoolean(SP_LOGIN, false)
                        startActivity(Intent(requireContext(), LoginActivity::class.java))
                        requireActivity().finish()
                    }
                })
            }
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun showErrorConexion() {
        dialog.dismiss()
        showDialogErrorCallback(requireContext(), "Error de conexion", object : DialogCallback {
            override fun onDialogCallback(value: Int) {

            }
        })
    }
}