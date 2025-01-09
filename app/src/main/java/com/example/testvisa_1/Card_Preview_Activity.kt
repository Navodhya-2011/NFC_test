package com.example.testvisa_1

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Card_Preview_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_card_preview)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val cardNameTextView = findViewById<TextView>(R.id.cardHolderName)
        val cardNumberTextView = findViewById<TextView>(R.id.cardNumber)

        // Retrieve data from intent
        val cardName = intent.getStringExtra("CARD_NAME")
        val cardNumber = intent.getStringExtra("CARD_NUMBER")

        // Display data
        cardNameTextView.text = cardName
        cardNumberTextView.text = cardNumber
    }

}