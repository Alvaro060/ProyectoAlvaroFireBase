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
import com.alvarodazacruces.proyectoalvarofirebase.data.FirestoreManager
import com.alvarodazacruces.proyectoalvarofirebase.data.FirestoreViewModel
import com.alvarodazacruces.proyectoalvarofirebase.ui.theme.ProyectoAlvaroFireBaseTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var firestoreManager: FirestoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)

        auth = FirebaseAuth.getInstance()

        firestoreManager = FirestoreManager()

        setContent {
            ProyectoAlvaroFireBaseTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    val authViewModel: AuthViewModel = viewModel()
                    val pokemonSearchViewModel: PokemonSearchViewModel = viewModel()

                    val firestoreViewModel: FirestoreViewModel = viewModel(
                        factory = FirestoreViewModel.FirestoreViewModelFactory(firestoreManager)
                    )

                    Navegacion(
                        navController = navController,
                        authViewModel = authViewModel,
                        pokemonSearchViewModel = pokemonSearchViewModel,
                        firestoreViewModel = firestoreViewModel
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
