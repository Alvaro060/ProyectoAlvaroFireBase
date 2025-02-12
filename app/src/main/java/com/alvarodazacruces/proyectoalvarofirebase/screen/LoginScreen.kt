package com.alvarodazacruces.proyectoalvarofirebase.screen

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alvarodazacruces.proyectoalvarofirebase.R
import com.alvarodazacruces.proyectoalvarofirebase.data.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    onSignInAnonymously: () -> Unit,
) {
    // Estados para email y contraseña
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    val isLoading by authViewModel.isLoading.collectAsState()
    val error by authViewModel.error.collectAsState()
    val user by authViewModel.user.collectAsState()

    // Navegar al siguiente destino si el usuario ya está autenticado
    LaunchedEffect(user) {
        if (user != null) {
            onLoginSuccess()
        }
    }

    // Obtener el contexto actual para crear el cliente de Google Sign-In
    val context = LocalContext.current

    // Configurar Google Sign-In
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(R.string.default_web_client_id))
        .requestEmail()
        .build()
    val googleSignInClient = GoogleSignIn.getClient(context, gso)

    // Lanzador para el resultado del intent de Google Sign-In
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.idToken?.let { idToken ->
                    authViewModel.signInWithGoogle(idToken)
                } ?: run {
                    authViewModel.updateError("Error al obtener el token de Google")
                }
            } catch (e: ApiException) {
                authViewModel.updateError("Error en Google Sign-In: ${e.localizedMessage}")
            }
        }
    }

    // Diseño de la pantalla de login
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp), // Espacio para toda la pantalla
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Título
        Text(
            text = "Iniciar Sesión",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            textAlign = TextAlign.Center
        )

        // Campo de correo electrónico
        TextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("Correo Electrónico") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            isError = error != null
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Campo de contraseña
        TextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("Contraseña") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            isError = error != null
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Botón para iniciar sesión con email y contraseña
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

        // Mostrar error si lo hay
        if (error != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = error ?: "", color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Enlace para restablecer contraseña
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

        Spacer(modifier = Modifier.height(24.dp))

        // Enlace para ir a la pantalla de registro
        TextButton(onClick = onNavigateToSignUp) {
            Text(
                text = "¿No tienes una cuenta? Regístrate",
                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para acceder de forma anónima
        Button(
            onClick = onSignInAnonymously,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Acceder de Forma Anónima")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- Botón para Iniciar Sesión con Google ---
        Button(
            onClick = {
                googleSignInClient.signOut().addOnCompleteListener {
                    val signInIntent = googleSignInClient.signInIntent
                    launcher.launch(signInIntent)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            shape = RoundedCornerShape(8.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_google),
                contentDescription = "Logo de Google",
                modifier = Modifier.size(84.dp)
            )
            Text(
                text = "Iniciar sesión con Google",
                color = Color.Black,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
