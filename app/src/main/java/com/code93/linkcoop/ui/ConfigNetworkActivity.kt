package com.code93.linkcoop.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.code93.linkcoop.DialogCallback
import com.code93.linkcoop.MyApp
import com.code93.linkcoop.R
import com.code93.linkcoop.Tools
import com.code93.linkcoop.adapters.MenuElementosAdapter
import com.code93.linkcoop.cache.SP2
import com.code93.linkcoop.models.DataTransaccion
import java.util.ArrayList

class ConfigNetworkActivity : AppCompatActivity(), MenuElementosAdapter.OnClickElemetos {

    var adapter: MenuElementosAdapter? = null

    private val RTA_ELEMENTO = 101

    var elementos: Array<DataTransaccion?> = arrayOfNulls(0)
    var elemSelect: DataTransaccion? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config_network)

        val rvElementos = findViewById<RecyclerView>(R.id.rvElementos)

        elementos = getData()
        adapter = MenuElementosAdapter(this)
        adapter!!.setElementos(elementos.toList())
        adapter!!.setOnClickElemento(this)
        rvElementos.layoutManager = LinearLayoutManager(this)
        rvElementos.adapter = adapter
    }

    private fun getData(): Array<DataTransaccion?> {

        val net_direccionip = MyApp.sp2.getString(SP2.net_direccionip, "")
        val net_puerto = MyApp.sp2.getString(SP2.net_puerto, "")

        val elementos: MutableList<DataTransaccion> = ArrayList()
        elementos.add(DataTransaccion(
                getString(R.string.url_o_direccion_ip),
                InputType.TYPE_CLASS_TEXT,
                32, "URL o dirección IP", R.drawable.ic_network_check, net_direccionip))
        elementos.add(DataTransaccion(
                getString(R.string.puerto),
                InputType.TYPE_CLASS_NUMBER,
                5, "Puerto", R.drawable.ic_network_check, net_puerto))
        return elementos.toTypedArray()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RTA_ELEMENTO) {
            val pos: Int = elementos.indexOf(elemSelect)
            var value: String? = null
            if (data != null) {
                value = data.getStringExtra("value")
            }
            elementos[pos] = DataTransaccion(elemSelect, value)
            adapter!!.setElementos(elementos.toList())
            adapter!!.notifyDataSetChanged()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onItemClick(item: RecyclerView.ViewHolder?, position: Int, id: Int) {
        val intent = Intent(this, IngresarDataActivity::class.java)
        elemSelect = elementos[position]
        intent.putExtra("nameTrx", "Llaves de cifrado")
        intent.putExtra("elemento", elemSelect)
        startActivityForResult(intent, RTA_ELEMENTO)
    }

    fun guardarConfiguracion(view: View) {
        var camposOk = true
        for (data in elementos) {
            if (data!!.value == null || data.value == "") {
                Toast.makeText(this, "El " + data.name.trim { it <= ' ' } + " no se ha completado.",
                        Toast.LENGTH_SHORT).show()
                camposOk = false
                break
            }
        }
        if (camposOk) {
            for (data in elementos) {
                when (data!!.name) {
                    getString(R.string.url_o_direccion_ip) -> {
                        MyApp.sp2.putString(SP2.net_direccionip, data.value)
                        continue
                    }
                    getString(R.string.puerto) -> {
                        MyApp.sp2.putString(SP2.net_puerto, data.value)
                    }
                }
            }

            Tools.showDialogPositive(this, "Cambio de llaves realizado exitosamente", object : DialogCallback {
                override fun onDialogCallback(value: Int) {
                    finish()
                }
            })
        }
    }
}