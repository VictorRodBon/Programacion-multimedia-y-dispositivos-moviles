fun main()  {
    // 1) Crea un array con los días de la semana, recórrelo e imprímelo por pantalla
    val semana: Array<String> = arrayOf("Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sábado", "Domingo")
    for(dia in semana){
        println(dia)
    }
    // 2) Crea un array con tres colores, recórrelo e imprime por pantalla la posición de cada color y el propio color
    val colores: Array<String> = arrayOf("rojo", "azul", "verde")
    for((index, color) in colores.withIndex()){
        println("[$index] - $color")
    }
}