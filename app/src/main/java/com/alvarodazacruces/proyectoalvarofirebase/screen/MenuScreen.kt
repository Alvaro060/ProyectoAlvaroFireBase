package com.alvarodazacruces.proyectoalvarofirebase.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    navController: NavHostController
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Menú Principal") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    // Navega a la pantalla de la API (PantallaDeInicio)
                    navController.navigate("pantalla_inicio") {
                        popUpTo("menu") { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("Api Pokemon")
            }
            Button(
                onClick = {
                    // Navega a la pantalla de la base de datos (Firestore)
                    navController.navigate("base_de_datos_pokemon_screen") {
                        popUpTo("menu") { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("Base De Datos Pokemon")
            }
        }
    }
}
