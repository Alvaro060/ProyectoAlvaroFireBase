package com.alvarodazacruces.proyectoalvarofirebase.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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

    if (pokemonList.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "No hay Pokémon disponibles para modificar.")
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(pokemonList) { pokemon ->
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
