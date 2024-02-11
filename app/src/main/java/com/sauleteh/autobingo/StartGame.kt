package com.sauleteh.autobingo

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sauleteh.autobingo.adapter.CardboardAdapterSim

class StartGame : AppCompatActivity()
{
    private lateinit var voiceIntent: Intent
    private var state: Boolean = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_game)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        StartGameSingleton.startGame = this
        initRecyclerView()
        voiceIntent = Intent(this, VoiceRecognitionService::class.java)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // Permiso no concedido en grabación de audio, solicitar permiso
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), 1)
        }

        var isPressed = false
        var timer: CountDownTimer? = null

        // Mecánica para mover el floating action button
        findViewById<FloatingActionButton>(R.id.fabMic).setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    isPressed = true
                    timer = object : CountDownTimer(1000, 1000) {
                        override fun onTick(millisUntilFinished: Long) {}

                        override fun onFinish() {
                            if (isPressed) {
                                findViewById<FloatingActionButton>(R.id.fabMic).hide()
                                isPressed = false
                            }
                        }
                    }
                    timer?.start()
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    findViewById<FloatingActionButton>(R.id.fabMic).show()
                    isPressed = false
                    timer?.cancel()
                }
            }
            false
        }
    }

    override fun onSupportNavigateUp(): Boolean // Botón retroceder
    {
        println("onSupportNavigateUp")
        stopService(voiceIntent)
        this.finish()
        return true
    }

    private fun initRecyclerView()
    {
        val recyclerView = findViewById<RecyclerView>(R.id.rvCardboards)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = CardboardAdapterSim(CardboardProvider.cardboardList)
        StartGameSingleton.recyclerView = recyclerView
    }

    fun micOnClick(view: View)
    {
        println("micOnClick")
        if (state)  // Si el micrófono está activado, desactivar
        {
            stopService(voiceIntent)
            findViewById<FloatingActionButton>(R.id.fabMic).backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(view.context, R.color.red))
            state = false
        }
        else    // Y si está desactivado, activar
        {
            startService(voiceIntent)
            findViewById<FloatingActionButton>(R.id.fabMic).backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(view.context, R.color.green))
            state = true
        }
    }

    object StartGameSingleton
    {
        lateinit var startGame: StartGame
        lateinit var recyclerView: RecyclerView

        @SuppressLint("NotifyDataSetChanged")
        fun labelNumber(number: String)
        {
            println("El número mencionado es: $number")
            for (cb in 0 until CardboardProvider.cardboardList.size) // Por cada cartón...
            {
                if (CardboardProvider.cardboardList[cb].checked) // Si se juega el cartón...
                {
                    for (i in 0..2)
                    {
                        for (j in 0..8)
                        {
                            val num: String = CardboardProvider.cardboardList[cb].numbers[i][j].toString()
                            if (num != "0" && num == number)
                            {
                                recyclerView.findViewHolderForAdapterPosition(cb)?.itemView?.findViewById<TextView>(
                                    startGame.resources.getIdentifier(
                                        "num$i$j" + "m",
                                        "id", startGame.packageName
                                    )
                                )?.setBackgroundResource(R.drawable.rounded_edge_num_bottom_yellow)
                            }
                        }
                    }
                }
            }
        }
    }
}