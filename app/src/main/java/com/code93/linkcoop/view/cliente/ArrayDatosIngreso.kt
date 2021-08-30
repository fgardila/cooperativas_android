package com.code93.linkcoop.view.cliente

import android.text.InputType
import com.code93.linkcoop.R
import com.code93.linkcoop.persistence.models.DataTransaccion


val firstName = DataTransaccion(
    "Primer Nombre",
    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS,
    15,
    "Ingresa primer nombre del cliente",
    DataTransaccion.TipoDato.OTRO,
    R.drawable.ic_account
)
val lastName = DataTransaccion(
    "Primer Apellido",
    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS,
    15,
    "Ingresa nombre del cliente",
    DataTransaccion.TipoDato.OTRO,
    R.drawable.ic_account
)
val phoneNumber = DataTransaccion(
    "Numero de telefono",
    InputType.TYPE_CLASS_PHONE,
    15,
    "Ingresa nombre del cliente",
    DataTransaccion.TipoDato.OTRO,
    R.drawable.ic_phone
)
val document = DataTransaccion(
    "Documento de identidad",
    InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL,
    15,
    "Ingresa documento de identidad",
    DataTransaccion.TipoDato.CEDULA,
    R.drawable.ic_account
)
val emailAddress = DataTransaccion(
    "Correo Electronico",
    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS,
    30,
    "Ingresa correo electronico del cliente",
    DataTransaccion.TipoDato.EMAIL,
    R.drawable.ic_email
)


object ArrayDatosIngreso {
    val listDataTransaccion = mutableListOf(firstName, lastName, phoneNumber, document, emailAddress)
}