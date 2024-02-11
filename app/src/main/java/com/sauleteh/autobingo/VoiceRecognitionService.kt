package com.sauleteh.autobingo

import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer

class VoiceRecognitionService : Service()
{
    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var speechRecognitionIntent: Intent

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        speechRecognitionIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES")
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
            putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, packageName)
            //putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        }

        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(bundle: Bundle?) {
                //Código para manejar el evento de comienzo de grabación
                println("Grabación de micrófono preparada")
            }

            override fun onBeginningOfSpeech() {
                //Código para manejar el evento de comienzo de la grabación
                println("Grabación de micrófono comenzada")
            }

            override fun onRmsChanged(v: Float) {
                // Código para manejar el evento de cambio de nivel de sonido
            }

            override fun onEvent(i: Int, bundle: Bundle?) {
                // Código para manejar otros eventos
            }
            override fun onBufferReceived(bytes: ByteArray?) {
                // Código para manejar el evento de recepción de buffer
            }

            override fun onEndOfSpeech() {
                //Código para manejar el evento de finalización de la grabación
                println("Grabación de micrófono finalizada")
            }

            override fun onError(i: Int) {
                //Código para manejar errores
                println("onError: $i")
                speechRecognizer.startListening(speechRecognitionIntent)
            }

            override fun onResults(bundle: Bundle?)
            {
                bundle?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.let {
                    // procesa los resultados aquí
                    println("onResults")
                    println(it[0])
                    val num: String = vectorToNumber(it[0])
                    if (num != "null") StartGame.StartGameSingleton.labelNumber(num)
                }
                speechRecognizer.startListening(speechRecognitionIntent)
            }

            override fun onPartialResults(bundle: Bundle?) {
                //Código para manejar los resultados parciales
                println("onPartialResults")
            }
        })

        speechRecognizer.startListening(speechRecognitionIntent)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        println("onDestroy")
        speechRecognizer.destroy()
    }

    fun vectorToNumber(list: String): String
    {
        // Para transformar de número textual a númerico, primero se comprueba si hay decena y luego las unidades
        with (list) {
            when {
                // Comprobación de números con decenas y unidades
                contains("once") || contains("11") -> return "11"
                contains("doce") || contains("12") -> return "12"
                contains("trece") || contains("13") -> return "13"
                contains("catorce") || contains("14") -> return "14"
                contains("quince") || contains("15") -> return "15"
                contains("diez y seis") || contains("dieciséis") || contains("16") -> return "16"
                contains("diez y siete") || contains("diecisiete") || contains("17") -> return "17"
                contains("diez y ocho") || contains("dieciocho") || contains("18") -> return "18"
                contains("diez y nueve") || contains("diecinueve") || contains("19") -> return "19"

                contains("veinte y uno") || contains("veintiuno") || contains("21") -> return "21"
                contains("veinte y dos") || contains("veintidos") || contains("22") -> return "22"
                contains("veinte y tres") || contains("veintitrés") || contains("23") -> return "23"
                contains("veinte y cuatro") || contains("veinticuatro") || contains("24") -> return "24"
                contains("veinte y cinco") || contains("veinticinco") || contains("25") -> return "25"
                contains("veinte y seis") || contains("veintiseis") || contains("26") -> return "26"
                contains("veinte y siete") || contains("veintisiete") || contains("27") -> return "27"
                contains("veinte y ocho") || contains("veintiocho") || contains("28") -> return "28"
                contains("veinte y nueve") || contains("veintinueve") || contains("29") -> return "29"

                contains("treinta y uno") || contains("31") -> return "31"
                contains("treinta y dos") || contains("32") -> return "32"
                contains("treinta y tres") || contains("33") -> return "33"
                contains("treinta y cuatro") || contains("34") -> return "34"
                contains("treinta y cinco") || contains("35") -> return "35"
                contains("treinta y seis") || contains("36") -> return "36"
                contains("treinta y siete") || contains("37") -> return "37"
                contains("treinta y ocho") || contains("38") -> return "38"
                contains("treinta y nueve") || contains("39") -> return "39"

                contains("cuarenta y uno") || contains("41") -> return "41"
                contains("cuarenta y dos") || contains("42") -> return "42"
                contains("cuarenta y tres") || contains("43") -> return "43"
                contains("cuarenta y cuatro") || contains("44") -> return "44"
                contains("cuarenta y cinco") || contains("45") -> return "45"
                contains("cuarenta y seis") || contains("46") -> return "46"
                contains("cuarenta y siete") || contains("47") -> return "47"
                contains("cuarenta y ocho") || contains("48") -> return "48"
                contains("cuarenta y nueve") || contains("49") -> return "49"

                contains("cincuenta y uno") || contains("51") -> return "51"
                contains("cincuenta y dos") || contains("52") -> return "52"
                contains("cincuenta y tres") || contains("53") -> return "53"
                contains("cincuenta y cuatro") || contains("54") -> return "54"
                contains("cincuenta y cinco") || contains("55") -> return "55"
                contains("cincuenta y seis") || contains("56") -> return "56"
                contains("cincuenta y siete") || contains("57") -> return "57"
                contains("cincuenta y ocho") || contains("58") -> return "58"
                contains("cincuenta y nueve") || contains("59") -> return "59"

                contains("sesenta y uno") || contains("61") -> return "61"
                contains("sesenta y dos") || contains("62") -> return "62"
                contains("sesenta y tres") || contains("63") -> return "63"
                contains("sesenta y cuatro") || contains("64") -> return "64"
                contains("sesenta y cinco") || contains("65") -> return "65"
                contains("sesenta y seis") || contains("66") -> return "66"
                contains("sesenta y siete") || contains("67") -> return "67"
                contains("sesenta y ocho") || contains("68") -> return "68"
                contains("sesenta y nueve") || contains("69") -> return "69"

                contains("setenta y uno") || contains("71") -> return "71"
                contains("setenta y dos") || contains("72") -> return "72"
                contains("setenta y tres") || contains("73") -> return "73"
                contains("setenta y cuatro") || contains("74") -> return "74"
                contains("setenta y cinco") || contains("75") -> return "75"
                contains("setenta y seis") || contains("76") -> return "76"
                contains("setenta y siete") || contains("77") -> return "77"
                contains("setenta y ocho") || contains("78") -> return "78"
                contains("setenta y nueve") || contains("79") -> return "79"

                contains("ochenta y uno") || contains("81") -> return "81"
                contains("ochenta y dos") || contains("82") -> return "82"
                contains("ochenta y tres") || contains("83") -> return "83"
                contains("ochenta y cuatro") || contains("84") -> return "84"
                contains("ochenta y cinco") || contains("85") -> return "85"
                contains("ochenta y seis") || contains("86") -> return "86"
                contains("ochenta y siete") || contains("87") -> return "87"
                contains("ochenta y ocho") || contains("88") -> return "88"
                contains("ochenta y nueve") || contains("89") -> return "89"
                // Comprobación de números con solo decenas
                contains("diez") || contains("10") -> return "10"
                contains("veinte") || contains("vente") || contains("20") -> return "20"
                contains("treinta") || contains("trenta") || contains("30") -> return "30"
                contains("cuarenta") || contains("40") -> return "40"
                contains("cincuenta") || contains("50") -> return "50"
                contains("sesenta") || contains("seseinta") || contains("60") -> return "60"
                contains("setenta") || contains("seteinta") || contains("70") -> return "70"
                contains("ochenta") || contains("ocheinta") || contains("80") -> return "80"
                contains("noventa") || contains("noveinta") || contains("90") -> return "90"
                // Comprobación de números con solo unidades
                contains("uno") || contains("1") -> return "1"
                contains("dos") || contains("2") -> return "2"
                contains("tres") || contains("3") -> return "3"
                contains("cuatro") || contains("4") -> return "4"
                contains("cinco") || contains("5") -> return "5"
                contains("seis") || contains("6") -> return "6"
                contains("siete") || contains("7") -> return "7"
                contains("ocho") || contains("8") -> return "8"
                contains("nueve") || contains("9") -> return "9"
                else -> return "null"
            }
        }
    }
}