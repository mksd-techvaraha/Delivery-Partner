package com.sunthemart.deliverypartner.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sunthemart.deliverypartner.models.datamodels.Order
import com.sunthemart.deliverypartner.models.repo.OrdersLiveData

/**
 * presenter for orderDetail activity
 */
class OrderList(application: Application) : AndroidViewModel(application) {

    /**
     * orderListObservable
     */
    private lateinit var orderListObservable: MutableLiveData<ArrayList<Order>>

    /**
     * to get orderlistobservable
     */
    fun getOrderListObservable(latitude: String, longitude: String): LiveData<ArrayList<Order>> {

        orderListObservable = OrdersLiveData().loadOrderData(this.getApplication(), latitude, longitude)
        return orderListObservable
    }


}