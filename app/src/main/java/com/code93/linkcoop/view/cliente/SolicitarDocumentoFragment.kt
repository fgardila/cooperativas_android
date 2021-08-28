package com.code93.linkcoop.view.cliente

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
import com.code93.linkcoop.DataElements
import com.code93.linkcoop.R
import com.code93.linkcoop.databinding.FragmentSolicitarDocumentoBinding
import com.code93.linkcoop.databinding.FragmentSolicitarDocumentoListBinding
import com.code93.linkcoop.persistence.models.DataTransaccion

class SolicitarDocumentoFragment : Fragment(), CallbackData {

    private var columnCount = 1
    private var _binding: FragmentSolicitarDocumentoListBinding? = null
    private val mBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSolicitarDocumentoListBinding.inflate(inflater, container, false)

        mBinding.tvTransaccion.text = "Validación cliente"

        val listDataTransaccion = mutableListOf<DataTransaccion>()
        listDataTransaccion.add(DataTransaccion(
            "Documento de identidad",
            InputType.TYPE_CLASS_NUMBER and InputType.TYPE_NUMBER_FLAG_DECIMAL,
            15,
            "Ingresa documento de identidad",
            DataTransaccion.TipoDato.CEDULA,
            R.drawable.ic_account)
        )

        with(mBinding.rvElementos) {
            layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
            adapter = DataSolicitarAdapter(listDataTransaccion, this@SolicitarDocumentoFragment)
        }
        return mBinding.root
    }

    override fun onClickData(data: DataTransaccion) {
        Toast.makeText(context, "Prueba", Toast.LENGTH_LONG).show()
        

    }
}