package com.example.ejemploroom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.room.Room
import com.example.ejemploroom.data.AppDatabase
import com.example.ejemploroom.data.Usuario
import com.example.ejemploroom.ui.theme.EjemploRoomTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "tabla-nombres"
        ).build()

        val dao = db.usuarioDao()

        setContent {
            var nombrePorGuardar by remember { mutableStateOf("") }
            val listaUsuarios by dao.getAll().collectAsState(initial = emptyList())
            val scope = rememberCoroutineScope()

            Column(Modifier.fillMaxSize().padding(20.dp)) {
                TextField(
                    value = nombrePorGuardar,
                    onValueChange = { nombrePorGuardar = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Nombre del usuario") }
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        if (nombrePorGuardar.isNotBlank()) {
                            scope.launch {
                                dao.insert(Usuario(nombre = nombrePorGuardar))
                                nombrePorGuardar = ""
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Guardar Nombre")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // LA LISTA CON EL BOTÓN DE BORRAR
                LazyColumn {
                    items(listaUsuarios) { usuario ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                        ) {
                            Text(
                                text = "👤 ${usuario.nombre}",
                                modifier = Modifier.weight(1f) // Esto empuja al botón a la derecha
                            )

                            Button(
                                onClick = {
                                    scope.launch {
                                        // Room usará la ID de este objeto 'usuario' para borrarlo
                                        dao.delete(usuario)
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.error
                                )
                            ) {
                                Text("Borrar")
                            }
                        }
                        HorizontalDivider() // Una línea separadora para que quede limpio
                    }
                }
            }
        }
    }
}