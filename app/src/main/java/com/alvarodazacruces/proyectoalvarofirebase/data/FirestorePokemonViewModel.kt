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
        fetchPokemonList() // Inicializa el listener de la lista de Pokémon
    }

    // Usamos addSnapshotListener para obtener actualizaciones en tiempo real
    private fun fetchPokemonList() {
        _isLoading.value = true
        _error.value = null

        pokemonCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                _error.value = "Error al obtener datos de Firestore: ${error.message}"
                Log.e("Firestore", "Error al obtener datos de Firestore", error)
                _isLoading.value = false
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val pokemonList = snapshot.mapNotNull { document ->
                    try {
                        document.toObject(PokemonBaseDatos::class.java).apply {
                            id = document.id
                        }
                    } catch (e: Exception) {
                        Log.e("Firestore", "Error al mapear documento", e)
                        null
                    }
                }
                _pokemonList.value = pokemonList
            }
            _isLoading.value = false
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
                // No es necesario llamar a fetchPokemonList() porque el listener se encargará de la actualización
            }
            .addOnFailureListener { e ->
                _error.value = "Error al agregar Pokémon: ${e.message}"
                Log.e("Firestore", "Error al agregar Pokémon", e)
            }
    }

    // Método para eliminar un Pokémon de Firestore
    fun deletePokemon(pokemonId: String) {
        pokemonCollection.document(pokemonId)
            .delete()
            .addOnSuccessListener {
                Log.d("Firestore", "Pokémon eliminado exitosamente")
                // El listener actualizará la lista automáticamente
            }
            .addOnFailureListener { e ->
                _error.value = "Error al eliminar Pokémon: ${e.message}"
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
                // El listener se encargará de actualizar la lista en tiempo real
            }
            .addOnFailureListener { e ->
                _error.value = "Error al actualizar Pokémon: ${e.message}"
                Log.e("Firestore", "Error al actualizar Pokémon", e)
            }
    }
}
