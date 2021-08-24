package com.code93.linkcoop.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.code93.linkcoop.R
import com.code93.linkcoop.persistence.models.LogTransacciones
import java.util.*

class ReportesAdapter(val context: Context, val lister : ReportesCallback) : RecyclerView.Adapter<ReportesAdapter.ViewHolder>() {

    var logs: List<LogTransacciones> = ArrayList()

    fun setListLogs(listLogs : List<LogTransacciones>) {
        logs = listLogs.reversed()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgInstitucion = itemView.findViewById<ImageView>(R.id.imgInstitucion)
        val tvTituloTrx = itemView.findViewById<TextView>(R.id.tvTituloTrx)
        val tvMontoTrx = itemView.findViewById<TextView>(R.id.tvMontoTrx)
        val tvFechaTrx = itemView.findViewById<TextView>(R.id.tvFechaTrx)
        val tvCodigoTrx = itemView.findViewById<TextView>(R.id.tvCodigoTrx)
        val btnReimpresion = itemView.findViewById<Button>(R.id.btnReimpresion)
        val btnDetalles = itemView.findViewById<Button>(R.id.btnDetalles)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_reportes, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val logTrx = logs[position]
        holder.tvTituloTrx.text = logTrx.transaction._namet
        holder.tvMontoTrx.text = logTrx.fieldsTrxSend.transaction_amount
        holder.tvFechaTrx.text = logTrx.fieldsTrxResponse.switch_date_time
        holder.tvCodigoTrx.text = logTrx.fieldsTrxResponse.authorization_code
        holder.btnReimpresion.setOnClickListener {
            lister.onReporteCallback(true, logTrx)
        }
    }

    override fun getItemCount(): Int {
        return logs.size
    }
}