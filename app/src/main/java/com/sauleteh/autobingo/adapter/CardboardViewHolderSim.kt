package com.sauleteh.autobingo.adapter

import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sauleteh.autobingo.*

class CardboardViewHolderSim(private val view: View) : ViewHolder(view)
{
    private val name: TextView? = view.findViewById(R.id.tvTitle)
    private val background: LinearLayout? = view.findViewById(R.id.llMain)

    fun render(cardboard: Cardboard)
    {
        // Meter todos los valores en el cartón simulado
        name?.text = cardboard.name
        val bgColor: Int = getColorFromId(cardboard.color)
        background?.setBackgroundColor(ContextCompat.getColor(view.context, bgColor))
        if (bgColor == R.color.black) name?.setTextColor(ContextCompat.getColor(view.context, R.color.white))   // Si el fondo es negro, el título se pone en blanco

        if (cardboard.checked)  // Aquí se comprueba el checked en vez de usar "?" para mayor optimización
        {
            for (i in 0..2)
            {
                for (j in 0..8)
                {
                    val num: String = cardboard.numbers[i][j].toString()
                    if (num != "0")
                    {
                        val numView: TextView = view.findViewById(
                            view.resources.getIdentifier(
                                "num$i$j",
                                "id", view.context.packageName
                            )
                        )
                        numView.text = num
                        var state = false
                        numView.setOnClickListener {
                            // Cambiar color de fondo al pulsar el número
                            println("numView.setOnClickListener")
                            if (state)  // Si el estado es marcado, desmarcamos los dos fondos
                            {
                                numView.setBackgroundResource(R.drawable.rounded_edge_num_top)
                                view.findViewById<TextView>(
                                    view.resources.getIdentifier(
                                        "num$i$j" + "m",
                                        "id", view.context.packageName
                                    )
                                ).setBackgroundResource(R.drawable.rounded_edge_num_bottom)
                                state = false
                            }
                            else    // Si no está marcado, se marca el número
                            {
                                numView.setBackgroundResource(R.drawable.rounded_edge_num_top_green)
                                state = true
                            }
                        }

                        view.findViewById<TextView>(
                            view.resources.getIdentifier(
                                "num$i$j" + "m",
                                "id", view.context.packageName
                            )
                        ).text = num
                    }
                }
            }
        }
    }

    private fun getColorFromId(id: Byte): Int
    {
        when (id.toInt())
        {
            0 -> { return getColorFromId((1..11).random().toByte()) }
            1 -> { return R.color.black }
            2 -> { return R.color.blue }
            3 -> { return R.color.brown }
            4 -> { return R.color.gray }
            5 -> { return R.color.green }
            6 -> { return R.color.orange }
            7 -> { return R.color.pink }
            8 -> { return R.color.purple }
            9 -> { return R.color.red }
            10 -> { return R.color.white }
            11 -> { return R.color.yellow }
        }
        return -1
    }
}