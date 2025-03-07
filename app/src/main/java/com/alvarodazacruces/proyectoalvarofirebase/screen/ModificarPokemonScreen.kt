package com.alvarodazacruces.proyectoalvarofirebase.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.alvarodazacruces.proyectoalvarofirebase.data.FirestoreViewModel
import com.alvarodazacruces.proyectoalvarofirebase.model.PokemonBaseDatos
import kotlinx.coroutines.launch

@Composable
fun ModificarPokemonScreen(
    viewModel: FirestoreViewModel,
    navController: NavController,
    pokemonId: String
) {
    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var validationError by remember { mutableStateOf("") }

    val pokemon = viewModel.pokemonList.collectAsState().value.firstOrNull { it.id == pokemonId }

    LaunchedEffect(pokemon) {
        pokemon?.let {
            name = it.name
            type = it.type
        }
    }

    val context = LocalContext.current
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
                text = "Modificar Pokémon",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                ),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (pokemon != null) {
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

                if (validationError.isNotEmpty()) {
                    Text(
                        text = validationError,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (name.isNotEmpty() && type.isNotEmpty()) {
                            coroutineScope.launch {
                                val updatedPokemon = PokemonBaseDatos(
                                    id = pokemon.id,
                                    name = name,
                                    type = type
                                )

                                viewModel.updatePokemon(updatedPokemon)

                                navController.popBackStack()
                            }
                        } else {
                            Toast.makeText(
                                context,
                                "Por favor, complete todos los campos",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Actualizar Pokémon",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            } else {
                Text(
                    text = "Pokémon no encontrado",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
