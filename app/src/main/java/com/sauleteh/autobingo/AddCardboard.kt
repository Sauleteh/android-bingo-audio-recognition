package com.sauleteh.autobingo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*

class AddCardboard : AppCompatActivity()
{
    private var spinner: Spinner ?= null
    private var index: Int = -1

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_cardboard)

        spinner = findViewById(R.id.sColor)    // El spinner
        val colorList = listOf( // La lista de colores del spinner
            "Aleatorio",
            "Negro",
            "Azul",
            "Marrón",
            "Gris",
            "Verde",
            "Naranja",
            "Rosa",
            "Púrpura",
            "Rojo",
            "Blanco",
            "Amarillo"
        )
        val sAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, colorList) // Meter los items al spinner
        spinner!!.adapter = sAdapter

        // Si se pasó un cartón al crear esta actividad, no se crea un nuevo cartón sino que se edita el que se pasó
        index = intent.getIntExtra("KEY_SENDER", -1)
        if (index != -1)
        {
            findViewById<EditText>(R.id.ptName).setText(CardboardProvider.cardboardList[index].name)
            spinner!!.setSelection(CardboardProvider.cardboardList[index].color.toInt())

            for (i in 0..2)
            {
                for (j in 0..8)
                {
                    val num: String = CardboardProvider.cardboardList[index].numbers[i][j].toString()
                    if (num != "0") {
                        findViewById<EditText>(
                            resources.getIdentifier(
                                "num$i$j",
                                "id", packageName
                            )
                        ).setText(num)
                    }
                }
            }
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun cancelOnClick(view: View)
    {
        println("cancelOnClick")
        finish()
    }

    @Suppress("UNUSED_PARAMETER")
    fun okOnClick(view: View)
    {
        println("okOnClick")
        if (!checkNumbers()) return // Si no están bien puestos los números, avisar y no seguir

        if (index == -1)  // Crear un nuevo Cardboard y meterlo en el CardboardProvider
        {
            CardboardProvider.cardboardList.add(
                Cardboard(
                    findViewById<EditText>(R.id.ptName).text.toString(),
                    getAllNumbers(),
                    spinner!!.selectedItemPosition.toByte(), true
                )
            )
        }
        else    // Si estamos en modo edición de cartón, lo editamos
        {
            CardboardProvider.cardboardList[index].name = findViewById<EditText>(R.id.ptName).text.toString()
            CardboardProvider.cardboardList[index].numbers = getAllNumbers()
            CardboardProvider.cardboardList[index].color = spinner!!.selectedItemPosition.toByte()
        }
        finish()
    }

    // Hacer todas las comprobaciones necesarias en los números
    private fun checkNumbers(): Boolean
    {
        val numList: ArrayList<Byte> = ArrayList()
        val numsPerRow: ArrayList<Byte> = ArrayList(listOf(0, 0, 0))

        for (i in 0..2)
        {
            for (j in 0..8)
            {
                val value: String = findViewById<EditText>(
                    resources.getIdentifier(
                        "num$i$j",
                        "id", packageName
                    )
                ).text.toString()

                if (value != "")
                {
                    val num: Byte = value.toByte()
                    if (j == 0) // Primera columna (9 posibles números)
                    {
                        if (num !in 1..9)
                        {
                            Toast.makeText(this, "ERROR: Número $num no está en el rango 1-9", Toast.LENGTH_SHORT).show()
                            return false
                        }
                    }
                    else if (j == 8)    // Última columna (11 posibles números)
                    {
                        if (num !in 80..90)
                        {
                            Toast.makeText(this, "ERROR: Número $num no está en el rango 80-90", Toast.LENGTH_SHORT).show()
                            return false
                        }
                    }
                    else    // Demás columnas (10 posibles números)
                    {
                        val min: Int = j * 10
                        val max: Int = j * 10 + 9
                        if (num !in min..max)
                        {
                            Toast.makeText(this, "ERROR: Número $num no está en el rango $min-$max", Toast.LENGTH_SHORT).show()
                            return false
                        }
                    }
                    if (!numList.contains(num)) // Comprobar repetidos
                    {
                        numList.add(num)
                        numsPerRow[i]++
                    }
                    else
                    {
                        Toast.makeText(this, "ERROR: Número $num repetido", Toast.LENGTH_SHORT).show()
                        return false
                    }
                }
            }
        }
        if (numList.size != 15)
        {
            Toast.makeText(this, "ERROR: El cartón debe tener 15 números", Toast.LENGTH_SHORT).show()
            return false
        }
        // Comprobar que cada fila tiene 5 números
        if (!(numsPerRow[0].compareTo(5) == 0 && numsPerRow[1].compareTo(5) == 0 && numsPerRow[2].compareTo(5) == 0))
        {
            Toast.makeText(this, "ERROR: Debe haber 5 números por fila", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    // Retornar la matriz 3x9 con los números del cartón
    private fun getAllNumbers(): ArrayList<ArrayList<Byte>>
    {
        val numbers: ArrayList<ArrayList<Byte>> = ArrayList(listOf(
            ArrayList(),
            ArrayList(),
            ArrayList()))

        for (i in 0..2)
        {
            for (j in 0..8)
            {
                val value = findViewById<EditText>(
                    resources.getIdentifier(
                        "num$i$j",
                        "id", packageName
                    )
                ).text.toString()

                numbers[i].add(if (value == "") 0 else value.toByte())
            }
        }
        Toast.makeText(this, "Cartón añadido", Toast.LENGTH_SHORT).show()
        return numbers
    }

    fun generateRandomNumbersOnClick(view: View)
    {
        println("generateRandomNumbersOnClick")

        deleteAllOnClick(view)  // Borrar los números antes de insertar nuevos
        // ArrayList con todas las posibles posiciones (00,...,08,10,...,18,20,...,28)
        val posibilities: ArrayList<String> = ArrayList()
        for (i in 0..2)
        {
            for (j in 0..8)
            {
                posibilities.add("$i$j")
            }
        }

        var rowColumn: String
        var generatedRandom: Byte

        val alreadyAdded: ArrayList<Byte> = ArrayList() // Números generados aleatoriamente ya insertados
        val count: ArrayList<Int> = ArrayList(listOf(0, 0, 0))
        var iterations = 0
        while (count[0] < 5 || count[1] < 5 || count[2] < 5)
        {
            rowColumn = posibilities[(0 until posibilities.size).random()]  // Escoger una posición del ArrayList y borrarla
            posibilities.remove(rowColumn)
            generatedRandom = columnToRandomNumber(rowColumn[1].toString().toInt())    // Generar un número aleatorio válido
            iterations++
            while (alreadyAdded.contains(generatedRandom))  // Mientras el número random no sea válido, generar otro
            {
                generatedRandom = columnToRandomNumber(rowColumn[1].toString().toInt())
                iterations++
            }
            alreadyAdded.add(generatedRandom)   // Insertar el número generado válido

            findViewById<EditText>( // Insertar el número en el cartón
                resources.getIdentifier(
                    "num" + rowColumn[0] + rowColumn[1],
                    "id", packageName
                )
            ).setText(generatedRandom.toString())

            count[rowColumn[0].toString().toInt()]++  // Aumentar "count[i]" una unidad
            if (count[rowColumn[0].toString().toInt()] == 5)  // Si "count[i]" ahora es 5, borrar todos los elementos de la ArrayList que empiecen por "i"
            {
                val it = posibilities.iterator()
                while (it.hasNext())
                {
                    if (it.next().startsWith(rowColumn[0]))
                    {
                        it.remove()
                    }
                }
                println(posibilities)
            }
        }
        println(iterations)
        sortOnClick(view)   // Ejecutar algoritmo de ordenamiento
    }

    private fun columnToRandomNumber(column: Int): Byte
    {
        return when (column) {
            0 // Primera columna (9 posibles números)
            -> {
                (1..9).random().toByte()
            }
            8    // Última columna (11 posibles números)
            -> {
                (80..90).random().toByte()
            }
            else    // Demás columnas (10 posibles números)
            -> {
                val min: Int = column * 10
                val max: Int = column * 10 + 9
                (min..max).random().toByte()
            }
        }
    }

    // Ordena todas las columnas para que los números estén ordenados de menor a mayor de arriba a abajo
    @Suppress("UNUSED_PARAMETER")
    fun sortOnClick(view: View)
    {
        println("sortOnClick")

        for (col in 0..8)   // Realizar el algoritmo de la burbuja por cada columna
        {
            for (i in 0..1) // Por cada fila (excepto la última)...
            {
                val pos1 = findViewById<EditText>(resources.getIdentifier("num$i$col","id",
                    packageName
                ))
                if (pos1.text.toString() != "")
                {
                    for (j in i+1..2)   // Por cada fila que esté delante de la fila i...
                    {
                        val pos2 = findViewById<EditText>(resources.getIdentifier("num$j$col","id",
                            packageName
                        ))
                        if (pos2.text.toString() != "")
                        {
                            if (pos2.text.toString().toByte() < pos1.text.toString().toByte())
                            {
                                val temp = pos1.text.toString()
                                pos1.setText(pos2.text.toString())
                                pos2.setText(temp)
                            }
                        }
                    }
                }
            }
        }
    }

    // Borrar todos los números del cartón
    @Suppress("UNUSED_PARAMETER")
    fun deleteAllOnClick(view: View)
    {
        println("deleteAllOnClick")

        for (i in 0..2)
        {
            for (j in 0..8)
            {
                findViewById<EditText>(
                    resources.getIdentifier("num$i$j","id",
                        packageName
                    )).setText("")
            }
        }
    }
}