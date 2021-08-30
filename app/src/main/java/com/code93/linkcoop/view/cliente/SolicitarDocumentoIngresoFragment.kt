package com.code93.linkcoop.view.cliente

import android.os.Bundle
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.code93.linkcoop.StringTools
import com.code93.linkcoop.StringTools.ValidaCedulaRuc
import com.code93.linkcoop.databinding.FragmentSolicitarDocumentoIngresoBinding
import com.code93.linkcoop.persistence.models.DataTransaccion


class SolicitarDocumentoIngresoFragment : Fragment() {

    private var _binding: FragmentSolicitarDocumentoIngresoBinding? = null
    private val mBinding get() = _binding!!

    val args: SolicitarDocumentoIngresoFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSolicitarDocumentoIngresoBinding.inflate(inflater, container, false)

        val data = args.dataTransaccion

        with(data!!) {
            mBinding.tvNomTrans.text = name
            mBinding.tilData.hint = subTitulo
            mBinding.tilData.startIconDrawable = AppCompatResources.getDrawable(requireContext(), drawable)
            mBinding.etData.inputType = inputType
            mBinding.etData.filters = arrayOf<InputFilter>(LengthFilter(maxLength))
            if (value != null) {
                mBinding.etData.setText(value)
            }
        }

        mBinding.floatingActionButton.setOnClickListener {
            if (mBinding.etData.text.toString().isEmpty()) {
                mBinding.etData.error = "Ingrese la informacion solicitada"
            } else {
                enviarData(data)
            }
        }

        return mBinding.root
    }

    private fun enviarData(data: DataTransaccion) {
        if (data.tipo != null) {
            if (data.tipo == DataTransaccion.TipoDato.CEDULA) {
                val validated = ValidaCedulaRuc(mBinding.etData.text.toString())
                if (validated) {
                    data.value = mBinding.etData.text.toString()
                    val action = SolicitarDocumentoIngresoFragmentDirections.actionDataIngresoFragmentToSolicitarDocumentoFragment(data)
                    Navigation.findNavController(mBinding.root).navigate(action)
                } else {
                    mBinding.etData.error = "Documento de identidad no valido"
                    Toast.makeText(requireContext(), "Documento no valido", Toast.LENGTH_SHORT).show()
                }
            } else {
                data.value = mBinding.etData.text.toString()
                val action = SolicitarDocumentoIngresoFragmentDirections.actionDataIngresoFragmentToSolicitarDocumentoFragment(data)
                Navigation.findNavController(mBinding.root).navigate(action)
            }
        }
    }
}