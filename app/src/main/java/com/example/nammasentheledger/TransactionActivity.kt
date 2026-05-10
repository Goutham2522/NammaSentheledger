package com.example.nammasentheledger

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class TransactionActivity : AppCompatActivity() {

    private lateinit var txtBalance: TextView
    private lateinit var txtHistory: TextView

    private lateinit var transactionList: MutableList<Transaction>

    private var balance = 0.0

    private var customerPosition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_transaction)

        val customerName =
            intent.getStringExtra("customerName")

        customerPosition =
            intent.getIntExtra(
                "customerPosition",
                -1
            )

        txtBalance =
            findViewById(R.id.txtBalance)

        txtHistory =
            findViewById(R.id.txtHistory)

        val txtCustomer =
            findViewById<TextView>(R.id.txtCustomer)

        val etAmount =
            findViewById<EditText>(R.id.etAmount)

        val etDelete =
            findViewById<EditText>(R.id.etDelete)

        val btnCredit =
            findViewById<Button>(R.id.btnCredit)

        val btnPayment =
            findViewById<Button>(R.id.btnPayment)

        val btnDelete =
            findViewById<Button>(R.id.btnDelete)

        txtCustomer.text = customerName

        // Load Existing Transactions

        if (!CustomersActivity.customerTransactions.containsKey(customerName)) {

            CustomersActivity.customerTransactions[customerName!!] =
                mutableListOf()
        }

        transactionList =
            CustomersActivity.customerTransactions[customerName]!!

        // Restore Saved Transactions

        if (customerPosition != -1) {

            transactionList =
                CustomersActivity.customerList[customerPosition].transactions

            balance =
                CustomersActivity.customerList[customerPosition].balance
        }

        updateBalance()

        updateHistory()

        // CREDIT

        btnCredit.setOnClickListener {

            val amountText =
                etAmount.text.toString()

            if (amountText.isEmpty()) {

                etAmount.error =
                    "Enter Amount"

            } else {

                val amount =
                    amountText.toDouble()

                balance += amount

                CustomersActivity.totalOutstanding += amount

                CustomersActivity.todaySales += amount

                val transaction =
                    Transaction(
                        customerName ?: "",
                        "CREDIT",
                        amount,
                        getCurrentTime()
                    )

                transactionList.add(transaction)

                CustomersActivity.recentTransactions.add(transaction)

                if (customerPosition != -1) {

                    CustomersActivity.customerList[customerPosition].balance =
                        balance

                    CustomersActivity.customerList[customerPosition].transactions =
                        transactionList
                }

                // SAVE PERMANENTLY

                StorageHelper.saveCustomers(
                    this,
                    CustomersActivity.customerList
                )

                updateBalance()

                updateHistory()

                etAmount.text.clear()

                Toast.makeText(
                    this,
                    "Credit Saved",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // PAYMENT

        btnPayment.setOnClickListener {

            val amountText =
                etAmount.text.toString()

            if (amountText.isEmpty()) {

                etAmount.error =
                    "Enter Amount"

            } else {

                val amount =
                    amountText.toDouble()

                balance -= amount

                CustomersActivity.totalOutstanding -= amount

                val transaction =
                    Transaction(
                        customerName ?: "",
                        "PAYMENT",
                        amount,
                        getCurrentTime()
                    )

                transactionList.add(transaction)

                CustomersActivity.recentTransactions.add(transaction)

                if (customerPosition != -1) {

                    CustomersActivity.customerList[customerPosition].balance =
                        balance

                    CustomersActivity.customerList[customerPosition].transactions =
                        transactionList
                }

                // SAVE PERMANENTLY

                StorageHelper.saveCustomers(
                    this,
                    CustomersActivity.customerList
                )

                updateBalance()

                updateHistory()

                etAmount.text.clear()

                Toast.makeText(
                    this,
                    "Payment Saved",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // DELETE TRANSACTION

        btnDelete.setOnClickListener {

            val deleteText =
                etDelete.text.toString()

            if (deleteText.isEmpty()) {

                etDelete.error =
                    "Enter Transaction ID"

            } else {

                val position =
                    deleteText.toInt()

                if (position < transactionList.size) {

                    val transaction =
                        transactionList[position]

                    if (transaction.type == "CREDIT") {

                        balance -= transaction.amount

                        CustomersActivity.totalOutstanding -=
                            transaction.amount

                    } else {

                        balance += transaction.amount

                        CustomersActivity.totalOutstanding +=
                            transaction.amount
                    }

                    transactionList.removeAt(position)

                    if (customerPosition != -1) {

                        CustomersActivity.customerList[customerPosition].balance =
                            balance

                        CustomersActivity.customerList[customerPosition].transactions =
                            transactionList
                    }

                    // SAVE PERMANENTLY

                    StorageHelper.saveCustomers(
                        this,
                        CustomersActivity.customerList
                    )

                    updateBalance()

                    updateHistory()

                    Toast.makeText(
                        this,
                        "Transaction Deleted",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {

                    Toast.makeText(
                        this,
                        "Invalid Transaction ID",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun updateBalance() {

        txtBalance.text =
            "Balance: ₹$balance"
    }

    private fun updateHistory() {

        var history = ""

        for (i in transactionList.indices) {

            val transaction =
                transactionList[i]

            history +=
                "ID: $i\n" +
                        "${transaction.type}  ₹${transaction.amount}\n" +
                        "${transaction.dateTime}\n\n"
        }

        txtHistory.text = history
    }

    private fun getCurrentTime(): String {

        val sdf =
            SimpleDateFormat(
                "dd MMM yyyy, hh:mm a",
                Locale.getDefault()
            )

        return sdf.format(Date())
    }
}