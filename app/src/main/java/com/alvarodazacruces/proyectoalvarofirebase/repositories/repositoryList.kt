package com.alvarodazacruces.proyectoalvarofirebase.repositories

import com.alvarodazacruces.proyectoalvarofirebase.model.Pokemon
import com.alvarodazacruces.proyectoalvarofirebase.model.PokemonList
import com.alvarodazacruces.proyectoalvarofirebase.repositories.RemoteConnection

object repositoryList {

    // Obtener una lista de Pokémon
    suspend fun getListaPokemon(): List<Pokemon> {
        return try {
            val response = RemoteConnection.remoteService.getObtenerTodosPokemon()
            response.results // "results" es el campo que contiene la lista de Pokémon en PokemonList
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    // Obtener un Pokémon por su ID o nombre
    suspend fun getPokemonById(idPokemon: String): Pokemon? {
        return try {
            val response = RemoteConnection.remoteService.getPokemonById(idPokemon)
            response
        } catch (e: Exception) {
            e.printStackTrace()
            null // Devuelve null en caso de error
        }
    }
}
