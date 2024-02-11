package com.sauleteh.autobingo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sauleteh.autobingo.Cardboard
import com.sauleteh.autobingo.R

class CardboardAdapterSim(private val cardboardList: ArrayList<Cardboard>) : RecyclerView.Adapter<CardboardViewHolderSim>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardboardViewHolderSim
    {
        return if (viewType == 0) {
            val layoutInflater = LayoutInflater.from(parent.context)
            CardboardViewHolderSim(
                layoutInflater.inflate(
                    R.layout.item_simulation_cardboard,
                    parent,
                    false
                )
            )
        } else CardboardViewHolderSim(LayoutInflater.from(parent.context).inflate(R.layout.item_empty, parent, false))
    }

    override fun onBindViewHolder(holder: CardboardViewHolderSim, position: Int)
    {
        val item = cardboardList[position]
        holder.render(item)
    }

    override fun getItemCount(): Int { return cardboardList.size }

    override fun getItemViewType(position: Int): Int
    {
        return if (cardboardList[position].checked) 0
        else 1
    }
}