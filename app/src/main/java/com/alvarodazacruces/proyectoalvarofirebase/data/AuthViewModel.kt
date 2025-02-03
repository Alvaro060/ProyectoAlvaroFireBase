package com.alvarodazacruces.proyectoalvarofirebase.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // Estado del usuario actual
    private val _user = MutableStateFlow<FirebaseUser?>(auth.currentUser)
    val user: StateFlow<FirebaseUser?> = _user

    // Estado de carga (isLoading)
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Estado de error
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    /**
     * Función para actualizar el estado del error.
     */
    fun updateError(message: String?) {
        _error.value = message
    }

    /**
     * Verifica si el usuario está autenticado.
     */
    fun isSignedIn(): Boolean {
        return auth.currentUser != null
    }

    /**
     * Inicia sesión con email y password.
     */
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.emit(true)
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    viewModelScope.launch {
                        _isLoading.emit(false)
                        if (task.isSuccessful) {
                            _user.emit(auth.currentUser)
                            updateError(null) // Limpia cualquier mensaje de error anterior
                        } else {
                            updateError(task.exception?.localizedMessage ?: "Error desconocido al iniciar sesión")
                        }
                    }
                }
        }
    }

    /**
     * Registra un usuario nuevo con email y password.
     */
    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.emit(true)
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    viewModelScope.launch {
                        _isLoading.emit(false)
                        if (task.isSuccessful) {
                            _user.emit(auth.currentUser)
                            updateError(null) // Limpia cualquier mensaje de error anterior
                        } else {
                            updateError(task.exception?.localizedMessage ?: "Error desconocido al registrarse")
                        }
                    }
                }
        }
    }

    /**
     * Cierra sesión.
     */
    fun logout() {
        viewModelScope.launch {
            auth.signOut()
            _user.emit(null)
            updateError(null) // Limpia cualquier mensaje de error anterior
        }
    }

    /**
     * Inicia sesión de forma anónima.
     */
    fun signInAnonymously() {
        viewModelScope.launch {
            _isLoading.emit(true)
            auth.signInAnonymously()
                .addOnCompleteListener { task ->
                    viewModelScope.launch {
                        _isLoading.emit(false)
                        if (task.isSuccessful) {
                            _user.emit(auth.currentUser)
                            updateError(null) // Limpia cualquier mensaje de error anterior
                        } else {
                            updateError(task.exception?.localizedMessage ?: "Error desconocido al iniciar sesión anónima")
                        }
                    }
                }
        }
    }

    /**
     * Inicia sesión con Google.
     */
    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _isLoading.emit(true)
            // Crear credenciales de Google
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            // Iniciar sesión con Firebase
            auth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    viewModelScope.launch {
                        _isLoading.emit(false)
                        if (task.isSuccessful) {
                            _user.emit(auth.currentUser)
                            updateError(null) // Limpia cualquier mensaje de error anterior
                        } else {
                            updateError(task.exception?.localizedMessage ?: "Error desconocido al iniciar sesión con Google")
                        }
                    }
                }
        }
    }
}