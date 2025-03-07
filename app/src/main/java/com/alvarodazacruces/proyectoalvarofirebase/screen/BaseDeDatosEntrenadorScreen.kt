package com.alvarodazacruces.proyectoalvarofirebase.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.navigation.NavHostController
import com.alvarodazacruces.proyectoalvarofirebase.data.FirestoreViewModel

@Composable
fun BaseDeDatosEntrenadorScreen(
    navController: NavHostController,
    onLogout: () -> Unit,
    onNavigateToAddEntrenador: () -> Unit,
    onNavigateToListModifyEntrenador: () -> Unit,
    onNavigateToDeleteEntrenador: () -> Unit,
    onNavigateToVerListaEntrenadores: () -> Unit,
    viewModel: FirestoreViewModel
) {
    // Estado de la UI
    val entrenadorList by viewModel.entrenadorList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    // Interceptar el evento de retroceso del dispositivo
    BackHandler {
        // Redirige al menú cuando el botón de retroceso es presionado
        navController.navigate("pantalla_inicio_base_de_datos") {
            // Limpiar la pila de navegación para evitar que el usuario regrese a la pantalla de BaseDeDatosEntrenadoresScreen
            popUpTo("pantalla_inicio_base_de_datos") { inclusive = true }
        }
    }

    // Layout principal
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp), // Añadido un padding horizontal para centrar el contenido
            verticalArrangement = Arrangement.Center, // Centra los elementos verticalmente
            horizontalAlignment = Alignment.CenterHorizontally // Centra los elementos horizontalmente
        ) {
            // Imagen de encabezado
            AsyncImage(
                model = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/26.png", // Imagen de Pikachu, por ejemplo
                contentDescription = "Logo",
                modifier = Modifier.size(200.dp)
            )

            // Título
            Text(
                text = "Entrenadores",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            // Mostrar error si existe
            error?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            // Botón de "Agregar Entrenador"
            Button(
                onClick = onNavigateToAddEntrenador,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    "Agregar Entrenador",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            // Botón de "Modificar Entrenador"
            Button(
                onClick = onNavigateToListModifyEntrenador,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    "Modificar Entrenador",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            // Botón de "Ver Lista de Entrenadores"
            Button(
                onClick = onNavigateToVerListaEntrenadores,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    "Ver Lista de Entrenadores",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            // Botón de "Eliminar Entrenador"
            Button(
                onClick = onNavigateToDeleteEntrenador,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    "Eliminar Entrenador",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de "Cerrar Sesión"
            Button(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Text(
                    "Cerrar Sesión",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}
