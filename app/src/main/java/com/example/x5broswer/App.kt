package com.example.x5broswer

import android.app.Application
import com.blankj.utilcode.util.Utils

/**
 * Created by tuyrt7 on 2021/5/31.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Utils.init(this)
    }
}