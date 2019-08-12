package com.sunthemart.deliverypartner.models.datamodels

/**
 * this is used to store product list
 */
data class Product(
        /**
         * stores amount of single product
         */
        val amount: Double,
        /**
         * stores item name
         */
        val item: String,
        /**
         * sotes quantity type
         */
        val quantityType: String,
        /**
         * how much is the quantity
         */
        val quantity: Double
)