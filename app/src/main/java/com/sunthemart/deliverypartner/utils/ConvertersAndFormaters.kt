package com.sunthemart.deliverypartner.utils

import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

object ConvertersAndFormaters {


    fun getAddress(addressObject: JSONObject): String {

        val address = addressObject.getString("House_no") + "," + addressObject.getString("Appartment_Name") + "," +
                addressObject.getString("Street") + "," + addressObject.getString("Area") + " - " + addressObject.getString("Pincode") + "\nLandmark: " +
                addressObject.getString("Landmark")


        return address.take(75) + " ..."
    }

    fun getTimeInAmPm(input: String): String {

        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        val outputFormat = SimpleDateFormat("KK:mm a", Locale.ENGLISH)

        return outputFormat.format(inputFormat.parse(input))
    }
}