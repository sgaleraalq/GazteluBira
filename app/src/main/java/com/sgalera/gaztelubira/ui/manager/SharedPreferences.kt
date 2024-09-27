package com.sgalera.gaztelubira.ui.manager

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.sgalera.gaztelubira.domain.model.Credentials

class SharedPreferences(context: Context) {

    lateinit var credentials: Credentials

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("token", Context.MODE_PRIVATE)

    fun manageAdminLogIn(logged: Boolean) {
        with(sharedPreferences.edit()) {
            putBoolean("isAdmin", logged)
            apply()
        }
    }

    fun saveAdminToken(token: String) {
        with(sharedPreferences.edit()) {
            putString("admin_token", token)
            apply()
        }
    }

    fun savePlayer(player: String) {
        with(sharedPreferences.edit()) {
            putString("player", player)
            apply()
        }
    }

    fun saveYear(year: Int) {
        with(sharedPreferences.edit()) {
            putInt("year", year)
            apply()
        }
    }


    fun getCredentials(): Boolean {
        return try {
            credentials = Credentials(
                isAdmin = sharedPreferences.getBoolean("isAdmin", false),
                player = sharedPreferences.getString("player", "") ?: "",
                year = sharedPreferences.getInt("year", 2024)
            )
            true
        } catch (e: Exception) {
            Log.i("SharedPreferences", "Error fetching credentials")
            false
        }
    }

    fun getAdminToken(): String {
        return sharedPreferences.getString("admin_token", "") ?: ""
    }
}