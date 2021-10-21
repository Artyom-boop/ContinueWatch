package ru.spbstu.icc.kspt.lab2.continuewatch

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var secondsElapsed: Int = 0
    private lateinit var textSecondsElapsed: TextView
    private var onScreen = true
    private lateinit var sharedPref: SharedPreferences

    private var backgroundThread = Thread {
        while (true) {
            if (onScreen) {
                Thread.sleep(1000)
                textSecondsElapsed.post {
                    textSecondsElapsed.text = getString(R.string.text, secondsElapsed++)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textSecondsElapsed = findViewById(R.id.textSecondsElapsed)
        sharedPref = getSharedPreferences("Seconds elapsed", Context.MODE_PRIVATE)
        backgroundThread.start()
    }

    override fun onStart() {
        onScreen = true
        secondsElapsed = sharedPref.getInt("Seconds elapsed", secondsElapsed)
        super.onStart()
    }

    override fun onStop() {
        onScreen = false
        with(sharedPref.edit()) {
            putInt("Seconds elapsed", secondsElapsed)
            apply()
        }
        super.onStop()
    }
}