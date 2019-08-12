package com.sunthemart.deliverypartner.view.ui.utils

import com.google.android.material.textfield.TextInputLayout
import java.util.regex.Matcher
import java.util.regex.Pattern

class Validation {

    private val emailPattern = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"
    private val numberPattern = "((\\+*)((0[ -]+)*|(91 )*)(\\d{12}+|\\d{10}+))|\\d{5}([- ]*)\\d{6}"
    private lateinit var matcher: Matcher

    fun validateEmail(mTILEmail: TextInputLayout): Boolean {
        val emailInput = mTILEmail.editText?.text.toString().trim()
        matcher = Pattern.compile(emailPattern).matcher(emailInput)
        return when {
            emailInput.isEmpty() -> {
                mTILEmail.error = "Field can't be empty"
                false
            }
            !matcher.matches() -> {
                mTILEmail.error = "enter correct email"
                matcher.matches()
            }
            else -> {
                mTILEmail.error = null
                true
            }
        }
    }

    fun validateNumber(mTILNumber: TextInputLayout): Boolean {
        val phoneInput = mTILNumber.editText?.text.toString().trim()
        matcher = Pattern.compile(numberPattern).matcher(phoneInput)
        return when {
            phoneInput.isEmpty() -> {
                mTILNumber.error = "Field can't be empty"
                false
            }
            !matcher.matches() -> {
                mTILNumber.error = "Enter correct Phone number"
                matcher.matches()
            }
            else -> {
                mTILNumber.error = null
                true
            }
        }
    }

    fun validate(mTILName: TextInputLayout): Boolean {
        val userInput = mTILName.editText?.text.toString().trim()

        return when {
            userInput.isEmpty() -> {
                mTILName.error = "Field can't be empty"
                false
            }
            else -> {
                mTILName.error = null
                true
            }
        }
    }

}