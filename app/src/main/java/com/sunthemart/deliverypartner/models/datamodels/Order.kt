package com.sunthemart.deliverypartner.models.datamodels

/**
 * This is to display order brief detail
 */
data class Order(
        /**
         * stores deliverBeforeTime
         */
        val deliverBeforeDate: String,
        /**
         * address of the customer
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
         * order no
         */
        val orderNo: String
)