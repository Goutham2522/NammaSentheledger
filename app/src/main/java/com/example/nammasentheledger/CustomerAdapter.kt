package com.example.nammasentheledger

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class CustomerAdapter(
    private val customerList: List<Customer>
) : RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder>() {

    class CustomerViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView) {

        val cardCustomer: CardView =
            itemView.findViewById(R.id.cardCustomer)

        val txtName: TextView =
            itemView.findViewById(R.id.txtName)

        val txtPhone: TextView =
            itemView.findViewById(R.id.txtPhone)

        val txtBalance: TextView =
            itemView.findViewById(R.id.txtBalance)

        val imgPhone: ImageView =
            itemView.findViewById(R.id.imgPhone)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CustomerViewHolder {

        val view =
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.item_customer,
                    parent,
                    false
                )

        return CustomerViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: CustomerViewHolder,
        position: Int
    ) {

        val customer =
            customerList[position]

        holder.txtName.text =
            customer.name

        holder.txtPhone.text =
            customer.phone

        holder.txtBalance.text =
            "₹${customer.balance}"

        // Balance Color

        if (customer.balance >= 0) {

            holder.txtBalance.setTextColor(
                Color.parseColor("#2E7D32")
            )

        } else {

            holder.txtBalance.setTextColor(
                Color.parseColor("#D32F2F")
            )
        }

        // Open Transaction Screen

        holder.cardCustomer.setOnClickListener {

            val intent =
                Intent(
                    holder.itemView.context,
                    TransactionActivity::class.java
                )

            intent.putExtra(
                "customerName",
                customer.name
            )

            intent.putExtra(
                "customerPosition",
                position
            )

            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {

        return customerList.size
    }
}