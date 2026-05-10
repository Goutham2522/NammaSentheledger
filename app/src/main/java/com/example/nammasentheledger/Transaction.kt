package com.example.nammasentheledger

data class Transaction(

    val customerName: String,
    val type: String,
    val amount: Double,
    val dateTime: String

)