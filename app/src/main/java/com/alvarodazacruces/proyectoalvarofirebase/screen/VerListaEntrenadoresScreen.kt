package com.alvarodazacruces.proyectoalvarofirebase.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.alvarodazacruces.proyectoalvarofirebase.R
import com.alvarodazacruces.proyectoalvarofirebase.data.FirestoreViewModel

@Composable
fun VerListaEntrenadoresScreen(
    viewModel: FirestoreViewModel,
    navController: NavController
) {

    LaunchedEffect(Unit) {
        viewModel.getEntrenadores()
    }

    val entrenadorList by viewModel.entrenadorList.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    val filteredEntrenadorList = entrenadorList.filter { entrenador ->
        entrenador.nombre.lowercase().contains(searchQuery.lowercase())
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Buscar Entrenador") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (filteredEntrenadorList.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "No se encontraron entrenadores con ese nombre.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(filteredEntrenadorList) { entrenador ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable {
                                navController.navigate("ver_lista_pokemons_entrenador/${entrenador.id}")
                            },
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_entrenador_pokemon),
                                contentDescription = "Imagen de ${entrenador.nombre}",
                                modifier = Modifier.size(80.dp)
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            Column {
                                Text(
                                    text = "ID: ${entrenador.id}",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = "Nombre: ${entrenador.nombre}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "Pokémon: ${entrenador.pokemons.joinToString { it }}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
