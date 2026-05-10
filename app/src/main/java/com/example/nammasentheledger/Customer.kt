package com.example.nammasentheledger

data class Customer(

    val name: String,
    val phone: String,
    var balance: Double = 0.0,
    var transactions: MutableList<Transaction> = mutableListOf()
)