package com.sgalera.gaztelubira.ui.manager

import android.content.Context
import android.content.SharedPreferences
import com.sgalera.gaztelubira.domain.model.Credentials

class SharedPreferences(context: Context) {

    lateinit var credentials: Credentials

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("token", Context.MODE_PRIVATE)

    fun adminLogIn() {
        with(sharedPreferences.edit()) {
            putBoolean("isAdmin", true)
            apply()
        }
    }

    fun adminLogOut() {
        with(sharedPreferences.edit()) {
            putBoolean("isAdmin", false)
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


    fun getCredentials() {
        credentials = Credentials(
            isAdmin = sharedPreferences.getBoolean("isAdmin", false),
            player = sharedPreferences.getString("player", "") ?: "",
            year = sharedPreferences.getInt("year", 2023)
        )
    }


    fun getAdminToken(): String {
        return sharedPreferences.getString("admin_token", "") ?: ""
    }

}