fun main()  {
    // Ejercicio 1 y 2: declaración de variables y mostrarlas por pantalla
    val entero:Int=5
    println(entero::class.simpleName)
    val largo: Long=100L
    println(largo::class.simpleName)
    val flotante:Float=2.5F
    println(flotante::class.simpleName)
    val doble: Double=2.52
    println(doble::class.simpleName)
    val caracter: Char='A'
    println(caracter::class.simpleName)
    val cadena: String="Hola Mundo"
    println(cadena::class.simpleName)
    val booleano: Boolean=true
    println(booleano::class.simpleName)

    // Ejercicio 3: trabajar con variables mutables (var),
    // mostrando cómo cambian su valor e imprimir con concatenaciones e interpolación de cadenas

    var miVariable:String="Mi variable"
    println("el valor de mi variable: $miVariable")
    miVariable="Mi variable con otro valor"
    println("el nuevo valor de mi variable: $miVariable")

    // Ejercicio 4: Definir y utilizar funciones con diferentes características
    a()
    b("hola mundo")
    c("hola mundo")
    c()
    println(d(10,20))
    println(e(10,20))

}

// a) función sin parámetros que imprime un mensaje
fun a(){
    println("ejercicio 4 -a")
}

// b) función con un parámetro el cual va a escribir
fun b(param: String){
    println("ejercicio 4-b: $param")
}

// c) función con parámetro por defecto
fun c(param: String="por defecto"){
    println("ejercicio 4-c: $param")
}

// d) función que supa dos números enteros y devuelve el resultado (return)
fun d(n1:Int, n2:Int):Int{
    return (n1 + n2)
}

// e) realizar una función suma usando expresión única
fun e(a:Int, b:Int)=a+b