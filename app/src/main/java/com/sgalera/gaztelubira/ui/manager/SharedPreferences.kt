package com.sgalera.gaztelubira.ui.manager

import android.content.Context
import android.content.SharedPreferences

class SharedPreferences(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("token", Context.MODE_PRIVATE)

    fun saveAdminToken(token: String){
        with(sharedPreferences.edit()){
            putString("admin_token", token)
            apply()
        }
    }

    fun getAdminToken(): String? {
        return sharedPreferences.getString("admin_token", null)
    }
}