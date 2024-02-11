package com.sauleteh.autobingo

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sauleteh.autobingo.adapter.CardboardAdapter
import java.util.*

class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        CardboardProvider.init(this)
        MainActivitySingleton.mainActivity = this
        CardboardProvider.deserializeCardboards()   // Deserializar la lista de cartones
        initRecyclerView()

        // Establecer el tema claro/oscuro de la serialización
        if (CardboardProvider.darkTheme) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    private fun initRecyclerView()
    {
        val manager = LinearLayoutManager(this)
        val decoration = DividerItemDecoration(this, manager.orientation)
        val recyclerView = findViewById<RecyclerView>(R.id.rvCardboards)
        recyclerView.layoutManager = manager
        recyclerView.adapter = CardboardAdapter(CardboardProvider.cardboardList)
        recyclerView.addItemDecoration(decoration)
    }

    @Suppress("UNUSED_PARAMETER")
    fun newCardboardOnClick(view: View)
    {
        println("newCardboardOnClick")
        getResult.launch(Intent(this, AddCardboard::class.java))    // Ejecutar getResult al terminar AddCardboard
    }

    @Suppress("UNUSED_PARAMETER")
    fun startSimOnClick(view: View)
    {
        println("startSimOnClick")
        startActivity(Intent(this, StartGame::class.java))
    }

    @SuppressLint("NotifyDataSetChanged")
    private val getResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        {
            findViewById<RecyclerView>(R.id.rvCardboards).adapter?.notifyDataSetChanged()   // Actualizar el RecyclerView
            CardboardProvider.serializeCardboards()   // Serializar la lista de cartones
        }

    object MainActivitySingleton
    {
        lateinit var mainActivity: MainActivity

        fun editCardboard(index: Int)
        {
            val senderIntent = Intent(mainActivity, AddCardboard::class.java)
            senderIntent.putExtra("KEY_SENDER", index)
            mainActivity.getResult.launch(senderIntent)    // Ejecutar getResult al terminar AddCardboard
        }

        @SuppressLint("NotifyDataSetChanged")
        fun deleteCardboard(index: Int)
        {
            val builder = AlertDialog.Builder(mainActivity)
            builder.setTitle("Confirmar borrado")
            builder.setMessage("¿Estás seguro de querer borrar el cartón seleccionado?")
            builder.setPositiveButton("SÍ") { _, _ ->
                CardboardProvider.cardboardList.removeAt(index)
                mainActivity.findViewById<RecyclerView>(R.id.rvCardboards).adapter?.notifyDataSetChanged()   // Actualizar el RecyclerView
                CardboardProvider.serializeCardboards()   // Serializar la lista de cartones
            }
            builder.setNegativeButton("NO") { _, _ -> }
            builder.show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.three_dot_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.iChangeTheme -> {
                println("iChangeTheme")
                if (CardboardProvider.darkTheme)
                {
                    println("Modo noche desactivado")
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    CardboardProvider.darkTheme = false
                }
                else
                {
                    println("Modo noche activado")
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    CardboardProvider.darkTheme = true
                }
                CardboardProvider.serializeCardboards()
                return true
            }
            R.id.iAbout -> {
                println("iAbout")
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Acerca de este software")
                builder.setMessage("Auto Bingo\nVersión: 1.0\nAutor: ElMineToFlama\n\nGracias por usar este programa")
                builder.setPositiveButton("OK") { _, _ ->
                    // Acción que se realiza cuando se pulsa OK
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}