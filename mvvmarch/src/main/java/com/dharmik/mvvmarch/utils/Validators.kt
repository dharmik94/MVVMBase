package com.dharmik.mvvmarch.utils

import android.text.TextUtils
import android.util.Patterns

import java.util.regex.Pattern

object Validators {
    fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidMobile(phone: String): Boolean {
        return phone.length > 9 && Patterns.PHONE.matcher(phone).matches()
    }

    /**
     * Checks for 1 Uppercase Alphabet, 1 Number, 1 Special Character and at least 8 character length
     *
     * @param password String password to validate
     * @return returns true if password is in correct format
     */
    fun isValidPassword(password: String): Boolean {
        var password = password
        password = password.trim { it <= ' ' }
        if (!TextUtils.isEmpty(password)) {
            val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$"

            return Pattern.compile(PASSWORD_PATTERN).matcher(password.trim { it <= ' ' }).matches() && password.length >= 8
        } else {
            return false
        }

    }
}