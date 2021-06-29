package com.example.x5broswer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.x5broswer.utils.log

/**
 * Created by tuyrt7 on 2021/5/31.
 */
class MainViewModel : ViewModel() {

    val url = MutableLiveData<String>()

    fun initData() {
        url.value = Settings.webUrl
    }

    fun saveUrl() {
        val webUrl = url.value.toString()
        if (webUrl.isNotEmpty()) {
            log("保存地址：$webUrl")
            Settings.webUrl = webUrl
        }
    }

}