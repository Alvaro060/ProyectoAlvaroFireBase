package com.alvarodazacruces.proyectoalvarofirebase.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alvarodazacruces.proyectoalvarofirebase.model.Pokemon
import com.alvarodazacruces.proyectoalvarofirebase.repositories.repositoryList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PokemonSearchViewModel : ViewModel() {
    // Estado del Pokémon encontrado (permite valores nulos)
    private val _pokemon = MutableStateFlow<Pokemon?>(null)
    val pokemon: StateFlow<Pokemon?> = _pokemon

    // Estado de error (permite valores nulos)
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Estado de carga
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    /**
     * Busca un Pokémon por su ID o nombre.
     */
    fun searchPokemon(idPokemon: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val pokemon = repositoryList.getPokemonById(idPokemon)
                _pokemon.value = pokemon
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Error: ${e.message ?: "Desconocido"}"
                _pokemon.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Limpia el estado del ViewModel.
     * Esto es útil cuando el usuario cierra sesión para evitar datos residuales.
     */
    fun clearState() {
        _pokemon.value = null
        _error.value = null
        _isLoading.value = false
    }
}