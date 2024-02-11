package com.sauleteh.autobingo

data class Cardboard(
    // Nombre del cartón
    var name: String,
    // Array 3x9 con los números del cartón (0 = null)
    var numbers: ArrayList<ArrayList<Byte>>,
    /* Color del cartón
    * 0 = Aleatorio
    * 1 = Negro
    * 2 = Azul
    * 3 = Marrón
    * 4 = Gris
    * 5 = Verde
    * 6 = Naranja
    * 7 = Rosa
    * 8 = Púrpura
    * 9 = Rojo
    * 10 = Blanco
    * 11 = Amarillo */
    var color: Byte,
    // Usar el cartón en el simulador
    var checked: Boolean
    ): java.io.Serializable