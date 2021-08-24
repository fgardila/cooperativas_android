package com.code93.linkcoop.ui

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.code93.linkcoop.DialogCallback
import com.code93.linkcoop.MyApp
import com.code93.linkcoop.R
import com.code93.linkcoop.Tools
import com.code93.linkcoop.adapters.MenuElementosAdapter
import com.code93.linkcoop.persistence.cache.SP2.Companion.aes_iv
import com.code93.linkcoop.persistence.cache.SP2.Companion.aes_password
import com.code93.linkcoop.persistence.cache.SP2.Companion.aes_salt
import com.code93.linkcoop.persistence.models.DataTransaccion
import java.util.*

class LlavesDeCifradoActivity : AppCompatActivity(), MenuElementosAdapter.OnClickElemetos {

    var adapter: MenuElementosAdapter? = null

    private val RTA_ELEMENTO = 101

    var elementos: Array<DataTransaccion?> = arrayOfNulls(0)
    var elemSelect: DataTransaccion? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_llaves_de_cifrado)

        val rvElementos = findViewById<RecyclerView>(R.id.rvElementos)

        elementos = getData()
        adapter = MenuElementosAdapter(this)
        adapter!!.setElementos(elementos.toList())
        adapter!!.setOnClickElemento(this)
        rvElementos.layoutManager = LinearLayoutManager(this)
        rvElementos.adapter = adapter

    }

    private fun getData(): Array<DataTransaccion?> {

        val aes_iv = MyApp.sp2.getString(aes_iv, "")
        val aes_password = MyApp.sp2.getString(aes_password, "")
        val aes_salt = MyApp.sp2.getString(aes_salt, "")

        val elementos: MutableList<DataTransaccion> = ArrayList()
        elementos.add(DataTransaccion(
                "AES IV",
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS,
                32, "Ingresa llave", R.drawable.ic_lock, aes_iv))
        elementos.add(DataTransaccion("AES Password",
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS,
                32, "Ingresa llave", R.drawable.ic_lock, aes_password))
        elementos.add(DataTransaccion("AES Salt",
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS,
                32, "Ingresa llave", R.drawable.ic_lock, aes_salt))
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

    fun guardarLlaves(view: View) {
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
                    "AES IV" -> {
                        MyApp.sp2.putString(aes_iv, data.value)
                        continue
                    }
                    "AES Password" -> {
                        MyApp.sp2.putString(aes_password, data.value)
                        continue
                    }
                    "AES Salt" -> {
                        MyApp.sp2.putString(aes_salt, data.value)
                    }
                }
            }

            Tools.showDialogPositive(this, "Cambio de llaves realizado exitosamente", object : DialogCallback{
                override fun onDialogCallback(value: Int) {
                    finish()
                }
            })
        }
    }

    override fun onItemClick(item: RecyclerView.ViewHolder?, position: Int, id: Int) {
        val intent = Intent(this, IngresarDataActivity::class.java)
        elemSelect = elementos[position]
        intent.putExtra("nameTrx", "Llaves de cifrado")
        intent.putExtra("elemento", elemSelect)
        startActivityForResult(intent, RTA_ELEMENTO)
    }
}