package com.sunthemart.deliverypartner.view.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.sunthemart.deliverypartner.R
import com.sunthemart.deliverypartner.models.datamodels.Order
import com.sunthemart.deliverypartner.utils.ConvertersAndFormaters
import com.sunthemart.deliverypartner.view.ui.activity.DeliveryDetailActivity
import com.sunthemart.deliverypartner.view.ui.activity.DeliveryStartedActivity
import kotlinx.android.synthetic.main.item_bottom_sheet.*

/**
 * to display orders data
 */
class OrdersFragment : Fragment() {

    /**
     * create view for fragment
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        val view = inflater.inflate(R.layout.item_bottom_sheet, container, false)
        val orderNumber: TextView = view.findViewById(R.id.xTvSPName)
        val deliverBeforeTime: TextView = view.findViewById(R.id.xTvDeliveryBefore)
        val seeDetails: Button = view.findViewById(R.id.xBtnSeeDetails)
        val address: TextView = view.findViewById(R.id.xTvAddress)
        val startDelivery: Button = view.findViewById(R.id.xBtnStartDelivery)

        val args = arguments
        if (args != null) {
            orderNumber.text = args.getString("orderNumber")
            deliverBeforeTime.text = args.getString("time")?.let { ConvertersAndFormaters.getTimeInAmPm(it) }
            seeDetails.setOnClickListener {
                val intent: Intent = Intent(activity, DeliveryDetailActivity::class.java)
                intent.putExtra("orderId", args.getString("orderNumber"))
                startActivity(intent)
            }
            address.text = args.getString("address")

            startDelivery.setOnClickListener {
                val intent: Intent = Intent(activity, DeliveryStartedActivity::class.java)
                intent.putExtra("orderId", args.getString("orderNumber"))
                startActivity(intent)
            }
        }

        return view
    }

    companion object {

        /**
         * creates fragment instance
         */
        fun newInstance(items: Order): OrdersFragment {

            val args = Bundle()
            args.putString("orderNumber", items.orderNo)
            args.putString("time", items.deliverBeforeDate)
            args.putString("address", items.address)
            args.putString("latitude", items.latitude)
            args.putString("longitude", items.longitude)
            val fragment = OrdersFragment()
            fragment.arguments = args
            return fragment
        }
    }
}