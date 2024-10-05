package com.sgalera.gaztelubira.domain.manager

import com.sgalera.gaztelubira.BuildConfig
import java.security.MessageDigest

class PasswordManager {

    private val adminPassword = BuildConfig.ADMIN_PASSWORD

    fun checkPassword(password: String): Boolean {
        val enteredHashedPassword = hashPassword(password)
        return enteredHashedPassword == hashPassword(adminPassword)
    }

    private fun hashPassword(password: String): String {
        val bytes = password.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }
}
