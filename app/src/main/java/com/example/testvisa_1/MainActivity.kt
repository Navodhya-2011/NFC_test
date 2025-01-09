package com.example.testvisa_1

import android.annotation.SuppressLint
import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

//    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cardNameField = findViewById<EditText>(R.id.card_name)
        val cardNumberField = findViewById<EditText>(R.id.card_number)
        val expiryDateField = findViewById<EditText>(R.id.expiry_date)
        val cvvField = findViewById<EditText>(R.id.cvv)
        val saveCardButton = findViewById<Button>(R.id.btn_save_card)

        var cardType = String

        val cardN = cardNumberField.toString()

        if (cardN.startsWith("4")){
            cardType.equals("visa")
        }
        else if (cardN.startsWith("5")){
            cardType.equals("mastercard")
        }else{
            cardType.equals("unknown")
        }

        val enableNfcButton: Button = findViewById(R.id.btn_enable_nfc)
//        val cardDetailsDisplay = findViewById<TextView>(R.id.card_details_display)
        enableNfcButton.setOnClickListener {
            val nfcAdapter = NfcAdapter.getDefaultAdapter(this)
            if (nfcAdapter == null) {
                Toast.makeText(this, "NFC is not available on this device.", Toast.LENGTH_LONG).show()
            } else if (!nfcAdapter.isEnabled) {
                startActivity(Intent(android.provider.Settings.ACTION_NFC_SETTINGS))
            } else {
                Toast.makeText(this, "NFC is enabled.", Toast.LENGTH_SHORT).show()
            }
        }

        saveCardButton.setOnClickListener {
            val cardName = cardNameField.text.toString()
            val cardNumber = cardNumberField.text.toString()
            val expiryDate = expiryDateField.text.toString()
            val cvv = cvvField.text.toString()

            if (cardNumber.isNotEmpty() && expiryDate.isNotEmpty() && cvv.isNotEmpty()) {
                saveCardDetails(cardName, cardNumber, expiryDate, cvv)
                Toast.makeText(this, "Card saved successfully", Toast.LENGTH_SHORT).show()

                // Start the HCE service
                val intent = Intent(this, MyHostApduService::class.java)
                startService(intent)

                // Display the card details
//                cardDetailsDisplay.text = "Card Name: $cardName\nCard Number: $cardNumber\nExpiry Date: $expiryDate\nCVV: $cvv"
//                cardDetailsDisplay.visibility = TextView.VISIBLE
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        val previewButton = findViewById<Button>(R.id.preview)
        previewButton.setOnClickListener {
            val cardName = cardNameField.text.toString()
            val cardNumber = cardNumberField.text.toString()

            if (cardName.isNotEmpty() && cardNumber.isNotEmpty()) {
                val intent = Intent(this, Card_Preview_Activity::class.java)
                intent.putExtra("CARD_NAME", cardName)
                intent.putExtra("CARD_NUMBER", cardNumber)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please save card details first.", Toast.LENGTH_SHORT).show()
            }
        }

}

    private fun saveCardDetails(cardName: String, cardNumber: String, expiryDate: String, cvv: String) {
        // Save card details securely (this example just stores them in memory)
        val sharedPrefs = getSharedPreferences("CardPrefs", MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.putString("CARD_NAME", cardName)
        editor.putString("CARD_NUMBER", cardNumber)
        editor.putString("EXPIRY_DATE", expiryDate)
        editor.putString("CVV", cvv)
        editor.apply()
    }


}
