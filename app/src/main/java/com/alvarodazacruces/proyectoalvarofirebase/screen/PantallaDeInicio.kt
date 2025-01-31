package com.alvarodazacruces.proyectoalvarofirebase.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

@Composable
fun PantallaDeInicio(
    viewModel: PokemonSearchViewModel,
    onNavigateToDetail: (String) -> Unit
) {
    val pokemon by viewModel.pokemon.collectAsState()
    val error by viewModel.error.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var inputText by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center // Centra todo el contenido en la pantalla
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 195.dp, bottom = 10.dp) // Mantener los márgenes superior e inferior
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
                contentDescription = "Logo",
                modifier = Modifier.size(200.dp)
            )

            Text(
                text = "Busca tu Pokémon",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            TextField(
                value = inputText,
                onValueChange = { inputText = it },
                label = { Text("Nombre o ID del Pokémon") },
                modifier = Modifier.fillMaxWidth()
            )

            if (showError) {
                Text(
                    text = "¡Debes ingresar un nombre!",
                    color = Color.Red,
                    modifier = Modifier.padding(8.dp)
                )
            }

            Button(
                onClick = {
                    if (inputText.isBlank()) {
                        showError = true
                    } else {
                        showError = false
                        viewModel.searchPokemon(inputText.lowercase().trim())
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text("Buscar Pokémon")
            }

            when {
                isLoading -> CircularProgressIndicator()
                error != null -> Text("Error: $error", color = Color.Red)
                pokemon != null -> {
                    LaunchedEffect(pokemon) {
                        pokemon?.name?.let { onNavigateToDetail(it) }
                    }
                }
            }
        }
    }
}