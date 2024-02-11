package com.sauleteh.autobingo.adapter

import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sauleteh.autobingo.*

class CardboardViewHolder(view: View) : ViewHolder(view)
{
    private val name: TextView = view.findViewById(R.id.tvName)
    private val iconEdit: ImageView = view.findViewById(R.id.ivEdit)
    private val iconDelete: ImageView = view.findViewById(R.id.ivDelete)
    private val checkBox: CheckBox = view.findViewById(R.id.cbActive)

    fun render(cardboard: Cardboard)
    {
        name.text = cardboard.name
        checkBox.isChecked = cardboard.checked

        iconEdit.setOnClickListener {
            // Colocar los datos del cartón en el view AddCardboard e ir a AddCardboard
            println("iconEdit.setOnClickListener")
            MainActivity.MainActivitySingleton.editCardboard(adapterPosition)
        }
        iconDelete.setOnClickListener {
            // Mostrar una ventana emergente preguntando si se quiere borrar (si/no) y borrar el cartón
            println("iconDelete.setOnClickListener")
            MainActivity.MainActivitySingleton.deleteCardboard(adapterPosition)
        }
        checkBox.setOnClickListener {
            // Cambiar estado del cartón al pulsar el check box
            println("checkBox.setOnClickListener")
            cardboard.checked = checkBox.isChecked
            CardboardProvider.serializeCardboards() // Serializar el cartón, no hace falta actualizar el adaptador
        }
    }
}