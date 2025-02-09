package com.alvarodazacruces.proyectoalvarofirebase.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.alvarodazacruces.proyectoalvarofirebase.data.FirestorePokemonViewModel

@Composable
fun AgregarPokemonScreen(
    viewModel: FirestorePokemonViewModel,
    navController: NavController
) {
    // Campos de texto para ingresar datos
    var name = remember { mutableStateOf("") }
    var type = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Agregar Nuevo Pokémon")

        TextField(
            value = name.value,
            onValueChange = { name.value = it },
            label = { Text("Nombre") }
        )

        TextField(
            value = type.value,
            onValueChange = { type.value = it },
            label = { Text("Tipo") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (name.value.isNotEmpty() && type.value.isNotEmpty()) {
                    viewModel.addPokemon(name.value, type.value)
                    navController.popBackStack() // Regresar a la pantalla anterior
                } else {
                    Toast.makeText(navController.context, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
                }
            }
        ) {
            Text(text = "Agregar Pokémon")
        }
    }
}
