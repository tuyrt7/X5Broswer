package com.example.x5broswer.utils

import android.util.Log
import android.widget.Toast

/**
 * Created by tuyrt7 on 2021/5/31.
 */

fun toast(message: Any?) {
    Toast.makeText(AppGlobals.getApplication(),message.toString(),Toast.LENGTH_SHORT).show()
}

fun log(message: Any?) {
    Log.d("aaaa", message.toString())
}