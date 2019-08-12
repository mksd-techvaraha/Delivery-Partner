package com.sunthemart.deliverypartner.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sunthemart.deliverypartner.R
import com.sunthemart.deliverypartner.models.datamodels.Order
import com.sunthemart.deliverypartner.utils.ConvertersAndFormaters
import com.sunthemart.deliverypartner.view.ui.activity.DeliveryDetailActivity
import com.sunthemart.deliverypartner.view.ui.activity.DeliveryStartedActivity

/**
 * list adapter
 */
class OrderListAdapter(
        private val items: ArrayList<Order>,
        /**
         * activity context
         */
        val context: Context
) : RecyclerView.Adapter<OrderListAdapter.ViewHolder>() {

    /**
     * to create view
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_bottom_sheet, parent, false))
    }

    /**
     * to bind the view with data
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.orderNumber.text = items[position].orderNo
        holder.deliverBeforeTime.text = ConvertersAndFormaters.getTimeInAmPm(items[position].deliverBeforeDate)
        holder.address.text = items[position].address
        holder.seeDetails.setOnClickListener {
            val intent: Intent = Intent(context, DeliveryDetailActivity::class.java)
            intent.putExtra("orderId", items[position].orderNo)
            context.startActivity(intent)
        }
        holder.startDelivery.setOnClickListener {
            val intent: Intent = Intent(context, DeliveryStartedActivity::class.java)
            intent.putExtra("orderId", items[position].orderNo)
            context.startActivity(intent)
        }
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

        var orderNumber: TextView = view.findViewById(R.id.xTvSPName)
        var deliverBeforeTime: TextView = view.findViewById(R.id.xTvDeliveryBefore)
        var seeDetails: Button = view.findViewById(R.id.xBtnSeeDetails)
        var address: TextView = view.findViewById(R.id.xTvAddress)
        var startDelivery: Button = view.findViewById(R.id.xBtnStartDelivery)
    }
}

