package com.code93.linkcoop.models

data class LoginCooperativas(
        var comercio: Comercio = Comercio(),
        var instituciones: List<Instituciones>
        //var cooperativas: List<Cooperativa>
) {}