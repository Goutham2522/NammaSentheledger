package com.example.nammasentheledger

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hbb20.CountryCodePicker

class AddCustomerActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etPhone: EditText
    private lateinit var btnSave: Button
    private lateinit var ccp: CountryCodePicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add_customer)

        etName = findViewById(R.id.etName)
        etPhone = findViewById(R.id.etPhone)
        btnSave = findViewById(R.id.btnSave)
        ccp = findViewById(R.id.countryCodePicker)

        btnSave.setOnClickListener {

            val name =
                etName.text.toString().trim()

            val phone =
                etPhone.text.toString().trim()

            val countryCode =
                ccp.selectedCountryCode

            // Empty validation

            if (name.isEmpty()) {

                etName.error =
                    "Enter customer name"

                return@setOnClickListener
            }

            if (phone.isEmpty()) {

                etPhone.error =
                    "Enter phone number"

                return@setOnClickListener
            }

            // Name validation

            if (!name.matches(Regex("^[a-zA-Z ]+$"))) {

                etName.error =
                    "Only alphabets allowed"

                return@setOnClickListener
            }

            // Indian phone validation

            if (countryCode == "91") {

                if (phone.length != 10) {

                    etPhone.error =
                        "Indian number must be 10 digits"

                    return@setOnClickListener
                }
            }

            // Other country validation

            if (phone.length < 6) {

                etPhone.error =
                    "Invalid phone number"

                return@setOnClickListener
            }

            val fullPhone =
                "+$countryCode $phone"

            val customer =
                Customer(
                    name,
                    fullPhone,
                    0.0
                )

            // Add customer

            CustomersActivity.customerList.add(customer)

            // SAVE DATA PERMANENTLY

            StorageHelper.saveCustomers(
                this,
                CustomersActivity.customerList
            )

            Toast.makeText(
                this,
                "Customer Saved Successfully",
                Toast.LENGTH_SHORT
            ).show()

            finish()
        }
    }
}