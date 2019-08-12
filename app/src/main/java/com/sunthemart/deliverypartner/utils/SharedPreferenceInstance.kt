package com.sunthemart.deliverypartner.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log


object SharedPreferenceInstance {

    private val APP_PREF = "APP_PREF"
    private lateinit var preferences: SharedPreferences


    fun init(context: Context) {
        preferences = context.getSharedPreferences(APP_PREF, MODE_PRIVATE)
        Log.i("SPInstance class : ", "prefrences init")
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    // for string
    fun getStringValue(preference: String): String? {
        return preferences.getString(preference, null)
    }

    fun setStringValue(newValue: String, preference: String) {
        preferences.edit {
            it.putString(preference, newValue)
        }
    }

    // for boolean
    fun getBooleanValue(preference: String): Boolean? {
        return preferences.getBoolean(preference, false)
    }

    fun setBooleanValue(newValue: Boolean, preference: String) {
        preferences.edit {
            it.putBoolean(preference, newValue)
        }
    }

    // for float
    fun getFloatValue(preference: String): Float? {
        return preferences.getFloat(preference, 0.0f)
    }

    fun setFloatValue(newValue: Float, preference: String) {
        preferences.edit {
            it.putFloat(preference, newValue)
        }
    }


    // for int
    fun getIntValue(preference: String): Int? {
        return preferences.getInt(preference, 0)
    }

    fun setIntValue(newValue: Int, preference: String) {
        preferences.edit {
            it.putInt(preference, newValue)
        }
    }

    // for long
    fun getLongValue(preference: String): Long? {
        return preferences.getLong(preference, 0)
    }

    fun setLongValue(newValue: Long, preference: String) {
        preferences.edit {
            it.putLong(preference, newValue)
        }
    }

    // for string set
    fun getStringSetValue(preference: String): Set<String>? {
        return preferences.getStringSet(preference, null)
    }

    fun setStringSetValue(newValue: Set<String>?, preference: String) {
        preferences.edit {
            it.putStringSet(preference, newValue)
        }
    }

    // remove and clear all
    fun clearAll() {
        preferences.edit().clear().apply()
    }

    fun remove(preference: String) {
        preferences.edit().remove(preference).apply()
    }
}