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
import com.alvarodazacruces.proyectoalvarofirebase.model.EntrenadorBaseDatos
import kotlinx.coroutines.launch

@Composable
fun ModificarEntrenadorScreen(
    viewModel: FirestoreViewModel,
    entrenadorId: String,
    navController: NavController,
) {
    var name by remember { mutableStateOf("") }
    var selectedPokemonId by remember { mutableStateOf<String?>(null) }

    navController.currentBackStackEntry?.savedStateHandle?.get<String>("selectedPokemon")?.let {
        selectedPokemonId = it
    }

    val coroutineScope = rememberCoroutineScope()

    val entrenador by viewModel.entrenador.collectAsState(initial = null)

    LaunchedEffect(entrenador) {
        entrenador?.let {
            name = it.nombre
        }
    }

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
                text = "Modificar Entrenador",
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

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    navController.navigate("seleccionar_pokemon_agregar_entrenador_screen")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Agregar Pokémon al Entrenador",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (name.isNotEmpty() && selectedPokemonId != null) {
                        coroutineScope.launch {
                            val entrenador = EntrenadorBaseDatos(
                                id = entrenadorId,
                                nombre = name,
                                pokemons = selectedPokemonId?.let { listOf(it) } ?: listOf()
                            )

                            viewModel.updateEntrenador(entrenador)

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
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedPokemonId != null
            ) {
                Text(
                    text = "Modificar Entrenador",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}
