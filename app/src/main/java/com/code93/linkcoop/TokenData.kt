package com.code93.linkcoop

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class TokenData (var idToken: String? = null,
                 var lenToken: String? = null,
                 var dataToken: String? = null) : Parcelable {

    @IgnoredOnParcel
    var A0: String = ""
    @IgnoredOnParcel
    var A1: String = ""
    @IgnoredOnParcel
    var A2: String = ""
    @IgnoredOnParcel
    var A3: String = ""
    @IgnoredOnParcel
    var A4: String = ""
    @IgnoredOnParcel
    var A5: String = ""
    @IgnoredOnParcel
    var A6: String = ""
    @IgnoredOnParcel
    var A7: String = ""
    @IgnoredOnParcel
    var A8: String = ""
    @IgnoredOnParcel
    var A9: String = ""
    @IgnoredOnParcel
    var X1: String = ""
    @IgnoredOnParcel
    var X2: String = ""
    @IgnoredOnParcel
    var X3: String = ""
    @IgnoredOnParcel
    var X4: String = ""
    @IgnoredOnParcel
    var X5: String = ""
    @IgnoredOnParcel
    var X6: String = ""
    @IgnoredOnParcel
    var X7: String = ""
    @IgnoredOnParcel
    var X8: String = ""
    @IgnoredOnParcel
    var X9: String = ""
    @IgnoredOnParcel
    var B1: String = ""
    @IgnoredOnParcel
    var B2: String = ""
    @IgnoredOnParcel
    var B3: String = ""
    @IgnoredOnParcel
    var B4: String = ""
    @IgnoredOnParcel
    var B5: String = ""
    @IgnoredOnParcel
    var B6: String = ""
    @IgnoredOnParcel
    var B7: String = ""
    @IgnoredOnParcel
    var B8: String = ""
    @IgnoredOnParcel
    var B9: String = ""
    @IgnoredOnParcel
    var C1: String = ""
    @IgnoredOnParcel
    var C2: String = ""
    @IgnoredOnParcel
    var C3: String = ""
    @IgnoredOnParcel
    var C4: String = ""
    @IgnoredOnParcel
    var C5: String = ""
    @IgnoredOnParcel
    var C6: String = ""
    @IgnoredOnParcel
    var C7: String = ""
    @IgnoredOnParcel
    var C8: String = ""
    @IgnoredOnParcel
    var C9: String = ""
    @IgnoredOnParcel
    var D1: String = ""
    @IgnoredOnParcel
    var D2: String = ""
    @IgnoredOnParcel
    var D3: String = ""
    @IgnoredOnParcel
    var D4: String = ""
    @IgnoredOnParcel
    var D5: String = ""
    @IgnoredOnParcel
    var D6: String = ""
    @IgnoredOnParcel
    var D7: String = ""

    @IgnoredOnParcel
    var indice: Int = 0

    fun setToken(tokenId : String, tokenData : String) : String {
        val mLenToken = StringTools.padleft(tokenData.length.toString(), 3, '0')
        return tokenId + mLenToken + tokenData
    }

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