package com.vaiwork.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Button Search anonimous class
        val buttonSearch = findViewById<Button>(R.id.button_search)
        val buttonSearchClickListener : View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(this@MainActivity, "Нажата кнопка поиск!", Toast.LENGTH_LONG).show()
            }
        }
        buttonSearch.setOnClickListener(buttonSearchClickListener)

        // Button Media lambda
        val buttonMedia = findViewById<Button>(R.id.button_media)
        buttonMedia.setOnClickListener {
            Toast.makeText(this@MainActivity, "Нажата кнопка медиа!", Toast.LENGTH_LONG).show()
        }

        // Button Settings lambda
        val buttonSettings = findViewById<Button>(R.id.button_settings)
        buttonSettings.setOnClickListener {
            Toast.makeText(this@MainActivity, "Нажата кнопка настройки!", Toast.LENGTH_LONG).show()
        }
    }
}