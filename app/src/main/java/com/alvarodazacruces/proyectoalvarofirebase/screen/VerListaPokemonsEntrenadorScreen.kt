package com.alvarodazacruces.proyectoalvarofirebase.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.alvarodazacruces.proyectoalvarofirebase.data.FirestoreViewModel

@Composable
fun VerListaPokemonsEntrenadorScreen(
    entrenadorId: String,
    viewModel: FirestoreViewModel,
    navController: NavController
) {
    val entrenadorList = viewModel.entrenadorList.collectAsState().value
    val entrenador = entrenadorList.find { it.id == entrenadorId }

    val pokemonList by viewModel.pokemonList.collectAsState()

    LaunchedEffect(entrenador?.pokemons) {
        entrenador?.let {
            viewModel.getPokemonDetails(it.pokemons)
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Pokémon de ${entrenador?.nombre ?: "Desconocido"}",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }

        if (entrenador == null || pokemonList.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Este entrenador no tiene Pokémon registrados.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                items(pokemonList) { pokemon ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
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
