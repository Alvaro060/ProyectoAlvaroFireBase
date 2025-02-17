package com.alvarodazacruces.proyectoalvarofirebase.screen

import android.widget.Toast
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
import com.alvarodazacruces.proyectoalvarofirebase.data.FirestoreEntrenadorViewModel
import com.alvarodazacruces.proyectoalvarofirebase.model.EntrenadorBaseDatos

@Composable
fun EliminarEntrenadorScreen(
    viewModel: FirestoreEntrenadorViewModel,
    navController: NavController
) {
    // Obtenemos la lista de entrenadores en tiempo real
    val entrenadorList = viewModel.entrenadorList.collectAsState().value

    // Estado para el filtro de búsqueda
    var searchQuery by remember { mutableStateOf("") }

    // Filtramos los entrenadores según el nombre (sin importar mayúsculas o minúsculas)
    val filteredEntrenadorList = entrenadorList.filter { entrenador ->
        entrenador.nombre.lowercase().contains(searchQuery.lowercase())
    }

    // Estado para controlar la visibilidad del AlertDialog
    var showDialog by remember { mutableStateOf(false) }
    var entrenadorToDelete by remember { mutableStateOf<EntrenadorBaseDatos?>(null) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Campo de búsqueda
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Buscar Entrenador") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Si la lista filtrada está vacía, mostramos un mensaje
        if (filteredEntrenadorList.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "No se encontraron entrenadores con ese nombre.")
            }
        } else {
            // Lista de entrenadores filtrados
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(filteredEntrenadorList) { entrenador ->
                    // Card clicable para eliminar
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable {
                                entrenadorToDelete = entrenador
                                showDialog = true
                            },
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Image(
                                painter = painterResource(id = R.drawable.ic_entrenador_pokemon), // Aquí se usa una imagen local
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

    // Mostrar el AlertDialog de confirmación
    if (showDialog && entrenadorToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Estás seguro de que deseas eliminar a ${entrenadorToDelete?.nombre}?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        // Lógica para eliminar el entrenador
                        entrenadorToDelete?.let { entrenador ->
                            viewModel.deleteEntrenador(entrenador)  // Asegúrate que esta función elimina el entrenador
                            showDialog = false
                            Toast.makeText(navController.context, "Entrenador eliminado", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) {
                    Text("Sí")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog = false }
                ) {
                    Text("No")
                }
            }
        )
    }
}
