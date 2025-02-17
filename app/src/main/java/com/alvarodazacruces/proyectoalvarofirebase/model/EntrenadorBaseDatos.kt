package com.alvarodazacruces.proyectoalvarofirebase.model

data class EntrenadorBaseDatos(
    var id: String = "",
    val nombre: String = "",
    val pokemons: List<String> = listOf() // Lista de IDs de Pokémon
) {
    constructor() : this("", "", listOf())
}
