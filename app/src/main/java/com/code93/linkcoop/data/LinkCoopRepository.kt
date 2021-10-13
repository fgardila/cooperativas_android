package com.code93.linkcoop.data

import com.code93.linkcoop.data.network.LinkCoopService
import kotlin.math.log

class LinkCoopRepository {

    private val soap = LinkCoopService()

    suspend fun getResponseLogin(loginXML: String): String {
        return soap.getLogin(loginXML)
    }
}