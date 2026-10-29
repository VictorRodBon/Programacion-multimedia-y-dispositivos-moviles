# PROGRAMACIÓN MULTIMEDIA Y DE DISPOSITIVOS MÓVILES

## RA 2:

### Debug en Android Studio

Para ejecutar una aplicación en modo debug hay que ejecutarla pulsando en el icono del bicho ("bug").

Si queremos parar la ejecución en una línea, hay que marcarla con un click en la parte izquierda.

Con el botón derecho se pueden añadir condiciones para que el breack point solo funcione cuando se cumplan.

Si nos situamos cobre una variable. se mostrará su valor actual.

Controles:
- Resume Program: Continúa la ejecución
- Step Over: Pasa a la siguiente instrucción sin meterse en la actual
- Step Into: Pasa a la siguiente instrucción, metiéndose en la actual
- Step Out: Ejecuta el resto de la función actual y se para en la siguiente instrucción exterior.

También es importante revisar los logs ("LogCat").

### Introducción a Jetpack Compose

Al crear un proyecto con Jetpack Compose, Android Studio crea una estructura básica con un "*Hola mundo*".

- **Paquete:** Donde se encuentra
- Librerías **importadas:** 
- **Actividad principal:** Es lo que se ejecuta al comenzar la aplicación
- **Ventana interna:** Decimos que si es posible no repete los "límites de la pantalla" (reloj, señal, botones...). Es decir, pintar detrás de elementos del sistema. En teléfonos antiguos no funciona.
  - super.onCreate(savedInstanceState)
  - enableEdgeToEdge()
- Los que modificaremos es el contenido de **setContent** para puntar lo que queramos:
```kotlin
setContent{
    EjemploTheme{
        Scaffold(modifier = Modifier.filMaxSice()){ innerPadding ->
            Greeting(
                name = "Android",
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
```
- **Función de composición:** Es una parte de la composición visual. Con *@Composable* se indica que es la parte de la composición. En una pantalla podemos tener varias funciones *@Composable* que se invocan desde la actividad princiàl o entre sí
```kotlin
@Composable
fun Greeting(name: String, modifier: Modifier){
    Text(
        text = "Hello $name",
        modifier = modifier
    )
}
```
- **Función de preview:** Es lo que se ejecutará antes para mostrar la vista previa. Es un bloque opcional, aunque bastante útil cuando se está desarrollando una aplicación.
```kotlin
@Preview(showBackground = true)
@Composable
fun GreeringPreview(){
    EjemploTheme{
        Greeting("Android")
    }
}
```

### Textos e imágenes con Jetpack Compose
- Función **Text**  
```kotlin
@Composable
public fun Text(
    text:String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    ...
)
```
- Función **Image**
  - La imagen debe estar presente en la estructura del proyecto (*app/res/drawable/imagen*)
  - Independientemente del tipo de archivo, nunca se indica la extensión.
```kotlin
@Composable
public fun Image(
    painter: Painter,
    contentDescription: String?,
    modifier: Modifier = Modiffier,
    alignment: Alignment = Alignment.Center,
    ...
)
```

### Filas y columnas en Jetpack Compose
- **Filas:**
```kotlin
Row{
    // elementos
}
```
- **Columnas:**
```kotlin
Column{
    // elementos
}
```
- **Modificadores:**
```kotlin
modifier = Modifier
    .fillMaxSize()
    .padding(16.dp),
horizontalAlignment = Alignment.CenterHorizontally,
verticalAlignment = Arrangement.Center,
```

### Modificadores en Jetpack Compose
Los modificadores sirven para cambiar la apariencia de los elementos.

Se pasan com un parámetro más a los elementos.

Se aplican en orden mediante programación funcional, encadenando funciones.

La mayoría de los modificadores son comunes a todos los elementos.

los **Spacer** son componentes invisibles para Jetpack Compose, a los cuales se pueden aplicar también modificadores.

Los modificadoers se aplican en el orden en el que se concatenan.

