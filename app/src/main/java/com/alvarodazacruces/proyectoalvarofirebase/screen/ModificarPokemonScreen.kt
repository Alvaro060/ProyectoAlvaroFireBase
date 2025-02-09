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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.alvarodazacruces.proyectoalvarofirebase.data.FirestorePokemonViewModel
import com.alvarodazacruces.proyectoalvarofirebase.model.PokemonBaseDatos

@Composable
fun ModificarPokemonScreen(
    viewModel: FirestorePokemonViewModel,
    navController: NavController
) {
    // Estado para los campos de texto
    val name = remember { mutableStateOf("") }
    val type = remember { mutableStateOf("") }

    // Obtenemos el primer Pokémon disponible desde el ViewModel
    val pokemon = viewModel.pokemonList.collectAsState().value.firstOrNull()

    // Aseguramos que los datos del Pokémon estén cargados
    LaunchedEffect(pokemon) {
        pokemon?.let {
            name.value = it.name
            type.value = it.type
        }
    }

    // Column para los elementos de la UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título
        Text(text = "Modificar Pokémon")

        // Verificamos si existe un Pokémon cargado
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

            // Botón para modificar el Pokémon
            Button(
                onClick = {
                    if (name.value.isNotEmpty() && type.value.isNotEmpty()) {
                        // Modificamos el Pokémon utilizando su ID
                        viewModel.updatePokemon(pokemon.id, name.value, type.value)
                        navController.popBackStack() // Navegar hacia atrás después de la modificación
                    } else {
                        Toast.makeText(navController.context, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
                    }
                }
            ) {
                Text(text = "Modificar Pokémon")
            }
        } else {
            // Si no hay Pokémon cargado, mostramos un mensaje
            Text(text = "No hay Pokémon disponibles para modificar.")
        }
    }
}
