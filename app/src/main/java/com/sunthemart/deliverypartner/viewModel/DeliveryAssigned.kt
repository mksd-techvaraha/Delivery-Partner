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
class DeliveryAssigned(application: Application) : AndroidViewModel(application) {

    /**
     * orderListObservable
     */
    private lateinit var stockPointListObservable: MutableLiveData<OrderDetail>

    /**
     * to get orderlistobservable
     */
    fun getStockPointListObservable(orderNumber: String): LiveData<OrderDetail> {

        stockPointListObservable = OrderDetailsLiveData().loadOrderDetailData(getApplication(), orderNumber)
        return stockPointListObservable
    }

    fun getStatusUpdate(): Boolean {
        return false

    }


}