fun main()  {
    // 1) Crea una lista inmutable llamada frutas con los elementos: "manzanas", "bananas" y "cereza".
    val frutas:List<String> = listOf("manzana", "banana", "cereza")
    // 2) Imprime la lista completa
    println(frutas)
    // 3) Imprime el último elemento de la lista
    println(frutas.last())
    // 4) Imprime el primer elemento de la lista
    println(frutas.first())
    // 5) Recorre la lista y, para cada fruta, imprime su nombre en un alínea separada
    frutas.forEach { println(it) }
}