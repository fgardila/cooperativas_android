package com.code93.linkcoop.view.cliente

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.code93.linkcoop.databinding.FragmentSolicitarDocumentoBinding
import com.code93.linkcoop.persistence.models.DataTransaccion

class DataSolicitarAdapter(
    var values: List<DataTransaccion>,
    private val callbackData: CallbackData
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
        holder.nameElemento.text = item.name

        if (item.value != null) {
            holder.tvValue.text = item.value
        }
        holder.lyNombre.setOnClickListener {
            callbackData.onClickData(item, position)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentSolicitarDocumentoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val lyNombre: LinearLayout = binding.lyNombre
        val nameElemento: TextView = binding.nameElemento
        val tvValue: TextView = binding.tvValue
    }

}