package com.sunthemart.deliverypartner.view.ui.activity


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sunthemart.deliverypartner.R
import com.sunthemart.deliverypartner.models.datamodels.OrderDetail
import com.sunthemart.deliverypartner.models.datamodels.Product
import com.sunthemart.deliverypartner.view.adapter.ProductListAdapter
import com.sunthemart.deliverypartner.viewModel.OrderDetailList


/**
 * Delivery detail activity used to show order details
 */
class DeliveryDetailActivity : AppCompatActivity() {


    private lateinit var mIncldDetails: View
    private lateinit var mRecycProductList: RecyclerView
    private lateinit var mTvPaymentStatus: TextView
    private lateinit var mBtnStartDelivery: Button

    // mIncldDetails Include views
    private lateinit var mTvOrderNum: TextView
    private lateinit var mTvDeliverTime: TextView
    //to be set as R.string.hey_you
    private lateinit var mTvAdrsDialoge: TextView
    //customer name
    private lateinit var mTvCustomerName: TextView
    // to be set as customer address
    private lateinit var mTvAdrsCustomer: TextView
    private lateinit var mBtnCallCustomer: Button
    private lateinit var mBtnCustomerDirection: Button

    // set total price
    private lateinit var mTvTotalPrice: TextView
    private lateinit var orderDetailsList: OrderDetailList

    private lateinit var orderDetails: OrderDetail

    private lateinit var productListAdapter: ProductListAdapter
    private lateinit var mProgressBar : View
    /**
     * OnCreate
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery_details)
        initWidget()

        initObjects()

        orderDetailsList = ViewModelProviders.of(this).get(OrderDetailList::class.java)

        val orderNumber = intent.getStringExtra("orderId")
        if (orderNumber != null) {
            observeViewModel(orderDetailsList, orderNumber)
        } else {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        mBtnStartDelivery.setOnClickListener {
            startDelivery()
        }
    }

    private fun initWidget() {
        mIncldDetails = findViewById(R.id.xIncldDetails)
        mRecycProductList = findViewById(R.id.xRecycProductList)
        mTvPaymentStatus = findViewById(R.id.xTvPaymentStatus)
        mBtnStartDelivery = findViewById(R.id.xBtnStartDelivery)

        mTvOrderNum = mIncldDetails.findViewById(R.id.xTvOrderNum)
        mTvDeliverTime = mIncldDetails.findViewById(R.id.xTvDeliverTime)
        mTvAdrsDialoge = mIncldDetails.findViewById(R.id.xTvAdrsDialoge)
        mTvCustomerName = mIncldDetails.findViewById(R.id.xTvCustName)
        mTvAdrsCustomer = mIncldDetails.findViewById(R.id.xTvAdrs)
        mBtnCallCustomer = mIncldDetails.findViewById(R.id.xBtnCall)
        mBtnCallCustomer.visibility = View.GONE
        mBtnCustomerDirection = mIncldDetails.findViewById(R.id.xBtnGetDirection)
        mBtnCustomerDirection.visibility = View.GONE
        mTvTotalPrice = findViewById(R.id.xTvPrice)
        enableButton(false)
        mProgressBar = findViewById(R.id.xProgressBar)

    }

    private fun setUpRecycler(product: ArrayList<Product>) {
        productListAdapter = ProductListAdapter(product, this)
        mRecycProductList.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        mRecycProductList.adapter = productListAdapter
        productListAdapter.notifyDataSetChanged()
    }

    private fun observeViewModel(orderDetailsList: OrderDetailList, orderNumber: String) {

        mProgressBar.visibility = View.VISIBLE
        orderDetailsList.getOrderDetailListObservable(orderNumber)
                .observe(this, Observer<OrderDetail> { orderDetails ->
                    this.orderDetails = orderDetails
                    setUpRecycler(orderDetails.productList)
                    mTvOrderNum.text = "Order# ${orderDetails.orderNo}"
                    mTvDeliverTime.text = "Deliver before - ${orderDetails.deliverBeforeDate}"
                    mTvAdrsDialoge.text = getString(R.string.hey_you)
                    mTvCustomerName.text = "${orderDetails.custName} is your customer"
                    mTvAdrsCustomer.text = orderDetails.address
                    /* Todo : can be used in started delivery activity
                    mBtnCallCustomer.setOnClickListener {
                        startActivity(Intent(Intent.ACTION_DIAL,Uri.parse(
                                "tel:${orderDetails.custPhone}"
                        )))
                    }
                    mBtnCustomerDirection.setOnClickListener {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(
                                "geo:${orderDetails.latitude},${orderDetails.longitude}?z=12"
                        )))
                    }*/
                    mTvTotalPrice.text = "Rs ${orderDetails.totalAmount}"
                    when (orderDetails.paymentStatus) {
                        "cash" -> {
                            mTvPaymentStatus.text = getString(R.string.cash_note)
                        }
                        else -> {
                            mTvPaymentStatus.text = getString(R.string.collect)
                        }
                    }
                    enableButton(true)

                    mProgressBar.visibility = View.GONE
                })
    }

    private fun initObjects() {

    }

    private fun startDelivery() {
        //Todo : start delivery set flag to start delivery
    }

    private fun enableButton(flag: Boolean) {
        if (flag) {
            mBtnStartDelivery.setBackgroundResource(R.drawable.bg_button)
            mBtnStartDelivery.isClickable = false
        } else {
            mBtnStartDelivery.setBackgroundResource(R.drawable.bg_button_disable)
            mBtnStartDelivery.isClickable = true
        }
    }
}