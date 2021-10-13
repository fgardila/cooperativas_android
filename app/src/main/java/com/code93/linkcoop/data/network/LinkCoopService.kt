package com.code93.linkcoop.data.network

import com.code93.linkcoop.LinkCoopApp
import com.code93.linkcoop.core.SoapHelper
import com.code93.linkcoop.core.logger.L
import com.code93.linkcoop.persistence.cache.SP2
import kotlinx.coroutines.*
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.SoapPrimitive
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpTransportSE
import kotlin.coroutines.CoroutineContext

class LinkCoopService: CoroutineScope {

    private val soapObject = SoapHelper.getSoapObject()

    val direccionip = LinkCoopApp.sp2!!.getString(SP2.net_direccionip, "0.0.0.0")
    val puerto = LinkCoopApp.sp2!!.getString(SP2.net_puerto, "9999")

    val SOAP_ACTION = "http://tempuri.org/iServiceAsynchronous/Invoque_Async_Service"
    val URL = "http://${direccionip}:${puerto}/AsynchronousService.svc"

    suspend fun getLogin(loginXML: String) : String {
        soapObject.addProperty("XmlRequest", loginXML)
        val soapEnvelope = SoapSerializationEnvelope(SoapEnvelope.VER11)
        soapEnvelope.dotNet = true
        soapEnvelope.setOutputSoapObject(soapObject)
        L.d("Entrando a withContext")
        return withContext(Dispatchers.IO) {
            try {
                val transport = HttpTransportSE(URL)
                kotlin.runCatching {
                    transport.call(SOAP_ACTION, soapEnvelope)
                }
                val resultString = soapEnvelope.response as SoapPrimitive
                L.d(resultString.toString())
                resultString.toString()
            }catch (e: Exception) {
                L.d("Error de conexion")
                "Error de conexion"
            }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = TODO("Not yet implemented")
}