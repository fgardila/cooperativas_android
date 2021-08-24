package com.code93.linkcoop.persistence.models

data class LoginCooperativas(
        var comercio: Comercio = Comercio(),
        var cooperativas: List<Cooperativa>
) {}