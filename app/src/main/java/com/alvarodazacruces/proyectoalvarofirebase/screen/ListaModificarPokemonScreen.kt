package com.alvarodazacruces.proyectoalvarofirebase.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.alvarodazacruces.proyectoalvarofirebase.data.FirestoreViewModel

@Composable
fun ListaModificarPokemonScreen(
    viewModel: FirestoreViewModel,
    navController: NavController
) {
    LaunchedEffect(Unit) {
        viewModel.getPokemons()
    }

    val pokemonList by viewModel.pokemonList.collectAsState()

    var searchQuery by remember { mutableStateOf("") }

    val filteredPokemonList = pokemonList.filter { pokemon ->
        pokemon.name.lowercase().contains(searchQuery.lowercase())
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Buscar Pokémon") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (filteredPokemonList.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "No se encontraron Pokémon con ese nombre.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(filteredPokemonList) { pokemon ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable {
                                navController.navigate("modify_pokemon_screen/${pokemon.id}")
                            },
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = "https://img.pokemondb.net/artwork/large/${pokemon.name.lowercase()}.jpg",
                                contentDescription = "Imagen de ${pokemon.name}",
                                modifier = Modifier.size(80.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = "ID: ${pokemon.id}",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = "Nombre: ${pokemon.name}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "Tipo: ${pokemon.type}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
