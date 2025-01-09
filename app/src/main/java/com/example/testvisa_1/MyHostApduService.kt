package com.example.testvisa_1

import android.content.SharedPreferences
import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import java.nio.charset.StandardCharsets

class MyHostApduService : HostApduService() {

    override fun processCommandApdu(commandApdu: ByteArray?, extras: Bundle?): ByteArray {
        val sharedPrefs = getSharedPreferences("CardPrefs", MODE_PRIVATE)
        val cardName = sharedPrefs.getString("CARD_NAME", "")
        val cardNumber = sharedPrefs.getString("CARD_NUMBER", "")
        val expiryDate = sharedPrefs.getString("EXPIRY_DATE", "")
        val cvv = sharedPrefs.getString("CVV", "")

        return if (cardNumber.isNullOrEmpty() || expiryDate.isNullOrEmpty() || cvv.isNullOrEmpty()) {
            // No card data saved
            "ERROR: No card data available".toByteArray(StandardCharsets.UTF_8)
        } else {
            // Simulate sending card data to the POS machine
            val cardData = "CardName:$cardName|CardNumber:$cardNumber|Expiry:$expiryDate|CVV:$cvv"
            cardData.toByteArray(StandardCharsets.UTF_8) + SUCCESS_RESPONSE
        }
    }

    override fun onDeactivated(reason: Int) {
        // Handle deactivation (e.g., user moved phone away from POS)
    }

    companion object {
        private val SUCCESS_RESPONSE = byteArrayOf(0x90.toByte(), 0x00.toByte())
    }
}
