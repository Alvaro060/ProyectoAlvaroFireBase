package com.alvarodazacruces.proyectoalvarofirebase.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alvarodazacruces.proyectoalvarofirebase.model.Pokemon
import com.alvarodazacruces.proyectoalvarofirebase.repositories.repositoryList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PokemonListViewModel : ViewModel() {
    private val _pokemonList = MutableStateFlow<List<Pokemon>>(emptyList())
    val pokemonList: StateFlow<List<Pokemon>> = _pokemonList

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isLastPage = MutableStateFlow(false)
    val isLastPage: StateFlow<Boolean> = _isLastPage

    private val _offset = MutableStateFlow(0)
    val offset: StateFlow<Int> = _offset

    val limit = 20

    init {
        fetchPokemonList()
    }

    private fun fetchPokemonList() {
        if (_isLastPage.value) return

        viewModelScope.launch {
            try {
                _isLoading.value = true
                val newList = repositoryList.getListaPokemon(limit, _offset.value) // Usamos _offset.value aquí
                if (newList.isEmpty()) {
                    _isLastPage.value = true
                } else {
                    _pokemonList.value += newList
                    _offset.value += limit // Actualizamos el valor de _offset
                }
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.localizedMessage ?: "Error desconocido"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadMore() {
        if (!_isLoading.value && !_isLastPage.value) {
            fetchPokemonList()
        }
    }
}