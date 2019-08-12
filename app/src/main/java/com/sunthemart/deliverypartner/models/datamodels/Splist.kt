package com.sunthemart.deliverypartner.models.datamodels

data class Splist(
        val address: String,
        val city: String,
        val inventorySize: Int,
        val latitute: String,
        val longitute: String,
        val managedBy: String,
        val pincode: String,
        val spId: Int,
        val state: String
)