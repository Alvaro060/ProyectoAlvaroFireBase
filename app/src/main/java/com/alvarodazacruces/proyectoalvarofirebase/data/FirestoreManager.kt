package com.alvarodazacruces.proyectoalvarofirebase.data

import com.alvarodazacruces.proyectoalvarofirebase.model.EntrenadorBaseDatos
import com.alvarodazacruces.proyectoalvarofirebase.model.PokemonBaseDatos
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class FirestoreManager {
    private val firestore = FirebaseFirestore.getInstance()
    private val entrenadorCollection = firestore.collection("entrenadores")
    private val pokemonCollection = firestore.collection("pokemons")

    fun getEntrenadores(): Flow<List<EntrenadorBaseDatos>> {
        return entrenadorCollection.snapshots().map { snapshot ->
            snapshot.documents.mapNotNull { document ->
                document.toObject(EntrenadorBaseDatos::class.java)?.apply { id = document.id }
            }
        }
    }

    suspend fun addEntrenador(entrenador: EntrenadorBaseDatos) {
        entrenadorCollection.add(entrenador).await()
    }

    suspend fun updateEntrenador(entrenador: EntrenadorBaseDatos) {
        entrenador.id?.let { entrenadorCollection.document(it).set(entrenador).await() }
    }

    suspend fun deleteEntrenador(entrenadorId: String) {
        entrenadorCollection.document(entrenadorId).delete().await()
    }

    fun getPokemons(): Flow<List<PokemonBaseDatos>> {
        return pokemonCollection.snapshots().map { snapshot ->
            snapshot.documents.mapNotNull { document ->
                document.toObject(PokemonBaseDatos::class.java)?.apply { id = document.id }
            }
        }
    }

    suspend fun addPokemon(pokemon: PokemonBaseDatos) {
        pokemonCollection.add(pokemon).await()
    }

    suspend fun updatePokemon(pokemon: PokemonBaseDatos) {
        pokemon.id?.let { pokemonCollection.document(it).set(pokemon).await() }
    }

    suspend fun deletePokemon(pokemonId: String) {
        pokemonCollection.document(pokemonId).delete().await()
    }

    suspend fun getPokemonDetails(pokemonIds: List<String>): List<PokemonBaseDatos> {
        return pokemonCollection
            .whereIn("id", pokemonIds)
            .get()
            .await()
            .documents
            .mapNotNull { document ->
                document.toObject(PokemonBaseDatos::class.java)?.apply { id = document.id }
            }
    }

}