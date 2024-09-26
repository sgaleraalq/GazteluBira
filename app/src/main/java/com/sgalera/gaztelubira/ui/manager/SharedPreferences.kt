package com.sgalera.gaztelubira.ui.manager

import android.content.Context
import android.content.SharedPreferences
import com.sgalera.gaztelubira.domain.model.Credentials

class SharedPreferences(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("token", Context.MODE_PRIVATE)

    fun saveAdminToken(token: String){
        with(sharedPreferences.edit()){
            putString("admin_token", token)
            apply()
        }
    }

    fun savePlayer(player: String){
        with(sharedPreferences.edit()){
            putString("player", player)
            apply()
        }
    }

    fun saveYear(year: Int){
        with(sharedPreferences.edit()){
            putInt("year", year)
            apply()
        }
    }


    fun getCredentials(): Credentials {
        return Credentials(
            isAdmin = sharedPreferences.getBoolean("isAdmin", true),
            player = sharedPreferences.getString("player", "") ?: "",
            year = sharedPreferences.getInt("year", 2023)
        )
    }


    fun getAdminToken(): String {
        return sharedPreferences.getString("admin_token", "") ?: ""
    }
}