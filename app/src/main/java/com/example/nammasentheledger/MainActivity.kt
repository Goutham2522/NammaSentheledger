package com.example.nammasentheledger

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var txtOutstanding: TextView
    private lateinit var txtSales: TextView
    private lateinit var transactionsContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // Cards

        val addCustomerCard =
            findViewById<CardView>(R.id.addCustomerCard)

        val addTransactionCard =
            findViewById<CardView>(R.id.addTransactionCard)

        // Bottom Navigation

        val bottomNav =
            findViewById<BottomNavigationView>(R.id.bottomNav)

        // Dashboard

        txtOutstanding =
            findViewById(R.id.txtOutstanding)

        txtSales =
            findViewById(R.id.txtSales)

        // Recent Transactions Container

        transactionsContainer =
            findViewById(R.id.recentTransactionsContainer)

        // Update Dashboard

        updateDashboard()

        // Add Customer

        addCustomerCard.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    AddCustomerActivity::class.java
                )
            )
        }

        // Add Transaction

        addTransactionCard.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    CustomersActivity::class.java
                )
            )
        }

        // Bottom Navigation

        bottomNav.selectedItemId =
            R.id.nav_home

        bottomNav.setOnItemSelectedListener {

            when (it.itemId) {

                R.id.nav_home -> {

                    true
                }

                R.id.nav_customers -> {

                    startActivity(
                        Intent(
                            this,
                            CustomersActivity::class.java
                        )
                    )

                    true
                }

                else -> false
            }
        }
    }

    override fun onResume() {

        super.onResume()

        updateDashboard()
    }

    private fun updateDashboard() {

        // Dashboard Values

        txtOutstanding.text =
            "₹${CustomersActivity.totalOutstanding}"

        txtSales.text =
            "Today's Sales: ₹${CustomersActivity.todaySales}"

        // Clear Old Views

        transactionsContainer.removeAllViews()

        // Reverse List

        val reversedList =
            CustomersActivity.recentTransactions.reversed()

        // No Transactions

        if (reversedList.isEmpty()) {

            val emptyText =
                TextView(this)

            emptyText.text =
                "No Transactions Yet"

            emptyText.textSize = 18f

            emptyText.setTextColor(Color.GRAY)

            transactionsContainer.addView(emptyText)

            return
        }

        // Show All Transactions

        for (transaction in reversedList) {

            // Card

            val cardView =
                CardView(this)

            val params =
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )

            params.setMargins(0, 20, 0, 0)

            cardView.layoutParams = params

            cardView.radius = 24f

            cardView.cardElevation = 5f

            cardView.setCardBackgroundColor(
                Color.parseColor("#FFF7F2")
            )

            // Layout

            val layout =
                LinearLayout(this)

            layout.orientation =
                LinearLayout.VERTICAL

            layout.setPadding(
                35,
                30,
                35,
                30
            )

            // Title

            val title =
                TextView(this)

            title.text =
                "${transaction.customerName} - ${transaction.type}"

            title.textSize = 18f

            title.setTextColor(
                Color.parseColor("#222222")
            )

            // Date

            val date =
                TextView(this)

            date.text =
                transaction.dateTime

            date.textSize = 14f

            date.setTextColor(Color.GRAY)

            // Amount

            val amount =
                TextView(this)

            amount.text =
                "₹${transaction.amount}"

            amount.textSize = 22f

            // Colors

            if (transaction.type == "CREDIT") {

                amount.setTextColor(
                    Color.parseColor("#E53935")
                )

            } else {

                amount.setTextColor(
                    Color.parseColor("#43A047")
                )
            }

            // Add Views

            layout.addView(title)

            layout.addView(date)

            layout.addView(amount)

            cardView.addView(layout)

            transactionsContainer.addView(cardView)
        }
    }
}