### Temas en Jetpack Compose
Al crear un proyecto, se crea en él un tema

#### Color.kt

Es donde se definen los nombres de los colores y su valor hexadecimal

**Type.kt**

Es donde se definen las fuentes y tamaños

#### Theme.kt

Es el fichero que une los otros dos.

Se deciden colores para modo claro y oscuro (se cambia el modo en los ajustes del teléfono).

Contiene la función **MateriarTheme** que envuelve toda la aplicación.

- Color **PRIMARY**: El color de tu marca
  - Se usa para las cosas más importantes en las que el usuario tiene que hacer clic o fijarse.
- Color **SECONDARY**: Es un color que combina con el primario, pero es menos llamativo. Sirve para acompañar.
  - Se usa para elementos secundarios que son útilies, pero no son el objetivo principal de la pantalla.
- Color **TERTIARY**: Es un color diferente a los otros que se usa muy poco, solo para dar un "chispazo" de atención o contraste en un lugar puntual.
  - Se usa para:
    - Avisos
    - Notificaciones
    - Ofertas
    - Destacar algo muy concreto

Los colores primari, secundario y terciario se aplican automáticamente a lo elementos correspondientes.

Además, pueden aplicarse como modificadores.

Hay más elementos:
- background: color de fondo de la pantalla completa.
- onBackground: El texto o icono predeterminado dobre el fondo de pantalla.

#### MainActivity

Para poder usar un tema, es necesario incluirlo en el seetContent (y en el preciew en su caso):
```kotlin
setContent{
    MyAplicationTheme(){
        MyComponent()
    }
}
```

Y ya que se aplicará automáticamente, y podemos usarlo también como un modificador en ;ainActivity en algún elemento.
```kotlin
modifier = Modifier
    .background(MatherialTheme.colorScheme.secondary)
```

Si entheme.kt tenemos activado el colro dinámico, android puede decidir adaptar el color a uno que ocnsidere más adecuado. Hay que cambiarlo a *dinamicColor: Boolean = false* si queremos forzar el uso del que indicamos.

En **Theme.kt** está el MaterialTheme con el nombre para el esquema de colores.

### Estados en Jetpack Compose

#### Remember

### Botones en Jetpack Compose

### Scaffold

En android, el Scaffold es la estructura. Es una "plantilla" que ya tiene los huecos preparados para las cosas que todas las apps suelen tener:
- Una barra arriba (Titulo)
- Un botón flotante (el + en WhatsApp)
- Una barra abajo (Menú)
- Espacio central (donde va el contenido)

Todas las secciones son opcionales.

Si usas un Scaffold sin nada, es símplemente una pantalla blanca que sabe gestionar los márgenes

```kotlin
    Scaffold{padding -> 
        // Aquí va el contenido
        Text("Hola, soy el contenido", modifier = Modifier.padding(padding))
    }
    @Composable
    fun EjemploScaffoldCompleto() {
        Scaffold(
            // 1. LA BARRA SUPERIOR (El Techo)
            topBar = {
                TopAppBar(
                    title = { Text("Ejemplo Scaffold") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF6200EE),
                        titleContentColor = Color.White
                    )
                )
            },
            // 2. EL BOTÓN FLOTANTE (La Acción Principal)
            floatingActionButton = {
                FloatingActionButton(onClick = { /* Acción */ }) {
                    Icon(Icons.Default.PlayArrow, contentDescription = "Reproducir")
                }
            },
            // 3. LA BARRA INFERIOR (El Menú)
            bottomBar = {
                BottomAppBar(
                    containerColor = Color(0xFF6200EE),
                    contentColor = Color.White
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = "Inicio | Buscar | Biblioteca"
                    )
                }
            }
        )
        { innerPadding ->
        // 4. EL CONTENIDO PRINCIPAL (El salón de la casa)
        // Usamos el padding que nos da el Scaffold para que nada se solape
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.LightGray),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Estamos dentro del área de contenido")
            }
        }
    }
```


