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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
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
            .padding(24.dp), // Padding ajustado para mayor espacio
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Título de la pantalla
        Text(
            text = "Iniciar Sesión",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        // Campo de Email
        TextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("Correo Electrónico") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            isError = error != null
        )
        Spacer(modifier = Modifier.height(16.dp)) // Espacio ajustado

        // Campo de Contraseña
        TextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("Contraseña") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            isError = error != null
        )
        Spacer(modifier = Modifier.height(24.dp)) // Espacio ajustado

        // Botón de Inicio de Sesión
        Button(
            onClick = { authViewModel.login(email.value, password.value) },
            modifier = Modifier.fillMaxWidth(),
            enabled = email.value.isNotEmpty() && password.value.isNotEmpty() && !isLoading,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Text("Iniciar Sesión")
            }
        }

        // Mostrar Error si es necesario
        if (error != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = error ?: "", color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(16.dp)) // Espacio ajustado

        // Enlace para restablecer la contraseña
        TextButton(onClick = {
            if (email.value.isNotEmpty()) {
                authViewModel.forgotPassword(email.value)
            } else {
                authViewModel.updateError("Por favor, ingresa un correo electrónico.")
            }
        }) {
            Text(
                text = "¿Has olvidado tu contraseña?",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.secondary
                )
            )
        }

        Spacer(modifier = Modifier.height(24.dp)) // Espacio ajustado

        // Botón para Registrarse
        TextButton(onClick = onNavigateToSignUp) {
            Text(
                text = "¿No tienes una cuenta? Regístrate",
                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary)
            )
        }

        Spacer(modifier = Modifier.height(16.dp)) // Espacio ajustado

        // Botón para Acceder Anónimamente
        Button(
            onClick = onSignInAnonymously,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Acceder de Forma Anónima")
        }
    }
}

