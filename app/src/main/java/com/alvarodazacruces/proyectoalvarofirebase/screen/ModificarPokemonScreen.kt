package com.alvarodazacruces.proyectoalvarofirebase.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.alvarodazacruces.proyectoalvarofirebase.data.FirestorePokemonViewModel

@Composable
fun ModificarPokemonScreen(
    viewModel: FirestorePokemonViewModel,
    navController: NavController,
    pokemonId: String
) {
    // Estados para los campos de texto
    val name = remember { mutableStateOf("") }
    val type = remember { mutableStateOf("") }

    // Buscamos el Pokémon cuyo id coincida con el pokemonId recibido
    val pokemon = viewModel.pokemonList.collectAsState().value.firstOrNull { it.id == pokemonId }

    // Actualizamos los valores de los campos si se encuentra el Pokémon
    LaunchedEffect(pokemon) {
        pokemon?.let {
            name.value = it.name
            type.value = it.type
        }
    }

    // Obtenemos el contexto para mostrar Toasts
    val context = LocalContext.current

    // UI de la pantalla
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título de la pantalla
        Text(text = "Modificar Pokémon")

        // Verificamos si el Pokémon existe
        if (pokemon != null) {
            // Mostramos el ID del Pokémon
            Text(text = "ID: ${pokemon.id}", style = MaterialTheme.typography.titleMedium)

            // Campo para el nombre
            TextField(
                value = name.value,
                onValueChange = { name.value = it },
                label = { Text("Nombre") }
            )

            // Campo para el tipo
            TextField(
                value = type.value,
                onValueChange = { type.value = it },
                label = { Text("Tipo") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para actualizar el Pokémon
            Button(
                onClick = {
                    if (name.value.isNotEmpty() && type.value.isNotEmpty()) {
                        viewModel.updatePokemon(pokemon.id, name.value, type.value)
                        navController.popBackStack()
                    } else {
                        Toast.makeText(context, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
                    }
                }
            ) {
                Text(text = "Modificar Pokémon")
            }
        } else {
            // Si no se encuentra el Pokémon, se muestra un mensaje
            Text(text = "No se encontró el Pokémon con el ID especificado.")
        }
    }
}
