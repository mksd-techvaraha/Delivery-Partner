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
import com.sunthemart.deliverypartner.models.datamodels.Order
import com.sunthemart.deliverypartner.utils.Apis
import com.sunthemart.deliverypartner.utils.ConvertersAndFormaters
import com.sunthemart.deliverypartner.utils.SharedPreferenceInstance
import com.sunthemart.deliverypartner.view.ui.utils.AlertDialogs
import com.sunthemart.deliverypartner.view.ui.utils.NetworkStatus
import org.json.JSONObject

/**
 * Livedata loading for pending Orders list
 */
class OrdersLiveData {
    private val tag = "OrdersLiveData"

    fun loadOrderData(context: Context, latitude: String, longitude: String): MutableLiveData<ArrayList<Order>> {

        val ordersLiveData: MutableLiveData<ArrayList<Order>> = MutableLiveData()

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
                    "\"Latitute\" : \"$latitude\"," +
                    "\"Longitute\" : \"$longitude\"," +
                    "\"Agent_Id\" : \"${SharedPreferenceInstance.getStringValue("agentId")}\"," +
                    "\"Auth_Token\" : \"${SharedPreferenceInstance.getStringValue("authId")}\"" +
                    "}"
            val jsonObj = JSONObject(jsonString)


            val url = Apis.orderList

            Log.i(tag, "starting request $jsonObj")

            val request = JsonObjectRequest(Request.Method.POST, url, jsonObj, Response.Listener { response ->
                val obj = JSONObject(response.toString())

                when (obj.get("Message")) {
                    "Ok" -> {
                        val dataArray = obj.getJSONArray("Data")
                        val orders = ArrayList<Order>()

                        Log.i(tag, "got the response")
                        for (data in 0 until dataArray.length()) {
                            val item = dataArray.getJSONObject(data)
                            val addressObject = item.getJSONObject("Delivery_details")

                            val address = ConvertersAndFormaters.getAddress(addressObject)

                            orders.add(
                                    Order(
                                            item.getString("Delivery_before_date"),
                                            address,
                                            item.getString("Latitude"),
                                            item.getString("Longitude"),
                                            item.getString("Order_no")
                                    )
                            )
                        }

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
}