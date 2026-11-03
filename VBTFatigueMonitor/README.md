# VBT Fatigue Monitor - Diario de entrenamiento basado en la velocidad

## Paso 1: Configuración del proyecto y temas personalizados (0.5 puntos)

1. Crear proyecto: crea un proyecto de tipo *empty activity*.
2. Personalizar el tema: modificar los archivos `Colo.kt`, `Type.kt` y `Theme.kt` dentro de `ui/theme`.
   1. Define una paleta de colores propia que no sea la de por defecto de Material Desing
   2. Aplicar tipografías personalizadas si se desea

## Paso 2: Estructura base con `scafold` (0.5 puntos)

1. Diseñar el layout principal: utilizar el componente `Scafold` de Jetpack Compose en tu plantilla principal.
2. Añadir elementos clave:
   1. `TopAppBar`con el título de la aplicación ("VBT Fatigue Monitor").
   2. `FloatingActionButton` para navegar a la pantalla de "Nueva Serie".
   3. Un contenedor central (`content`) para mostrar el historial de entrenamientos con `LazyColumn`

## Paso 3: Configuración de Base de Datos con ROOM (3.5 puntos)

Implementar la arquitectura de persistencia local dividida en 3 componentes principales:

1. Entidad (`SerieEntity.kt`): define una tabla que almacene los datos de cada serie realizada:
   1. `id` (Clave principal autoincremental)
   2. `exerciseName` (Nombre del ejercicio)
   3. `date` (Fecha y hora)
2. Entidad `RepeticionEntity:`
   1. `id` (autoincremental)
   2. `serieId` (clave foranea)
   3. `repNumber` (int)
   4. `velocity` (velocidad)
   5. `isFatiguedReached` (bool)
3. DAO (`SerieDao.kt`): Crea la interfaz con las consultas del CRUD completo:
   1. Create: `@Insert` para guardar una nueva serie
   2. Read: `@Query("SELECT * FROM series")` para el historial.
   3. Update: `@Update` para modificar notas o parámetros de una serie
   4. Delete: `@Delete` o `@Query("DELETE FROM series WHERE id = :id") para borrar registros`
4. Base de Datos (`AppDatabase.kt`): Configura la clase abstracta singleton de ROOM para instanciar la base de datos de forma segura.

## Paso 4: Navegación entre pantallas con Jetpack Navifation (3 puntos)

Configurar el grafo de navegación usando `NavHost` y `composable`:

1. Pantalla 1 (`HomeScreen`): Muestra el listado de entrenamientos guardados leídos desde ROOM mediante un `ViewModel` y un `StateFlow`. Incluye botones para eliminar elementos (Delete) o ver detalles (Read/Update).
2. Pantalla 2 (`TrainingScreen`): La pantalla activa donde el usuario realiza el ejercicio, interactúa con el sensor y guarda nuevos registros (Create).

## Paso 5: Integración del acelerómetro y lógica de Fatiga (2 puntos)

1. El botón funcional: Añade un botón principal en la pantalla 2 con el texto "Iniciar serie". Al pulsarlo, cambia su estado a "Finalizar y guardar", activando la escucha del sensor.
2. Gestión del sensor (`SensorManager`):
   1. Obtener una instancia de `SensorManager` y registrar un `SensorEventListener` escuchando el acelerómetro (`Sensonr.TYPE_ACCELEROMETER`).
   2. Lógica de cálculo: Filtra el eje vertical (Z o Y según cómo se coloque el movil) para detectar picos de aceleración durante la fase concéntrica del levantamiento.
   3. Umbral de fatiga: Toma la primera repetición como el 100% de velocidad de referencia. Si una repetición posterior presenta una caída de velocidad superior al 18%, cambia la interfaz de color (rojo) y avisas visualmente al usuario (proponer aviso sonoro).
   4. Ciclo de vida: Asegurate de desregistrar el sensor (`unregisterListener`) en los métodos del ciclo de vida adecuados o mediante `DisposableEffect` en Compose para evitar consumo de batería.

## Paso 6 (opcional): Conexión con Retrofit para datos externos

1. Añadir dependencias
2. Crear la interfaz API: Define una interfaz HTTP que consuma una API pública gratuíta (api de frases motivacionales/api de salud/deporte).
3. Consumo en el ViewModel: Muestra un consejo del día o frase motivadora en la pantalla de la aplicación mediante corrutinas de Kotlin