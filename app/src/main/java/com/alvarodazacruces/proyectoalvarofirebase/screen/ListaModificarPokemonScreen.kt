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
import com.alvarodazacruces.proyectoalvarofirebase.data.FirestorePokemonViewModel

@Composable
fun ListaModificarPokemonScreen(
    viewModel: FirestorePokemonViewModel,
    navController: NavController
) {
    // Obtenemos la lista de Pokémon en tiempo real
    val pokemonList = viewModel.pokemonList.collectAsState().value

    // Estado para el filtro de búsqueda
    var searchQuery by remember { mutableStateOf("") }

    // Filtramos los Pokémon según el nombre (sin importar mayúsculas o minúsculas)
    val filteredPokemonList = pokemonList.filter { pokemon ->
        pokemon.name.lowercase().contains(searchQuery.lowercase())
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Campo de búsqueda
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Buscar Pokémon") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Si la lista filtrada está vacía, mostramos un mensaje
        if (filteredPokemonList.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "No se encontraron Pokémon con ese nombre.")
            }
        } else {
            // Lista de Pokémon filtrados
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
                                // Navegamos a la pantalla de modificación pasando el id del Pokémon
                                navController.navigate("modify_pokemon_screen/${pokemon.id}")
                            },
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Usamos AsyncImage de Coil para cargar la imagen desde la API
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
