package com.sgalera.gaztelubira.domain.manager

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.sgalera.gaztelubira.domain.model.Credentials

class SharedPreferences(context: Context) {

    lateinit var credentials: Credentials

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("credentials", Context.MODE_PRIVATE)

    private fun saveCredentials(credentials: Credentials) {
        with(sharedPreferences.edit()) {
            putBoolean("isAdmin", credentials.isAdmin)
            putString("player", credentials.player)
            putInt("year", credentials.year)
            apply()
        }
    }

    fun manageAdminLogIn(logged: Boolean) {
        credentials.isAdmin = logged
        saveCredentials(credentials)
    }

    fun manageSeason(season: Int) {
        credentials.year = season
        saveCredentials(credentials)
    }

    fun getCredentials(): Boolean {
        return try {
            credentials = Credentials(
                isAdmin = sharedPreferences.getBoolean("isAdmin", false),
                player = sharedPreferences.getString("player", "") ?: "",
                year = sharedPreferences.getInt("year", 2024),
            )
            true
        } catch (e: Exception) {
            Log.i("SharedPreferences", "Error fetching credentials")
            false
        }
    }
}