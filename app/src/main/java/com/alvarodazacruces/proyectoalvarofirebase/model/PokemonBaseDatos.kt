package com.alvarodazacruces.proyectoalvarofirebase.model

data class PokemonBaseDatos(
    var id: String = "",
    val name: String = "",
    val type: String = "",
) {
    constructor() : this("", "")
}
