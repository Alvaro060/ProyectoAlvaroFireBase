package com.alvarodazacruces.proyectoalvarofirebase.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.alvarodazacruces.proyectoalvarofirebase.data.FirestorePokemonViewModel

@Composable
fun EditarPokemonScreen(
    viewModel: FirestorePokemonViewModel,
    navController: NavController
) {
    var pokemonId by remember { mutableStateOf("") }
    var pokemonName by remember { mutableStateOf("") }
    var pokemonType by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Editar Pokémon")

        OutlinedTextField(
            value = pokemonId,
            onValueChange = { pokemonId = it },
            label = { Text("ID del Pokémon") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        )

        OutlinedTextField(
            value = pokemonName,
            onValueChange = { pokemonName = it },
            label = { Text("Nuevo Nombre del Pokémon") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        )

        OutlinedTextField(
            value = pokemonType,
            onValueChange = { pokemonType = it },
            label = { Text("Nuevo Tipo del Pokémon") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        )

        Button(
            onClick = {
                if (pokemonId.isNotEmpty() && pokemonName.isNotEmpty() && pokemonType.isNotEmpty()) {
                    viewModel.updatePokemon(pokemonId, pokemonName, pokemonType)
                    navController.popBackStack() // Volver a la pantalla anterior
                } else {
                    Toast.makeText(
                        navController.context,
                        "Por favor ingresa todos los campos",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        ) {
            Text(text = "Editar Pokémon")
        }
    }
}
