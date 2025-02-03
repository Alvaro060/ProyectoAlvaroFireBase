package com.alvarodazacruces.proyectoalvarofirebase.repositories

import com.alvarodazacruces.proyectoalvarofirebase.model.Pokemon
import com.alvarodazacruces.proyectoalvarofirebase.model.PokemonList
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RemoteConnection {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/") // URL base de la API de Pokémon
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val remoteService: RemoteService = retrofit.create(RemoteService::class.java)

    suspend fun RecibirPokemonPorId(pokemonId: String): Pokemon? {
        return try {
            val response = remoteService.getPokemonById(pokemonId)
            response
        } catch (e: Exception) {
            null
        }
    }
}