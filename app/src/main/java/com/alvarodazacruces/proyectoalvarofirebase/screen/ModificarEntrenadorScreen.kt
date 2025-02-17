package com.alvarodazacruces.proyectoalvarofirebase.screen

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alvarodazacruces.proyectoalvarofirebase.data.FirestoreEntrenadorViewModel
import com.alvarodazacruces.proyectoalvarofirebase.data.FirestorePokemonViewModel
import com.alvarodazacruces.proyectoalvarofirebase.model.EntrenadorBaseDatos
import kotlinx.coroutines.launch


@Composable
fun ModificarEntrenadorScreen(
    viewModel: FirestoreEntrenadorViewModel,
    entrenadorId: String,
    navController: NavController,
) {
    // Obtener el ViewModel de Pokémon
    val pokemonViewModel: FirestorePokemonViewModel = viewModel()

    // Estados para los campos de texto
    var name by remember { mutableStateOf("") }
    var selectedPokemonId by remember { mutableStateOf<String?>(null) }

    // Recuperar el Pokémon seleccionado desde la navegación
    navController.currentBackStackEntry?.savedStateHandle?.get<String>("selectedPokemon")?.let {
        selectedPokemonId = it
    }

    // Cargar la lista de Pokémon desde Firestore
    val pokemonList by pokemonViewModel.pokemonList.collectAsState(initial = emptyList())

    // Scope para lanzar corrutinas desde Compose
    val coroutineScope = rememberCoroutineScope()

    // Obtener el entrenador de Firestore
    val entrenador by viewModel.getEntrenadorById(entrenadorId).observeAsState(initial = null)

    // Cuando se recibe un entrenador, actualiza el campo 'name'
    LaunchedEffect(entrenador) {
        entrenador?.let {
            name = it.nombre // Asigna el nombre del entrenador al campo de texto
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

            // Campo para ingresar el nombre del entrenador
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para agregar Pokémon al entrenador
            Button(
                onClick = {
                    // Navegar a la pantalla para seleccionar Pokémon
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

            // Botón para modificar el entrenador
            Button(
                onClick = {
                    if (name.isNotEmpty() && selectedPokemonId != null) {
                        coroutineScope.launch {
                            // Crear el objeto EntrenadorBaseDatos con el nombre y el ID del Pokémon
                            val entrenador = EntrenadorBaseDatos(
                                id = entrenadorId,  // Usamos el ID actual para modificar
                                nombre = name,
                                pokemons = selectedPokemonId?.let { listOf(it) } ?: listOf() // Lista con un solo Pokémon
                            )

                            // Llamamos a la función de actualizar entrenador en el ViewModel
                            viewModel.updateEntrenador(entrenadorId, entrenador)  // Actualizar el entrenador

                            // Volver a la pantalla anterior después de la actualización
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
                enabled = selectedPokemonId != null // El botón solo se habilita si un Pokémon ha sido seleccionado
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

