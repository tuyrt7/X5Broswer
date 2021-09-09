package com.example.x5broswer

import android.app.Application
import android.util.Log
import com.blankj.utilcode.util.Utils
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.QbSdk.PreInitCallback
import com.tencent.smtt.sdk.TbsListener


/**
 * Created by tuyrt7 on 2021/5/31.
 */
class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        Utils.init(this)

        x5init()
    }

    private fun x5init() {
        // 在调用TBS初始化、创建WebView之前进行如下配置
//        val map = HashMap<String, Any>()
//        map[TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER] = true
//        map[TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE] = true
//        QbSdk.initTbsSettings(map)

        // QbSdk.setDownloadWithoutWifi(true)
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
//        val cb: PreInitCallback = object : PreInitCallback {
//            override fun onViewInitFinished(p0: Boolean) {
//                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
//                Log.e("QbSdk", "X5 内核加载成功: $p0")
//            }
//
//            override fun onCoreInitFinished() {
//
//            }
//        }
//        // x5内核初始化接口
//        QbSdk.initX5Environment(this, cb)
//        QbSdk.setTbsListener(object : TbsListener {
//
//            override fun onDownloadFinish(p0: Int) {
//                Log.e("QbSdk", "onDownloadFinish进度：$p0")
//            }
//
//            override fun onInstallFinish(p0: Int) {
//                Log.e("QbSdk", "onInstallFinish进度：$p0")
//            }
//
//            override fun onDownloadProgress(p0: Int) {
//                Log.e("QbSdk", "onDownloadProgress进度：$p0")
//            }
//        })

    }
}