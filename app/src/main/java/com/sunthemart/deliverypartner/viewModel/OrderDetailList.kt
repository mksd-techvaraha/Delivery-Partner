package com.sunthemart.deliverypartner.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sunthemart.deliverypartner.models.datamodels.OrderDetail
import com.sunthemart.deliverypartner.models.repo.OrderDetailsLiveData

/**
 * presenter for orderDetail activity
 */
class OrderDetailList(application: Application) : AndroidViewModel(application) {

    /**
     * orderListObservable
     */
    private lateinit var orderListObservable: MutableLiveData<OrderDetail>

    /**
     * to get orderlistobservable
     */
    fun getOrderDetailListObservable(orderNumber: String): LiveData<OrderDetail> {

        orderListObservable = OrderDetailsLiveData().loadOrderDetailData(getApplication(), orderNumber)
        return orderListObservable
    }


}