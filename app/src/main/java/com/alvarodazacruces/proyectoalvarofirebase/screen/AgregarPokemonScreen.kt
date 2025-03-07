package com.alvarodazacruces.proyectoalvarofirebase.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.alvarodazacruces.proyectoalvarofirebase.data.FirestoreViewModel
import com.alvarodazacruces.proyectoalvarofirebase.model.PokemonBaseDatos
import kotlinx.coroutines.launch

@Composable
fun AgregarPokemonScreen(
    viewModel: FirestoreViewModel,
    navController: NavController
) {
    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }

    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Agregar Nuevo Pokémon",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                ),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = type,
                onValueChange = { type = it },
                label = { Text("Tipo") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (name.isNotEmpty() && type.isNotEmpty()) {
                        coroutineScope.launch {
                            val pokemon = PokemonBaseDatos(
                                id = "",
                                name = name,
                                type = type
                            )
                            viewModel.addPokemon(pokemon)
                            navController.popBackStack()
                        }
                    } else {
                        Toast.makeText(
                            navController.context,
                            "Por favor, complete todos los campos",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Agregar Pokémon",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            if (isLoading) {
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator()
            }

            error?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Error: $it",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
