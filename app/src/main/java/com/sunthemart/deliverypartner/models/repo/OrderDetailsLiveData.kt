package com.sunthemart.deliverypartner.models.repo

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.bunkguru.registration.utils.VolleySingleton
import com.sunthemart.deliverypartner.BuildConfig
import com.sunthemart.deliverypartner.models.datamodels.OrderDetail
import com.sunthemart.deliverypartner.models.datamodels.Product
import com.sunthemart.deliverypartner.utils.Apis
import com.sunthemart.deliverypartner.utils.ConvertersAndFormaters
import com.sunthemart.deliverypartner.utils.SharedPreferenceInstance
import com.sunthemart.deliverypartner.view.ui.utils.AlertDialogs
import com.sunthemart.deliverypartner.view.ui.utils.NetworkStatus
import org.json.JSONObject

/**
 * Livedata loading for pending Orders list
 */
class OrderDetailsLiveData {
    private val tag = "OrderDetailLiveData"

    /**
     * To load the order detailed layout
     */
    fun loadOrderDetailData(context: Context, orderId: String): MutableLiveData<OrderDetail> {

        val ordersLiveData: MutableLiveData<OrderDetail> = MutableLiveData()

        if (!NetworkStatus(context).isNetworkAvailable(context)) {
            val alert = AlertDialogs(
                    context, "Please check Your Internet Connectivity",
                    "Thank you have a Nice Day", "Open Status bar and check", true
                    , enableNegativeButton = false
            )

            alert.showAlert()
            Log.i(tag, "network error")

        } else {

            val jsonString = "{" +
                    "\"order_id\" : \"$orderId\"," +
                    "\"Agent_Id\" : \"${SharedPreferenceInstance.getStringValue("agentId")}\"," +
                    "\"Auth_Token\" : \"${SharedPreferenceInstance.getStringValue("authId")}\"" +
                    "}"
            val jsonObj = JSONObject(jsonString)


            val url = Apis.orderDetails

            Log.i(tag, "starting request $jsonObj")

            val request = JsonObjectRequest(Request.Method.POST, url, jsonObj, Response.Listener { response ->
                val obj = JSONObject(response.toString())

                when (obj.get("Message")) {
                    "Ok" -> {

                        val data = obj.getJSONObject("Data")
                        val addressObject = data.getJSONObject("Delivery_details")

                        val orders = OrderDetail(
                                ConvertersAndFormaters.getTimeInAmPm(data.getString("Delivery_before_date")),
                                ConvertersAndFormaters.getAddress(addressObject),
                                addressObject.getString("Latitude"),
                                addressObject.getString("Longitude"),
                                data.getString("Order_No"),
                                data.getInt("total_amt"),
                                addressObject.getString("Phone_no"),
                                addressObject.getString("Name"),
                                addressObject.getString("Payment_Option"),
                                productList(data)
                        )
                        ordersLiveData.value = orders
                    }
                    "No Order Available" -> {
                        Log.i(tag, obj.get("Message").toString())
                        val alert = AlertDialogs(
                                context,
                                "Sorry but currently there are no orders to finish",
                                " ",
                                "Please try again later",
                                enablePositiveButton = false
                                ,
                                enableNegativeButton = false
                        )
                        ordersLiveData.value = null

                    }
                    else -> {
                        Log.i(tag, obj.get("Message").toString())
                        Toast.makeText(
                                context,
                                obj.get("Server not responding, please try again later").toString(),
                                Toast.LENGTH_LONG
                        ).show()

                        ordersLiveData.value = null
                    }
                }
            }, Response.ErrorListener {
                if (BuildConfig.DEBUG) {
                    Log.i(tag, "Volley error $it")
                }
                ordersLiveData.value = null
            })
            request.retryPolicy = DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
            VolleySingleton.getInstance(context).addToRequestQueue(request)

        }
        return ordersLiveData
    }

    fun productList(data: JSONObject): ArrayList<Product> {
        val product = ArrayList<Product>()
        val productList = data.getJSONArray("order_list")

        for (orders in 0 until productList.length()) {

            val item = productList.getJSONObject(orders)
            product.add(
                    Product(
                            item.getDouble("amount"),
                            item.getString("item"),
                            item.getString("quantit_type"),
                            item.getDouble("quantity")
                    )
            )
        }

        return product
    }
}