package com.alvarodazacruces.proyectoalvarofirebase.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.alvarodazacruces.proyectoalvarofirebase.data.FirestoreViewModel

@Composable
fun SeleccionarPokemonAgregarEntrenadorScreen(
    pokemonViewModel: FirestoreViewModel,
    navController: NavController
) {

    LaunchedEffect(Unit) {
        pokemonViewModel.getPokemons()
    }

    val pokemonList by pokemonViewModel.pokemonList.collectAsState(initial = emptyList())

    var searchQuery by remember { mutableStateOf("") }

    val filteredPokemonList = pokemonList.filter { pokemon ->
        pokemon.name.lowercase().contains(searchQuery.lowercase())
    }

    if (filteredPokemonList.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("No hay Pokémon disponibles o coinciden con la búsqueda.")
        }
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize()) {

            item {
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Buscar Pokémon") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    singleLine = true
                )
            }

            items(filteredPokemonList) { pokemon ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                        .clickable {
                            navController.previousBackStackEntry?.savedStateHandle?.set("selectedPokemon", pokemon.id)
                            navController.popBackStack()
                        },
                    elevation = CardDefaults.cardElevation(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = "https://img.pokemondb.net/artwork/large/${pokemon.name.lowercase()}.jpg",
                            contentDescription = "Imagen de ${pokemon.name}",
                            modifier = Modifier.size(100.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Nombre: ${pokemon.name.capitalize()}",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Tipo: ${pokemon.type}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }
        }
    }
}
