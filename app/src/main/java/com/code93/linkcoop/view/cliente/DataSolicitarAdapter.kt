package com.code93.linkcoop.view.cliente

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.code93.linkcoop.databinding.FragmentSolicitarDocumentoBinding
import com.code93.linkcoop.persistence.models.DataTransaccion

class DataSolicitarAdapter(
    private val values: List<DataTransaccion>
) : RecyclerView.Adapter<DataSolicitarAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentSolicitarDocumentoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        //holder.idView.text = item.id
        //holder.contentView.text = item.content
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentSolicitarDocumentoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val nameElemento: TextView = binding.nameElemento
        val tvValue: TextView = binding.tvValue
    }

}