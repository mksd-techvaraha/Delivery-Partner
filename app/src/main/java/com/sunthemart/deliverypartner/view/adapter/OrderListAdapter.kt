package com.sunthemart.deliverypartner.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.sunthemart.deliverypartner.models.datamodels.Order
import com.sunthemart.deliverypartner.view.ui.fragments.OrdersFragment

/**
 * list adapter
 */
class OrderListAdapter(fragmentManager: FragmentManager, private val items: ArrayList<Order>) : FragmentPagerAdapter(fragmentManager){
    override fun getItem(position: Int): Fragment {
        return OrdersFragment.newInstance(items[position])
    }

    override fun getCount(): Int {
        return items.size
    }


}