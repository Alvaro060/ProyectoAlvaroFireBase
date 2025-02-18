package com.alvarodazacruces.proyectoalvarofirebase.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alvarodazacruces.proyectoalvarofirebase.model.EntrenadorBaseDatos
import com.alvarodazacruces.proyectoalvarofirebase.model.PokemonBaseDatos
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FirestoreEntrenadorViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val entrenadorCollection = firestore.collection("entrenadores")
    private val pokemonCollection = firestore.collection("pokemons")  // Asumiendo que tienes esta colección

    // Estado de la lista de entrenadores
    private val _entrenadorList = MutableStateFlow<List<EntrenadorBaseDatos>>(emptyList())
    val entrenadorList: StateFlow<List<EntrenadorBaseDatos>> get() = _entrenadorList

    // Estado de carga (loading)
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    // Estado de error
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error

    init {
        fetchEntrenadoresList() // Inicializa el listener de la lista de entrenadores
    }

    // Usamos addSnapshotListener para obtener actualizaciones en tiempo real
    private fun fetchEntrenadoresList() {
        _isLoading.value = true
        _error.value = null

        entrenadorCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                _error.value = "Error al obtener datos de Firestore: ${error.message}"
                Log.e("Firestore", "Error al obtener datos de Firestore", error)
                _isLoading.value = false
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val entrenadoresList = snapshot.mapNotNull { document ->
                    try {
                        val entrenador = document.toObject(EntrenadorBaseDatos::class.java).apply {
                            id = document.id
                        }
                        // Limitar a máximo 3 pokemons
                        val limitedPokemons = entrenador.pokemons.take(3)
                        entrenador.copy(pokemons = limitedPokemons)
                    } catch (e: Exception) {
                        Log.e("Firestore", "Error al mapear documento", e)
                        null
                    }
                }
                _entrenadorList.value = entrenadoresList
            }

            _isLoading.value = false
        }
    }

    // Método para agregar un entrenador con Pokémon
    fun addEntrenador(entrenador: EntrenadorBaseDatos) {
        val entrenadorRef = entrenadorCollection.document() // Firestore generará un ID automáticamente

        // Establecemos el ID generado por Firestore en el objeto entrenador
        val entrenadorConId = entrenador.copy(id = entrenadorRef.id)

        // Guardamos el entrenador con su ID
        entrenadorRef.set(entrenadorConId)
            .addOnSuccessListener {
                Log.d("Firestore", "Entrenador agregado exitosamente")
                // No es necesario llamar a fetchEntrenadoresList() porque el listener se encargará de la actualización
            }
            .addOnFailureListener { e ->
                _error.value = "Error al agregar el entrenador: ${e.message}"
                Log.e("Firestore", "Error al agregar el entrenador", e)
            }
    }

    // Método para eliminar un entrenador
    fun deleteEntrenador(entrenador: EntrenadorBaseDatos) {
        if (entrenador.id.isNotEmpty()) {
            entrenadorCollection.document(entrenador.id).delete()
                .addOnSuccessListener {
                    Log.d("Firestore", "Entrenador eliminado correctamente")
                    // Recargar la lista de entrenadores después de la eliminación
                    fetchEntrenadoresList()
                }
                .addOnFailureListener { exception ->
                    Log.e("Firestore", "Error al eliminar el entrenador", exception)
                }
        }
    }

    // Método para actualizar los detalles de un entrenador en Firestore
    fun updateEntrenador(entrenadorId: String, entrenador: EntrenadorBaseDatos) {
        // Usamos el objeto EntrenadorBaseDatos directamente en lugar de crear un hashMap
        val entrenadorData = hashMapOf(
            "nombre" to entrenador.nombre,
            "pokemons" to entrenador.pokemons
        )

        // Actualizamos el documento en Firestore usando el ID del entrenador
        entrenadorCollection.document(entrenadorId)
            .set(entrenadorData)
            .addOnSuccessListener {
                Log.d("Firestore", "Entrenador actualizado exitosamente")
                // El listener se encargará de actualizar la lista en tiempo real
            }
            .addOnFailureListener { e ->
                _error.value = "Error al actualizar entrenador: ${e.message}"
                Log.e("Firestore", "Error al actualizar entrenador", e)
            }
    }

    fun getEntrenadorById(id: String): LiveData<EntrenadorBaseDatos?> {
        val entrenadorLiveData = MutableLiveData<EntrenadorBaseDatos?>()

        // Realizamos la consulta a Firestore para obtener el entrenador por su ID
        FirebaseFirestore.getInstance()
            .collection("entrenadores")
            .document(id)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val entrenador = document.toObject(EntrenadorBaseDatos::class.java)
                    entrenadorLiveData.value = entrenador
                } else {
                    entrenadorLiveData.value = null
                }
            }
            .addOnFailureListener {
                entrenadorLiveData.value = null
            }

        return entrenadorLiveData
    }

    fun getPokemonDetails(pokemonIds: List<String>, onResult: (List<PokemonBaseDatos>) -> Unit) {
        if (pokemonIds.isEmpty()) {
            onResult(emptyList())
            return
        }

        val pokemons = mutableListOf<PokemonBaseDatos>()

        for (id in pokemonIds) {
            firestore.collection("pokemons").document(id).get()
                .addOnSuccessListener { document ->
                    val pokemon = document.toObject(PokemonBaseDatos::class.java)
                    if (pokemon != null) {
                        pokemons.add(pokemon)
                    }
                    if (pokemons.size == pokemonIds.size) {
                        onResult(pokemons) // Retorna la lista completa cuando termine
                    }
                }
                .addOnFailureListener {
                    // Manejo de error si es necesario
                }
        }
    }


}
