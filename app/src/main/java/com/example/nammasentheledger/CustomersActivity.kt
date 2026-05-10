package com.example.nammasentheledger

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CustomersActivity : AppCompatActivity() {

    companion object {

        val customerList =
            mutableListOf<Customer>()

        val recentTransactions =
            mutableListOf<Transaction>()

        val customerTransactions =
            mutableMapOf<String, MutableList<Transaction>>()

        var totalOutstanding = 0.0

        var todaySales = 0.0
    }

    private lateinit var recyclerView: RecyclerView

    private lateinit var adapter: CustomerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_customers)

        // LOAD SAVED CUSTOMERS

        customerList.clear()

        customerList.addAll(
            StorageHelper.loadCustomers(this)
        )

        // RESTORE TOTAL OUTSTANDING

        totalOutstanding = 0.0

        for (customer in customerList) {

            totalOutstanding += customer.balance

            customerTransactions[customer.name] =
                customer.transactions

            // RESTORE RECENT TRANSACTIONS

            for (transaction in customer.transactions) {

                recentTransactions.add(transaction)
            }
        }

        recyclerView =
            findViewById(R.id.recyclerCustomers)

        recyclerView.layoutManager =
            LinearLayoutManager(this)

        adapter =
            CustomerAdapter(customerList)

        recyclerView.adapter =
            adapter
    }

    override fun onResume() {

        super.onResume()

        adapter.notifyDataSetChanged()
    }
}