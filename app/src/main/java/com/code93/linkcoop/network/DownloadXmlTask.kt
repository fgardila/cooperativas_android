package com.code93.linkcoop.network

import android.os.AsyncTask
import android.util.Log
import com.code93.linkcoop.MyApp
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.SoapObject
import org.ksoap2.serialization.SoapPrimitive
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpTransportSE
import java.io.IOException

class DownloadXmlTask(var xmlSend: String, var callback: DownloadCallback) : AsyncTask<String, Void, String>() {

    var resultString: SoapPrimitive? = null

    override fun doInBackground(vararg params: String?): String {
        return try {
            sendRequest(params[0]!!)
        } catch (e: IOException) {
            "Error de conexion"
        }
    }

    override fun onPostExecute(result: String?) {
        Log.d("XML RECIVE", result!!)
        callback.onDownloadCallback(result)
    }

    @Throws(IOException::class)
    private fun sendRequest(xmlSend: String) : String {
        MyApp.sp2.incTraceNo()
        val SOAP_ACTION = "http://tempuri.org/iServiceAsynchronous/Invoque_Async_Service"
        val METHOD_NAME = "Invoque_Async_Service"
        val NAMESPACE = "http://tempuri.org/"
        val URL = "http://190.216.106.14:1992/AsynchronousService.svc"

        val Request = SoapObject(NAMESPACE, METHOD_NAME)
        Request.addProperty("XmlRequest", xmlSend)
        val soapEnvelope = SoapSerializationEnvelope(SoapEnvelope.VER11)
        soapEnvelope.dotNet = true
        soapEnvelope.setOutputSoapObject(Request)
        val transport = HttpTransportSE(URL)
        transport.call(SOAP_ACTION, soapEnvelope)
        val resultString = soapEnvelope.response as SoapPrimitive
        return resultString.toString()
    }
}