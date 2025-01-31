package com.alvarodazacruces.proyectoalvarofirebase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.alvarodazacruces.proyectoalvarofirebase.navegacion.Navegacion
import com.alvarodazacruces.proyectoalvarofirebase.navegacion.Navegacion
import com.alvarodazacruces.proyectoalvarofirebase.screen.PokemonSearchViewModel
import com.alvarodazacruces.proyectoalvarofirebase.ui.theme.ProyectoAlvaroFireBaseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProyectoAlvaroFireBaseTheme {
                val navController = rememberNavController()
                val viewModel: PokemonSearchViewModel = viewModel()

                Navegacion(navController = navController, viewModel = viewModel)
            }
        }
    }
}
