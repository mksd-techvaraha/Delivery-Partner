package com.sunthemart.deliverypartner.utils

/**
 * This object is used to store the apis
 */
object Apis {

    private const val baseUrl: String = "https://sunthemart.pythonanywhere.com/"

    private const val devOrProd: String = "dev/v1/Agent/"

    /**
     * registeration api
     */
    const val register: String = "$baseUrl${devOrProd}Register"
    /**
     * login api
     */
    const val login: String = "$baseUrl${devOrProd}Login"

    /**
     * orderlist api
     */
    const val orderList: String = "$baseUrl${devOrProd}Order-List"

    /**
     * order details api
     */
    const val orderDetails: String = "$baseUrl${devOrProd}Order-detail"

    /**
     * stock point api
     */
    const val stockPointList: String = "$baseUrl${devOrProd}SP-List"
    /**
     * to update the status of the order
     */
    const val status: String = "$baseUrl${devOrProd}Update-Status"
}