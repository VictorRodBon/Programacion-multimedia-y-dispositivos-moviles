fun main() {
    // 1) Declara una variable numérica y verifica si es mayor que 5 para imprimir un mensaje. Luego una variable String, comprueba si coincide con un valor dado y muestra mensajes diferentes según el resultado.
    val num1 = 4
    if (num1 <= 5) {
        println("mensaje 1")
    } else {
        println("mensaje 2")
    }
    // 2) Según el valor de una variable que representa un anumal, imprime un mensaje específico para "perro", "gato" o "pájaro", y un mensaje genérico para cualquier otro caso.
    if (num1 <= 5) {
        println("mensaje 1")
    } else if (num1 <= 3) {
        println("mensaje 2")
    } else {
        println("mensaje 3")
    }
    // 3) Asigna a una variable un valor basado en una condición simple evaluada con una expresión if (por ejemplo, si la vida de un enemigo es menor o igual a cierto valor)
    val vida: Int = 100
    val ataqueEsp: Boolean = if (vida <= 100) {
        true
    } else {
        false
    }
    println(ataqueEsp)
    // 4) Declara una variable booleana y usa condiciones para imprimir mensajes diferentes si la variable es verdadera o falsa (usando negación)
    if (ataqueEsp) {
        println("Jefe abatido")
    } else {
        println("sigue atacando")
    }
    // 5) Usa una expresión when para asignar el nombre del día de la semana a artir de un número, incluyendo rangos y múltiples valores para un mismo resultado. Imprime el día correspondiente. Realiza una variante con un caso else distinto. Además, evalúa una puntuación numérica en rangos y muestra la evaluación correspondiente
    var diaSemana: String;
    when (num1) {
        1 -> diaSemana = "Lunes"
        2, 5 -> diaSemana = "Martes"
        3 -> diaSemana = "Miercoles"
        4 -> diaSemana = "Jueves"
        in 6..9 -> diaSemana = "Viernes"
        else -> diaSemana = "domingo"
    }
    print(diaSemana)
}
    // 6) Crea una función que reciba un parámetro de cualquier tipo (Any) y que use when para detectar si es String, Int, Boolean y otro, imprimiendo un mensaje según corresponda
    fun ej6(param: Any){
        when(param){
            is Int -> println("Int")
            is String -> println("String")
            is Boolean -> println("Boolean")
            else -> println("otro tipo")
        }
    }
    // 7) Inplementa dos funciones que reciban una puntuación y devuelvan un texto evaluativo según rangos numéricos usando when. Una debe usar return explícito y la otra debe usar la expresión when directamente como valor de retorno

fun ej7_1(nota: Int):String{
    when(nota){
        in 0..4 -> return "suspenso"
        in 5..6 -> return "aprobado"
        in 7..8 -> return  "notable"
        in 9..10 -> return "sobresaliente"
        else -> return "notal mal indicada"
    }
}

fun ej7_2(nota:Int){
    when(nota){
        in 0..4 -> println("suspenso")
        in 5..6 -> println("aprobado")
        in 7..8 -> println("notable")
        in 9..10 -> println("sobresaliente")
        else -> return println("nota mal indicada")
    }
}