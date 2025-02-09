package com.alvarodazacruces.proyectoalvarofirebase.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.alvarodazacruces.proyectoalvarofirebase.data.FirestorePokemonViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

suspend fun getPokemonTypeListModificar(name: String): List<String>? {
    return withContext(Dispatchers.IO) {
        try {
            val url = URL("https://pokeapi.co/api/v2/pokemon/${name.lowercase()}")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 5000
            connection.readTimeout = 5000

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                val json = JSONObject(response)
                val typesArray = json.getJSONArray("types")
                val typesList = mutableListOf<String>()
                for (i in 0 until typesArray.length()) {
                    val typeObject = typesArray.getJSONObject(i)
                    val typeName = typeObject.getJSONObject("type").getString("name")
                    typesList.add(typeName.lowercase())
                }
                return@withContext typesList
            } else {
                return@withContext null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext null
        }
    }
}

suspend fun validatePokemonModificar(name: String, inputType: String): Boolean {
    val apiTypes = getPokemonTypeListModificar(name)
    if (apiTypes == null) return false

    // Separamos la entrada usando "/" o "," y eliminamos espacios extra
    val inputTypes = inputType.split("/", ",")
        .map { it.trim().lowercase() }
        .filter { it.isNotEmpty() }

    // Retornamos true si TODOS los tipos ingresados se encuentran en los tipos de la API
    return inputTypes.all { it in apiTypes } && inputTypes.isNotEmpty()
}

@Composable
fun ModificarPokemonScreen(
    viewModel: FirestorePokemonViewModel,
    navController: NavController,
    pokemonId: String
) {
    // Estados para los campos de texto
    val name = remember { mutableStateOf("") }
    val type = remember { mutableStateOf("") }

    // Buscamos el Pokémon cuyo id coincida con el pokemonId recibido
    val pokemon = viewModel.pokemonList.collectAsState().value.firstOrNull { it.id == pokemonId }

    // Actualizamos los valores de los campos si se encuentra el Pokémon
    LaunchedEffect(pokemon) {
        pokemon?.let {
            name.value = it.name
            type.value = it.type
        }
    }

    // Obtenemos el contexto para mostrar Toasts
    val context = LocalContext.current

    // Scope para lanzar corrutinas desde Compose
    val coroutineScope = rememberCoroutineScope()

    // UI de la pantalla
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título de la pantalla
        Text(text = "Modificar Pokémon")

        // Verificamos si el Pokémon existe
        if (pokemon != null) {
            // Mostramos el ID del Pokémon
            Text(text = "ID: ${pokemon.id}", style = MaterialTheme.typography.titleMedium)

            // Campo para el nombre
            TextField(
                value = name.value,
                onValueChange = { name.value = it },
                label = { Text("Nombre") }
            )

            // Campo para el tipo
            TextField(
                value = type.value,
                onValueChange = { type.value = it },
                label = { Text("Tipo") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para actualizar el Pokémon
            Button(
                onClick = {
                    if (name.value.isNotEmpty() && type.value.isNotEmpty()) {
                        // Se lanza una corrutina para validar los datos mediante la PokeAPI
                        coroutineScope.launch {
                            val isValid = validatePokemonModificar(name.value, type.value)
                            if (isValid) {
                                viewModel.updatePokemon(pokemon.id, name.value, type.value)
                                navController.popBackStack()
                            } else {
                                Toast.makeText(
                                    context,
                                    "El nombre del Pokémon o el tipo son incorrectos",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } else {
                        Toast.makeText(context, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
                    }
                }
            ) {
                Text(text = "Modificar Pokémon")
            }
        } else {
            // Si no se encuentra el Pokémon, se muestra un mensaje
            Text(text = "No se encontró el Pokémon con el ID especificado.")
        }
    }
}
