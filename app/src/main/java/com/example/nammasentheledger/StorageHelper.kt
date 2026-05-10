package com.example.nammasentheledger

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object StorageHelper {

    private const val PREF_NAME = "ledger_pref"
    private const val CUSTOMER_KEY = "customers"

    fun saveCustomers(context: Context, customers: MutableList<Customer>) {

        val prefs =
            context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        val editor = prefs.edit()

        val gson = Gson()

        val json =
            gson.toJson(customers)

        editor.putString(CUSTOMER_KEY, json)

        editor.apply()
    }

    fun loadCustomers(context: Context): MutableList<Customer> {

        val prefs =
            context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        val gson = Gson()

        val json =
            prefs.getString(CUSTOMER_KEY, null)

        val type =
            object : TypeToken<MutableList<Customer>>() {}.type

        return if (json != null) {

            gson.fromJson(json, type)

        } else {

            mutableListOf()
        }
    }
}