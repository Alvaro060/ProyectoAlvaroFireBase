package com.alvarodazacruces.proyectoalvarofirebase.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.alvarodazacruces.proyectoalvarofirebase.data.AuthViewModel

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import java.lang.Runtime.getRuntime

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    onSignInAnonymously: () -> Unit,
) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val isLoading by authViewModel.isLoading.collectAsState()
    val error by authViewModel.error.collectAsState()
    val user by authViewModel.user.collectAsState()

    LaunchedEffect(user) {
        if (user != null) {
            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Iniciar Sesión", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))

        // Campo de Email
        TextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Campo de Contraseña
        TextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("Contraseña") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Botón de Inicio de Sesión
        Button(
            onClick = { authViewModel.login(email.value, password.value) },
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(20.dp))
            } else {
                Text("Iniciar Sesión")
            }
        }

        // Mostrar Error
        if (error != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = error ?: "", color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Enlace para restablecer la contraseña
        TextButton(onClick = {
            authViewModel.forgotPassword(email.value)
        }) {
            Text("¿Has olvidado tu contraseña?")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para Registrarse
        TextButton(onClick = onNavigateToSignUp) {
            Text("¿No tienes una cuenta? Regístrate")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para Acceder Anónimamente
        Button(
            onClick = onSignInAnonymously,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Acceder de Forma Anónima")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para Salir de la Aplicación
        Button(
            onClick = { exitApp() },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Salir de la Aplicación")
        }
    }
}

// Función para salir de la aplicación
private fun exitApp() {
    getRuntime().exit(1000)
}