package com.sauleteh.autobingo

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class CardboardProvider
{
    companion object
    {
        private lateinit var preferences: SharedPreferences
        var darkTheme: Boolean = false
        var cardboardList = ArrayList<Cardboard>(listOf(
            /*Cardboard(
                "Cartón de la suerte",
                ArrayList<ArrayList<Byte>>(listOf(
                    ArrayList<Byte>(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9)),
                    ArrayList<Byte>(listOf(10, 20, 30, 40, 50, 60, 70, 80, 90)),
                    ArrayList<Byte>(listOf(0, 1, 2, 3, 4, 5, 6, 7, 8)))),
                1, false
            ),
            Cardboard(
                "Cartón campeón",
                ArrayList<ArrayList<Byte>>(listOf(
                    ArrayList<Byte>(listOf(2, 5, 78, 122, 123, 5, 6, 7, 8)),
                    ArrayList<Byte>(listOf(11, 32, 1, 3, 5, 6, 54, 32, 21)),
                    ArrayList<Byte>(listOf(10, 12, 23, 43, 54, 65, 76, 78, 98)))),
                1, true
            )*/
        ))

        fun init(context: Context) {
            preferences = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        }

        fun serializeCardboards()
        {
            val byteArrayOutputStream = ByteArrayOutputStream()
            val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
            objectOutputStream.writeObject(cardboardList)
            val serializedMyCardboards = byteArrayOutputStream.toByteArray()
            val base64MyCardboards = Base64.encodeToString(serializedMyCardboards, Base64.DEFAULT)

            val editor = preferences.edit()
            editor.putString("myCardboards", base64MyCardboards)
            editor.putBoolean("myTheme", darkTheme)
            editor.apply()
        }

        @Suppress("UNCHECKED_CAST")
        fun deserializeCardboards()
        {
            if (preferences.contains("myCardboards"))
            {
                val base64MyCardboards = preferences.getString("myCardboards", "")
                val serializedMyCardboards = Base64.decode(base64MyCardboards, Base64.DEFAULT)
                val byteArrayInputStream = ByteArrayInputStream(serializedMyCardboards)
                val objectInputStream = ObjectInputStream(byteArrayInputStream)
                cardboardList = objectInputStream.readObject() as java.util.ArrayList<Cardboard>
            }
            if (preferences.contains("myTheme"))
            {
                darkTheme = preferences.getBoolean("myTheme", false)
            }
        }
    }
}