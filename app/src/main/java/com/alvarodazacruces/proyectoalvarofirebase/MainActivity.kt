package com.alvarodazacruces.proyectoalvarofirebase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.alvarodazacruces.proyectoalvarofirebase.data.AuthViewModel
import com.alvarodazacruces.proyectoalvarofirebase.navegacion.Navegacion
import com.alvarodazacruces.proyectoalvarofirebase.data.PokemonSearchViewModel
import com.alvarodazacruces.proyectoalvarofirebase.ui.theme.ProyectoAlvaroFireBaseTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

    // Inicializamos el objeto FirebaseAuth
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa Firebase antes de montar la UI
        FirebaseApp.initializeApp(this)

        // Inicializamos la autenticación de Firebase
        auth = FirebaseAuth.getInstance()

        setContent {
            ProyectoAlvaroFireBaseTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    val authViewModel: AuthViewModel = viewModel()
                    val pokemonSearchViewModel: PokemonSearchViewModel = viewModel()

                    // Inicio de la navegación
                    Navegacion(
                        navController = navController,
                        authViewModel = authViewModel,
                        pokemonSearchViewModel = pokemonSearchViewModel
                    )
                }
            }
        }
    }

    // Método para cerrar sesión cuando la actividad se destruye
    override fun onDestroy() {
        super.onDestroy()
        auth.signOut() // Cierra la sesión de Firebase
    }
}