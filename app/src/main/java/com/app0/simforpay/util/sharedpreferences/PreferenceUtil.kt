package com.app0.simforpay.util.sharedpreferences

import android.content.Context
import android.content.SharedPreferences

class PreferenceUtil(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("prefs_name", Context.MODE_PRIVATE)

    fun getString(key: String, defValue: String): String {
        return prefs.getString(key, defValue).toString()
    }
    fun setString(key: String, str: String) {
        prefs.edit().putString(key, str).apply()
    }
    fun getInt(key: String, defValue: Int): Int {
        return prefs.getInt(key, defValue)
    }
    fun setInt(key: String, int: Int ) {
        prefs.edit().putInt(key, int).apply()
    }
    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return prefs.getBoolean(key, defValue)
    }
    fun setBoolean(key: String, bool: Boolean) {
        prefs.edit().putBoolean(key, bool).apply()
    }
    fun remove(key: String) {
        prefs.edit().remove(key).apply()
    }
    fun clear() {
        prefs.edit().clear().apply()
    }
}