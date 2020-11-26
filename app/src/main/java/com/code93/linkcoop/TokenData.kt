package com.code93.linkcoop

import com.code93.linkcoop.xmlParsers.XmlParser

class TokenData (var idToken: String? = null,
                 var lenToken: String? = null,
                 var dataToken: String? = null) {

    var A0: String = ""
    var A1: String = ""
    var A2: String = ""
    var A3: String = ""
    var A4: String = ""
    var A5: String = ""
    var A6: String = ""
    var A7: String = ""
    var A8: String = ""
    var A9: String = ""
    var X1: String = ""
    var X2: String = ""
    var X3: String = ""
    var X4: String = ""
    var X5: String = ""
    var X6: String = ""
    var X7: String = ""
    var X8: String = ""
    var X9: String = ""
    var B1: String = ""
    var B2: String = ""
    var B3: String = ""
    var B4: String = ""
    var B5: String = ""
    var B6: String = ""
    var B7: String = ""
    var B8: String = ""
    var B9: String = ""
    var C1: String = ""
    var C2: String = ""
    var C3: String = ""
    var C4: String = ""
    var C5: String = ""
    var C6: String = ""
    var C7: String = ""
    var C8: String = ""
    var C9: String = ""
    var D1: String = ""
    var D2: String = ""
    var D3: String = ""
    var D4: String = ""
    var D5: String = ""
    var D6: String = ""
    var D7: String = ""

    var indice: Int = 0

    fun getTokens(tokenData: String) {
        indice = 0
        var i = 0
        while (i < tokenData.length) {
            val mIdToken = tokenData.substring(indice, 2.let { indice += it; indice })
            val mLenToken = tokenData.substring(indice, 3.let { indice += it; indice })
            val len = mLenToken.toInt()
            val mDataToken = tokenData.substring(indice, len.let { indice += it; indice })
            when (mIdToken) {
                "A0" -> A0 = mDataToken
                "A1" -> A1 = mDataToken
                "A2" -> A2 = mDataToken
                "A3" -> A3 = mDataToken
                "A4" -> A4 = mDataToken
                "A5" -> A5 = mDataToken
                "A6" -> A6 = mDataToken
                "A7" -> A7 = mDataToken
                "A8" -> A8 = mDataToken
                "X1" -> X1 = mDataToken
                "X2" -> X2 = mDataToken
                "X3" -> X3 = mDataToken
                "X4" -> X4 = mDataToken
                "X5" -> X5 = mDataToken
                "X6" -> X6 = mDataToken
                "X7" -> X7 = mDataToken
                "X8" -> X8 = mDataToken
                "X9" -> X9 = mDataToken
                "B1" -> B1 = mDataToken
                "B2" -> B2 = mDataToken
                "B3" -> B3 = mDataToken
                "B4" -> B4 = mDataToken
                "B5" -> B5 = mDataToken
                "B6" -> B6 = mDataToken
                "B7" -> B7 = mDataToken
                "B8" -> B8 = mDataToken
                "B9" -> B9 = mDataToken
                "C1" -> C1 = mDataToken
                "C2" -> C2 = mDataToken
                "C3" -> C3 = mDataToken
                "C4" -> C4 = mDataToken
                "C5" -> C5 = mDataToken
                "C6" -> C6 = mDataToken
                "C7" -> C7 = mDataToken
                "C8" -> C8 = mDataToken
                "D1" -> D1 = mDataToken
                "D2" -> D2 = mDataToken
                "D3" -> D3 = mDataToken
                "D4" -> D4 = mDataToken
                "D5" -> D5 = mDataToken
                "D6" -> D6 = mDataToken
                "D7" -> D7 = mDataToken
            }
            i = indice
        }
    }
}