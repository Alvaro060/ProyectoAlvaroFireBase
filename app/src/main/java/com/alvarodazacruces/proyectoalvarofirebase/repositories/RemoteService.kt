package com.alvarodazacruces.proyectoalvarofirebase.repositories

import com.alvarodazacruces.proyectoalvarofirebase.model.Pokemon
import com.alvarodazacruces.proyectoalvarofirebase.model.PokemonList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RemoteService {

    // Obtener todos los Pokémon (con un query opcional)
    @GET("pokemon")
    suspend fun getObtenerTodosPokemon(@Query("limit") limit: Int = 20, @Query("offset") offset: Int = 0): PokemonList

    // Obtener un Pokémon por su ID o nombre
    @GET("pokemon/{id}")
    suspend fun getPokemonById(@Path("id") pokemonId: String): Pokemon
}