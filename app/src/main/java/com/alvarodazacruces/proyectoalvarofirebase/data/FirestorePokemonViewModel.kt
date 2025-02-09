package com.alvarodazacruces.proyectoalvarofirebase.data

import android.util.Log
import androidx.lifecycle.ViewModel
import com.alvarodazacruces.proyectoalvarofirebase.model.PokemonBaseDatos
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FirestorePokemonViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val pokemonCollection = firestore.collection("pokemons")

    // Estado de la lista de Pokémon
    private val _pokemonList = MutableStateFlow<List<PokemonBaseDatos>>(emptyList())
    val pokemonList: StateFlow<List<PokemonBaseDatos>> get() = _pokemonList

    // Estado de carga (loading)
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    // Estado de error
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error

    init {
        fetchPokemonList() // Inicializa la lista de Pokémon
    }

    // Método para obtener la lista de Pokémon desde Firestore
    private fun fetchPokemonList() {
        _isLoading.value = true // Se empieza a cargar
        _error.value = null // Limpiamos cualquier error previo

        pokemonCollection.get()
            .addOnSuccessListener { result ->
                val pokemonList = result.mapNotNull { document ->
                    try {
                        document.toObject(PokemonBaseDatos::class.java).apply {
                            id = document.id // Asignamos el id del documento
                        }
                    } catch (e: Exception) {
                        Log.e("Firestore", "Error al mapear documento", e)
                        null // En caso de error, no agregamos el Pokémon
                    }
                }
                _pokemonList.value = pokemonList // Actualizamos la lista de Pokémon
            }
            .addOnFailureListener { e ->
                _error.value = "Error al obtener datos de Firestore: ${e.message}" // Actualizamos el error
                Log.e("Firestore", "Error al obtener datos de Firestore", e)
            }
            .addOnCompleteListener {
                _isLoading.value = false // Terminamos el proceso de carga
            }
    }

    // Método para agregar un Pokémon a la base de datos de Firestore
    fun addPokemon(name: String, type: String) {
        val newPokemon = hashMapOf(
            "name" to name,
            "type" to type
        )

        pokemonCollection.add(newPokemon)
            .addOnSuccessListener {
                Log.d("Firestore", "Pokémon agregado exitosamente")
                fetchPokemonList() // Actualizamos la lista de Pokémon después de agregarlo
            }
            .addOnFailureListener { e ->
                _error.value = "Error al agregar Pokémon: ${e.message}" // Actualizamos el error
                Log.e("Firestore", "Error al agregar Pokémon", e)
            }
    }

    // Método para eliminar un Pokémon de Firestore
    fun deletePokemon(pokemonId: String) {
        pokemonCollection.document(pokemonId)
            .delete()
            .addOnSuccessListener {
                Log.d("Firestore", "Pokémon eliminado exitosamente")
                fetchPokemonList() // Actualizamos la lista de Pokémon después de eliminarlo
            }
            .addOnFailureListener { e ->
                _error.value = "Error al eliminar Pokémon: ${e.message}" // Actualizamos el error
                Log.e("Firestore", "Error al eliminar Pokémon", e)
            }
    }

    // Método para actualizar los detalles de un Pokémon en Firestore
    fun updatePokemon(pokemonId: String, name: String, type: String) {
        val pokemonData = hashMapOf(
            "name" to name,
            "type" to type
        )

        pokemonCollection.document(pokemonId)
            .set(pokemonData)
            .addOnSuccessListener {
                Log.d("Firestore", "Pokémon actualizado exitosamente")
                fetchPokemonList() // Actualizamos la lista después de la edición
            }
            .addOnFailureListener { e ->
                _error.value = "Error al actualizar Pokémon: ${e.message}" // Actualizamos el error
                Log.e("Firestore", "Error al actualizar Pokémon", e)
            }
    }
}
