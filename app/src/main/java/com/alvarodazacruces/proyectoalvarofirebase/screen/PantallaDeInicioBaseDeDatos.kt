package com.alvarodazacruces.proyectoalvarofirebase.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDeInicioBaseDeDatos(
    navController: NavHostController,
    onLogout: () -> Unit,
    onNavigateToPokemons: () -> Unit,
    onNavigateToEntrenadores: () -> Unit
) {

    BackHandler {
        // Redirige al menú cuando el botón de retroceso es presionado
        navController.navigate("menu") {
            // Limpiar la pila de navegación para evitar que el usuario regrese a la pantalla de BaseDeDatosPokemonScreen
            popUpTo("menu") { inclusive = true }
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Imagen de encabezado
            AsyncImage(
                model = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/25.png",
                contentDescription = "Logo",
                modifier = Modifier.size(200.dp)
            )

            // Título
            Text(
                text = "Base de Datos",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            // Botón de "Pokémons"
            Button(
                onClick = onNavigateToPokemons,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    "Pokémons",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            // Botón de "Entrenadores"
            Button(
                onClick = onNavigateToEntrenadores,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    "Entrenadores",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            // Botón de "Cerrar Sesión"
            Button(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
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
