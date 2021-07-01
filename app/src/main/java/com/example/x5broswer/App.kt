package com.example.x5broswer

import android.app.Application
import android.util.Log
import com.blankj.utilcode.util.Utils
import com.tencent.smtt.sdk.QbSdk

/**
 * Created by tuyrt7 on 2021/5/31.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Utils.init(this)

        //QbSdk.setDownloadWithoutWifi(true)
        val cb = object : QbSdk.PreInitCallback {
            override fun onCoreInitFinished() {

            }

            override fun onViewInitFinished(p0: Boolean) {
                Log.d("QbSdk", "onViewInitFinished is $p0")
            }
        }
        // x5内核初始化接口
        QbSdk.initX5Environment(this, cb)
    }
}