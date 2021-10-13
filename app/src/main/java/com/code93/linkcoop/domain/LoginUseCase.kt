package com.code93.linkcoop.domain

import com.code93.linkcoop.data.LinkCoopRepository

class LoginUseCase {

    private val repository = LinkCoopRepository()

    suspend operator fun invoke(loginXML: String): String = repository.getResponseLogin(loginXML)

}