package com.sunthemart.deliverypartner.models.datamodels

/**
 * stock pint list model
 */
data class Splist(
        /**
         * address
         */
        val address: String,
        /**
         * city
         */
        val city: String,
        /**
         * inventory size
         */
        val inventorySize: Int,
        /**
         * latitude
         */
        val latitute: String,
        /**
         * longitutde
         */
        val longitute: String,
        /**
         * managed by
         */
        val managedBy: String,
        /**
         * pincode
         */
        val pincode: String,
        /**
         * stock point id
         */
        val spId: Int,
        /**
         * stock point id
         */
        val state: String
)