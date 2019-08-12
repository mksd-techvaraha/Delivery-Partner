package com.sunthemart.deliverypartner.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sunthemart.deliverypartner.R
import com.sunthemart.deliverypartner.models.datamodels.Product

/**
 * list adapter
 */
class ProductListAdapter(
        private val items: ArrayList<Product>,
        /**
         * activity context
         */
        val context: Context
) : RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {

    /**
     * to create view
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_order_list, parent, false))
    }

    /**
     * to bind the view with data
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.price.text = "Rs ${items[position].amount}"
        holder.productQuantType.text = "${items[position].quantity} ${items[position].quantityType}"
        holder.productName.text = items[position].item

    }

    /**
     * get list count
     */
    override fun getItemCount(): Int {
        return items.size
    }


    /**
     * class that holds view
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var productName: TextView = view.findViewById(R.id.xTvProduct)
        var productQuantType: TextView = view.findViewById(R.id.xTvQuant)
        var price: TextView = view.findViewById(R.id.xTvPrice)
    }
}

