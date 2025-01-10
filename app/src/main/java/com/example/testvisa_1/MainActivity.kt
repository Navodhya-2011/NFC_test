package com.example.testvisa_1

import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private var cardType: String = "Unknown"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cardNameField = findViewById<EditText>(R.id.card_name)
        val cardNumberField = findViewById<EditText>(R.id.card_number)
        val expiryDateField = findViewById<EditText>(R.id.expiry_date)
        val cvvField = findViewById<EditText>(R.id.cvv)
        val saveCardButton = findViewById<Button>(R.id.btn_save_card)
        val viewCardsButton = findViewById<Button>(R.id.btn_view_cards)
        val previewButton = findViewById<Button>(R.id.preview)

        // NFC enable button
        val enableNfcButton: Button = findViewById(R.id.btn_enable_nfc)
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

        // Save Card Button
        saveCardButton.setOnClickListener {
            val cardName = cardNameField.text.toString()
            val cardNumber = cardNumberField.text.toString()
            val expiryDate = expiryDateField.text.toString()
            val cvv = cvvField.text.toString()

            determineCardType(cardNumber)

            if (cardNumber.isNotEmpty() && expiryDate.isNotEmpty() && cvv.isNotEmpty()) {
                saveCardDetails(cardName, cardNumber, expiryDate, cvv, cardType)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        // View Cards Button
        viewCardsButton.setOnClickListener {
            startActivity(Intent(this, ViewCardsActivity::class.java))
        }

        // Preview Button
        previewButton.setOnClickListener {
            val cardName = cardNameField.text.toString()
            val cardNumber = cardNumberField.text.toString()
            val expiryDate = expiryDateField.text.toString()

            determineCardType(cardNumber)

            if (cardName.isNotEmpty() && cardNumber.isNotEmpty()) {
                val intent = Intent(this, Card_Preview_Activity::class.java)
                intent.putExtra("CARD_NAME", cardName)
                intent.putExtra("CARD_NUMBER", cardNumber)
                intent.putExtra("EXPIRY_DATE", expiryDate)
                intent.putExtra("CARD_TYPE", cardType)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun determineCardType(cardNumber: String) {
        cardType = when {
            cardNumber.startsWith("4") -> "Visa"
            cardNumber.startsWith("5") -> "Mastercard"
            cardNumber.startsWith("3") -> "Amex"
            else -> "Unknown"
        }
    }

//    private fun saveCardDetails(cardName: String, cardNumber: String, expiryDate: String, cvv: String, cardType: String) {
//        val cardData = hashMapOf(
//            "cardName" to cardName,
//            "cardNumber" to cardNumber,
//            "expiryDate" to expiryDate,
//            "cvv" to cvv,
//            "cardType" to cardType
//        )
//
//        val db = FirebaseFirestore.getInstance()
//        db.collection("cards")
//            .add(cardData)
//            .addOnSuccessListener {
//                Toast.makeText(this, "Card details saved successfully!", Toast.LENGTH_SHORT).show()
//            }
//            .addOnFailureListener { e ->
//                Toast.makeText(this, "Failed to save card details: ${e.message}", Toast.LENGTH_SHORT).show()
//            }
//    }

    private fun saveCardDetails(cardName: String, cardNumber: String, expiryDate: String, cvv: String, cardType: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            val cardData = mapOf(
                "cardName" to cardName,
                "cardNumber" to cardNumber,
                "expiryDate" to expiryDate,
                "cvv" to cvv,
                "cardType" to cardType,
                "userId" to userId
            )

            val db = FirebaseFirestore.getInstance()
            db.collection("cards")
                .add(cardData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Card details saved successfully!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to save card details: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "User not authenticated. Please log in.", Toast.LENGTH_SHORT).show()
        }
    }

}
