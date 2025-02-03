package com.alvarodazacruces.proyectoalvarofirebase.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.alvarodazacruces.proyectoalvarofirebase.data.PokemonListViewModel
import com.alvarodazacruces.proyectoalvarofirebase.model.Pokemon

@Composable
fun PokemonListScreen(
    viewModel: PokemonListViewModel,
    onNavigateToDetail: (String) -> Unit // Función para navegar a los detalles del Pokémon
) {
    val pokemonList by viewModel.pokemonList.collectAsState()
    val error by viewModel.error.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isLastPage by viewModel.isLastPage.collectAsState()
    val offset by viewModel.offset.collectAsState() // Accedemos al offset desde el ViewModel
    val limit = viewModel.limit // Accedemos al limit directamente

    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading && pokemonList.isEmpty()) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.primary
            )
        } else if (error != null) {
            Text(
                text = "Error: $error",
                color = Color.Red,
                modifier = Modifier.align(Alignment.Center)
            )
        } else if (pokemonList.isEmpty()) {
            Text(
                text = "No hay Pokémon disponibles",
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(pokemonList) { pokemon ->
                    PokemonListItem(
                        pokemon = pokemon,
                        onItemClick = { onNavigateToDetail(pokemon.name) }
                    )
                    Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
                }
                // Mostrar indicador de carga al final de la lista
                if (isLoading && !isLastPage) {
                    item {
                        Row(
                            modifier = Modifier.fillParentMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
            // Detectar cuándo el usuario llega al final de la lista
            LaunchedEffect(Unit) {
                snapshotFlow { pokemonList.size }
                    .collect { size ->
                        if (size >= offset - limit && !isLoading && !isLastPage) {
                            viewModel.loadMore()
                        }
                    }
            }
        }
    }
}

@Composable
fun PokemonListItem(
    pokemon: Pokemon,
    onItemClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onItemClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${pokemon.id}.png",
            contentDescription = "${pokemon.name} Official Artwork",
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = "#${String.format("%03d", pokemon.id)} ${pokemon.name.capitalize()}",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}