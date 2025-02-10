package com.alvarodazacruces.proyectoalvarofirebase.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }

    // Buscamos el Pokémon cuyo id coincida con el pokemonId recibido
    val pokemon = viewModel.pokemonList.collectAsState().value.firstOrNull { it.id == pokemonId }

    // Actualizamos los valores de los campos si se encuentra el Pokémon
    LaunchedEffect(pokemon) {
        pokemon?.let {
            name = it.name
            type = it.type
        }
    }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Modificar Pokémon",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                ),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (pokemon != null) {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = type,
                    onValueChange = { type = it },
                    label = { Text("Tipo") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (name.isNotEmpty() && type.isNotEmpty()) {
                            coroutineScope.launch {
                                viewModel.updatePokemon(pokemon.id, name, type)
                                navController.popBackStack()
                            }
                        } else {
                            Toast.makeText(
                                context,
                                "Por favor, complete todos los campos",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Actualizar Pokémon",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            } else {
                Text(
                    text = "Pokémon no encontrado",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

