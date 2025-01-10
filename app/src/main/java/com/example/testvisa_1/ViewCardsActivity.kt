package com.example.testvisa_1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ViewCardsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_cards)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val db = FirebaseFirestore.getInstance()
//        db.collection("cards")
//            .get()
//            .addOnSuccessListener { documents ->
//                val cardList = documents.map { document ->
//                    Card(
//                        document.getString("cardName") ?: "",
//                        document.getString("cardNumber") ?: "",
//                        document.getString("expiryDate") ?: "",
//                        document.getString("cardType") ?: ""
//                    )
//                }
//                recyclerView.adapter = CardAdapter(cardList)
//            }
//            .addOnFailureListener { e ->
//                Toast.makeText(this, "Failed to load cards: ${e.message}", Toast.LENGTH_SHORT).show()
//            }

        db.collection("cards")
            .whereEqualTo("userId", FirebaseAuth.getInstance().currentUser?.uid)
            .get()
            .addOnSuccessListener { documents ->
                val cardList = documents.map { document ->
                    Card(
                        document.getString("cardName") ?: "",
                        document.getString("cardNumber") ?: "",
                        document.getString("expiryDate") ?: "",
                        document.getString("cardType") ?: ""
                    )
                }
                recyclerView.adapter = CardAdapter(cardList)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to load cards: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

}

data class Card(val name: String, val number: String, val expiryDate: String, val type: String)

class CardAdapter(private val cards: List<Card>) : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    class CardViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val cardName: TextView = view.findViewById(R.id.cardHolderName)
        val cardNumber: TextView = view.findViewById(R.id.cardNumber)
        val expiryDate: TextView = view.findViewById(R.id.expDate)
        val cardType: TextView = view.findViewById(R.id.Ctype)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_item, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val card = cards[position]
        holder.cardName.text = card.name
        holder.cardNumber.text = "**** **** **** ${card.number.takeLast(4)}"
        holder.expiryDate.text = card.expiryDate
        holder.cardType.text = card.type
    }

    override fun getItemCount() = cards.size
}
