package com.example.x5broswer.utils

import android.os.Build
import android.util.Log
import java.lang.reflect.Method

object WebViewExs {

    private const val TAG = "WebViewExs"

    /**
     *   解决系统应用无法使用WebView 出现的bug:
     *   java.lang.UnsupportedOperationException: For security reasons, WebView is not allowed in privileged processes
     *  如果在使用webView页面使用，在setContentView()之前调用
     */
    fun hookWebView() {
        Log.d(TAG, "Hook start!")
        val sdkInt = Build.VERSION.SDK_INT
        try {
            val factoryClass = Class.forName("android.webkit.WebViewFactory")
            val field = factoryClass.getDeclaredField("sProviderInstance")
            field.isAccessible = true
            var sProviderInstance = field[null]
            if (sProviderInstance != null) {
                Log.d(TAG, "sProviderInstance isn't null")
                return
            }
            val getProviderClassMethod: Method
            getProviderClassMethod = if (sdkInt > 22) { // above 22
                factoryClass.getDeclaredMethod("getProviderClass")
            } else if (sdkInt == 22) { // method name is a little different
                factoryClass.getDeclaredMethod("getFactoryClass")
            } else { // no security check below 22
                Log.d(TAG, "Don't need to Hook WebView")
                return
            }
            getProviderClassMethod.isAccessible = true
            val providerClass = getProviderClassMethod.invoke(factoryClass) as Class<*>
            val delegateClass = Class.forName("android.webkit.WebViewDelegate")
            val providerConstructor = providerClass.getConstructor(delegateClass)
            if (providerConstructor != null) {
                providerConstructor.isAccessible = true
                val declaredConstructor = delegateClass.getDeclaredConstructor()
                declaredConstructor.isAccessible = true
                sProviderInstance = providerConstructor.newInstance(declaredConstructor.newInstance())
                Log.d(TAG, "sProviderInstance: $sProviderInstance")
                field["sProviderInstance"] = sProviderInstance
            }
            Log.d(TAG, "Hook done!")
        } catch (e: Throwable) {
            Log.e(TAG, e.toString())
        }
    }
}