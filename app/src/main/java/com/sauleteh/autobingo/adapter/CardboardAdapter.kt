package com.sauleteh.autobingo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sauleteh.autobingo.Cardboard
import com.sauleteh.autobingo.R

class CardboardAdapter(private val cardboardList: ArrayList<Cardboard>) : RecyclerView.Adapter<CardboardViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardboardViewHolder
    {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CardboardViewHolder(layoutInflater.inflate(R.layout.item_cardboard, parent, false))
    }

    override fun onBindViewHolder(holder: CardboardViewHolder, position: Int)
    {
        val item = cardboardList[position]
        holder.render(item)
    }

    override fun getItemCount(): Int { return cardboardList.size }
}