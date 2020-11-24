package com.code93.linkcoop

class TokenData (var idToken: String? = null,
                 var lenToken: String? = null,
                 var dataToken: String? = null) {


    var indice: Int = 0

    fun getTokens(tokenData: String) : List<TokenData> {
        val listTokens = mutableListOf<TokenData>()
        indice = 0
        var i = 0
        while (i < tokenData.length) {
            val mIdToken = tokenData.substring(indice, 2.let { indice += it; indice })
            val mLenToken = tokenData.substring(indice, 3.let { indice += it; indice })
            val len = mLenToken.toInt()
            val mDataToken = tokenData.substring(indice, len.let { indice += it; indice })
            listTokens.add(TokenData(mIdToken, mLenToken, mDataToken))
            i = indice
        }
        return listTokens
    }
}