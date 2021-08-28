package com.code93.linkcoop.persistence.cache

import com.code93.linkcoop.persistence.models.ClienteData
import com.code93.linkcoop.persistence.models.Cooperativa
import com.code93.linkcoop.persistence.models.Transaction

object DataTrans {
    var transaction: Transaction = Transaction()
    var cooperativa: Cooperativa = Cooperativa(_transaction = arrayListOf())
    var clienteData: ClienteData = ClienteData()
}