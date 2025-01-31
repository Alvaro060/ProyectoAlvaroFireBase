package com.alvarodazacruces.proyectoalvarofirebase.screen

import android.graphics.Color.rgb
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.alvarodazacruces.proyectoalvarofirebase.repositories.RemoteConnection
import com.alvarodazacruces.proyectoalvarofirebase.model.Pokemon

@Composable
fun DetallePokemonScreen(
    pokemonId: String,
    navigateToBack: () -> Unit
) {
    val pokemonState = remember { mutableStateOf<Pokemon?>(null) }

    LaunchedEffect(pokemonId) {
        val pokemon = RemoteConnection.RecibirPokemonPorId(pokemonId)
        pokemonState.value = pokemon
    }

    pokemonState.value?.let { pokemon ->
        Column(
            modifier = Modifier
                .fillMaxSize() // Para llenar toda la pantalla
                .padding(vertical = 16.dp) // Margen igual en la parte superior e inferior
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Nombre del Pokémon con margen superior
            Text(
                text = pokemon.name.capitalize(),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .padding(top = 195.dp) // Margen superior
                    .padding(bottom = 10.dp)
                    .background(Color(rgb(233, 206, 68)), shape = RoundedCornerShape(16.dp))
                    .padding(10.dp) // Agrega padding para la legibilidad
            )

            // Imagen del Pokémon
            AsyncImage(
                model = pokemon.sprites.other.officialArtwork.frontDefault,
                contentDescription = "Imagen de ${pokemon.name}",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(250.dp)
                    .padding(vertical = 16.dp) // Margen vertical alrededor de la imagen
            )

            // Tipo de Pokémon
            Text(
                text = "Tipo: ${pokemon.types.joinToString(", ") { it.type.name.capitalize() }}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .background(Color(rgb(233, 206, 68)), shape = RoundedCornerShape(16.dp))
                    .padding(10.dp) // Agrega padding para la legibilidad
            )

            // Habilidades
            Text(
                text = "Habilidades: ${pokemon.abilities.joinToString(", ") { it.ability.name.capitalize() }}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .background(Color(rgb(233, 206, 68)), shape = RoundedCornerShape(16.dp))
                    .padding(10.dp) // Agrega padding para la legibilidad
            )
        }
    }
}