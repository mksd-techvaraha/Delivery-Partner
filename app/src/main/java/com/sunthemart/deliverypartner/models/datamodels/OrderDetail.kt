package com.sunthemart.deliverypartner.models.datamodels

/**
 * This shows detailed order details
 */
data class OrderDetail(
        /**
         * stores deliverBeforeTime
         */
        val deliverBeforeDate: String,
        /**
         * stores address of customer
         */
        val address: String,
        /**
         * latitude of the address
         */
        val latitude: String,
        /**
         * longitude of the address
         */
        val longitude: String,
        /**
         * order number
         */
        val orderNo: String,
        /**
         * total amount of order
         */
        val totalAmount: Int,
        /**
         * customer's phones number
         */
        val custPhone: String,
        /**
         * customer name
         */
        val custName: String,
        /**
         * paymentStatus ie cash or card
         */
        val paymentStatus: String,
        /**
         * product list
         */
        val productList: ArrayList<Product>
)