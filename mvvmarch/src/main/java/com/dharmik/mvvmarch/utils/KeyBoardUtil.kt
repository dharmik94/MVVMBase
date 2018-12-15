package com.dharmik.mvvmarch.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

class KeyBoardUtil private constructor(private val context: Context?) {
    private var inputManager: InputMethodManager? = null

    private fun hideSoftKeyboard(v: View) {
        if (context != null) {
            inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        }
        inputManager!!.hideSoftInputFromWindow(v.getWindowToken(), 0)
    }

    companion object {

        fun hideKeyBoard(v: View) {
            getInstance(v.getContext()).hideSoftKeyboard(v)
        }

        private fun getInstance(context: Context): KeyBoardUtil {
            return KeyBoardUtil(context)
        }
    }
}