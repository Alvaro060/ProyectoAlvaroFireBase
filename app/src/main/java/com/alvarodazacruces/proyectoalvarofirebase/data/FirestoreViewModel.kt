package com.alvarodazacruces.proyectoalvarofirebase.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.alvarodazacruces.proyectoalvarofirebase.model.EntrenadorBaseDatos
import com.alvarodazacruces.proyectoalvarofirebase.model.PokemonBaseDatos
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FirestoreViewModel(private val firestoreManager: FirestoreManager) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error

    private val _entrenadorList = MutableStateFlow<List<EntrenadorBaseDatos>>(emptyList())
    val entrenadorList: StateFlow<List<EntrenadorBaseDatos>> get() = _entrenadorList

    private val _pokemonList = MutableStateFlow<List<PokemonBaseDatos>>(emptyList())
    val pokemonList: StateFlow<List<PokemonBaseDatos>> get() = _pokemonList

    private val _entrenador = MutableStateFlow<EntrenadorBaseDatos?>(null)
    val entrenador: StateFlow<EntrenadorBaseDatos?> get() = _entrenador

    fun getEntrenadores() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                firestoreManager.getEntrenadores().collect { entrenadores ->
                    _entrenadorList.value = entrenadores
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getPokemons() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                firestoreManager.getPokemons().collect { pokemons ->
                    _pokemonList.value = pokemons
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addEntrenador(entrenador: EntrenadorBaseDatos) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                firestoreManager.addEntrenador(entrenador)
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e.message
                _isLoading.value = false
            }
        }
    }

    fun updateEntrenador(entrenador: EntrenadorBaseDatos) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                firestoreManager.updateEntrenador(entrenador)
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e.message
                _isLoading.value = false
            }
        }
    }

    fun deleteEntrenador(entrenadorId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                firestoreManager.deleteEntrenador(entrenadorId)
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e.message
                _isLoading.value = false
            }
        }
    }

    fun addPokemon(pokemon: PokemonBaseDatos) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                firestoreManager.addPokemon(pokemon)
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e.message
                _isLoading.value = false
            }
        }
    }

    fun updatePokemon(pokemon: PokemonBaseDatos) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                firestoreManager.updatePokemon(pokemon)
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e.message
                _isLoading.value = false
            }
        }
    }

    fun deletePokemon(pokemonId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                firestoreManager.deletePokemon(pokemonId)
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e.message
                _isLoading.value = false
            }
        }
    }

    fun getPokemonDetails(pokemonIds: List<String>) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val pokemons = firestoreManager.getPokemonDetails(pokemonIds)
                _pokemonList.value = pokemons
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Factory para crear el ViewModel
    class FirestoreViewModelFactory(
        private val firestoreManager: FirestoreManager
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FirestoreViewModel(firestoreManager) as T
        }
    }

}