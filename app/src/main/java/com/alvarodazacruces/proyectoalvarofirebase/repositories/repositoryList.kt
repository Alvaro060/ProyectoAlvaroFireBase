package com.alvarodazacruces.proyectoalvarofirebase.repositories

import com.alvarodazacruces.proyectoalvarofirebase.model.OfficialArtwork
import com.alvarodazacruces.proyectoalvarofirebase.model.OtherSprites
import com.alvarodazacruces.proyectoalvarofirebase.model.Pokemon
import com.alvarodazacruces.proyectoalvarofirebase.model.PokemonSprites

object repositoryList {

    // Obtener una lista de Pokémon
    suspend fun getListaPokemon(limit: Int = 20, offset: Int = 0): List<Pokemon> {
        return try {
            val response = RemoteConnection.remoteService.getObtenerTodosPokemon(limit, offset)
            response.results.mapIndexed { index, pokemon ->
                Pokemon(
                    id = index + 1 + offset,
                    name = pokemon.name,
                    types = emptyList(),
                    abilities = emptyList(),
                    sprites = PokemonSprites(
                        other = OtherSprites(
                            officialArtwork = OfficialArtwork(frontDefault = "")
                        )
                    )
                )
            }
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
