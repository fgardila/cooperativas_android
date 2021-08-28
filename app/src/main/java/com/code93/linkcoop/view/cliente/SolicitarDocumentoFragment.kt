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
import com.code93.linkcoop.DataElements
import com.code93.linkcoop.R
import com.code93.linkcoop.persistence.models.DataTransaccion

class SolicitarDocumentoFragment : Fragment() {

    private var columnCount = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_solicitar_documento_list, container, false)

        val listDataTransaccion = mutableListOf<DataTransaccion>()
        listDataTransaccion.add(DataTransaccion(
            "Documento de identidad",
            InputType.TYPE_CLASS_NUMBER and InputType.TYPE_NUMBER_FLAG_DECIMAL,
            15,
            "Ingresa documento de identidad",
            DataTransaccion.TipoDato.CEDULA,
            R.drawable.ic_network_check)
        )
        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = DataSolicitarAdapter(listDataTransaccion)
            }
        }
        return view
    }
}