El Scaffold puede ponerse en el MainActivity o en la pantalla que queramos directamente.

Hay que tener en cuenta que so lo ponemos en el MainActivity, y tenermos varias pantallas, todas ellas tendrán la misma estructura.

Si tenemos varias pantallas, y queremos la misma estructura para todas, podemos cambiar solo el contenido en función de la pantalla seleccionada.
```kotlin
{ innerPadding ->
    // 3. El contenido cambia según el estado, pero el Scaffold no se mueve
    Box(modifier = Modifier.padding(innerPadding)) {
        when (pantallaActual) {
            "Inicio" -> PantallaDeInicio()
            "Videos" -> EjercicioConceptosMultimedia() // Tu ejercicio anterior
    }
}
```

Si queremos estructuras distintas en las pantallas, usaremos un Scaffold distinto para cada una, en lugar de definirlo en el MainActivity.

### Navegación con Jetpack Compose

Para poder usar la navegación entre pantallas, primero es necesario revisar la configuración del fichero **build.gradle.kts (Module:app)** y añadir lo siguietne (si no está).

Después hay que crear los ficheros de pantalla (tantos como pantallas tengamos). todas ellas recibiran por parámetro el control de navegación, que es el que realmente se encarga de cambiar entre pantallas.

```kotlin
// Archivo: PantallaA.kt
@Composable
fun PantallaA(navController: NavHostController){
    // UI de la pantalla

    // Botón para ir a B
    Button(onClick = {navControler.navigate("pantalla_2")}){
        Text("Ir a B")
    }
}
```

Y por último, en el MainActivity hay que crear la estructura general de navegación:

```kotlin
setContent{
    // 1. Crear el mando para toda la App
    val navcontroleer = rememberNavControler()
    // 2. Crear el mapa de rutas
    NavHost(
        navControler = navControler,
        startDestination = "pantalla_1"
    ){
        // 3. Asociar nombres a los archivos externos
        composable("pantalla_1"){
            PantallaA(navControler) // Llama al codigo del archivo PantallaA
        }
        composable("pantalla_2"){
            PantallaB(navControler) // Llama al codigo del archivo PantallaA
        }
    }
}
```

### ViewModel

A nivel teórico, el ViewModel es una clase diseñada para almacenar y gestionar los datos relacionados con la UI. Su función principal es servir de puente entre tus datos y lo que el usuario ve.

Los estados (Activo/Pausado/Destruido) son las etapas de la vida de la aplicación. El ViewModel es un baúl de recuerdos que sobrevive a esas etapas para que, si la app "muere" y "resucita" al girar el móvil, no lo olvidemos todo.

En Android, cuando giras la pantalla, el sistema destruye y recrea la Activity. Si tenías una variable, al girar el movil se resetea.

- **Sin** ViewModel: Los datos se pierden o tienes que guardarlos con métodos farragosos y poco seguros.
- **Con** ViewModel: Los datos sobreviven a la rotación de pantalla.

Las clases que guardan y manipulan los datos persistentes deben heredar de ViewModel():

```kotlin
import androidx.lifecycle.ViewModel
class ContadorViewModel : ViewModel() {
    // Variables y funciones de manipulación de datos
}
```

para usar ViewModel hay que añadir la **versión** y **librería** en:

```toml
//  libs.versions.toml
[versions]

# ···
# Define la versión de la librería
lifecycleCompose = "2.8.4"

[libraries]
#···
# Define el alias que usaremos después. Importante, usa quiones, no puntos
androidx-lifecycle-viewmodel-compose = { group = "androidx.lifecycle", name = "lifecycle-viewmodel-compose", version.ref = "lifecycleCompose" }
```

Después hay que añadir la **dependencia** en:

```kotlin
// build.gradle.kts (Module :app)

// Fíjate: "androidx-lifecycle-viewmodel-compose" pasa a "androidx.lifecycle.viewmodel.compose"
implementation(libs.androidx.lifecycle.viewmodel.compose)
```

Después hay que **sincronizar** el proyecto.