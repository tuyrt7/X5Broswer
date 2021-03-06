package com.example.x5broswer.web

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.util.Log
import android.webkit.JavascriptInterface
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.blankj.utilcode.util.BarUtils
import com.example.x5broswer.R
import com.example.x5broswer.Settings
import com.example.x5broswer.databinding.ActivityWebBinding
import com.example.x5broswer.utils.WebViewExs
import com.example.x5broswer.utils.log
import com.example.x5broswer.utils.toast
import com.tencent.smtt.export.external.extension.interfaces.IX5WebChromeClientExtension
import com.tencent.smtt.export.external.extension.proxy.ProxyWebChromeClientExtension
import com.tencent.smtt.export.external.interfaces.MediaAccessPermissionsCallback
import com.tencent.smtt.export.external.interfaces.SslError
import com.tencent.smtt.export.external.interfaces.SslErrorHandler
import com.tencent.smtt.sdk.*
import org.jsoup.Jsoup
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by tuyrt7 on 2021/5/31.
 */
class X5WebActivity : AppCompatActivity() {

    companion object {

        private const val TAG = "X5WebActivity"
        private val sdf = SimpleDateFormat("yyyyMMdd_hhmmss", Locale.CHINA)

        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, X5WebActivity::class.java)
            context.startActivity(starter)
        }
    }

    private lateinit var binding: ActivityWebBinding

    //private val x5WebView: WebView by lazy { binding.webview }
    private val webBox by lazy { binding.webContainer }

    private var mUploadCallback: ValueCallback<Array<Uri>>? = null
    private var imageUri: Uri? = null
    private val REQUEST_CODE: Int = 1000

    private val mainHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        BarUtils.setNavBarVisibility(this, false)
        // WebViewExs.hookWebView()
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_web)

        binding.run {
            ivClose.setOnClickListener { finish() }
        }
        //X5???WebView?????????????????????????????????????????????????????????????????????????????????????????????
        if (QbSdk.canLoadX5(applicationContext)) {
            Log.i("TBS_X5", "???????????????????????????");
            createWebView()
        } else {
            Log.i("TBS_X5","?????????")
            Thread {
                val ok: Boolean = QbSdk.preinstallStaticTbs(applicationContext)
                Log.i("TBS_X5", "???????????????$ok")
                runOnUiThread {
                    createWebView()
                }
            }.start()
        }
    }

    private fun createWebView() {
        //????????????WebView??????????????????????????????????????????WebView????????????X5???????????????????????????
        val webView = WebView(this)
        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        webBox.addView(webView, params)

        loadWebView(webView)
    }

    private fun loadWebView(x5WebView: WebView) {
        if (Settings.webUrl.isEmpty()) {
            toast("????????????????????????")
            return
        }
        var url = Settings.webUrl
//        url = "https://cloud.tencent.com/document/product/647/17021"
        url = "https://web.sdk.qcloud.com/trtc/webrtc/demo/latest/official-demo/index.html"
        //url = "https://web.sdk.qcloud.com/trtc/webrtc/demo/detect/index.html"
        //url = "https://alivc-demo-cms.alicdn.com/versionProduct/other/htmlSource/beaconTower/index.html"
        //url = "http://debugtbs.qq.com"
        log("???????????????$url")

        val extension = x5WebView.x5WebViewExtension
        log("X5 extension???$extension")

        val webSettings: WebSettings = x5WebView.settings
        webSettings.javaScriptEnabled = true
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        x5WebView.addJavascriptInterface(InJavaScriptLocalObj(), "local_obj")

        //5.0??????????????????????????????
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        webSettings.setSupportMultipleWindows(true)
        //?????????????????????
        webSettings.useWideViewPort = true
        webSettings.allowContentAccess = true

        webSettings.domStorageEnabled = true
        webSettings.loadsImagesAutomatically = true
        webSettings.loadWithOverviewMode = true
        webSettings.setAppCacheEnabled(true)
        webSettings.blockNetworkImage = false
        webSettings.allowFileAccess = true
        webSettings.mediaPlaybackRequiresUserGesture = false

//        webSettings.setGeolocationEnabled(true);
//        webSettings.setAppCacheMaxSize(Long.MAX_VALUE);
//        webSettings.setPluginState(WebSettings.PluginState.ON_DEMAND);
//        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
//
//        webSettings.setUseWideViewPort(true);
//        webSettings.setSupportZoom(true);
//        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        webSettings.setBuiltInZoomControls(true);
//        webSettings.setDisplayZoomControls(false);

        // ???????????????WebView??????????????????
        x5WebView.webViewClient = MyWebViewClient()
        x5WebView.webChromeClient = MyWebChromeClient()
        //x5WebView.webChromeClientExtension = MyX5WebChromeClientExtension()
        x5WebView.webChromeClientExtension = MyWebChromeClientExtension2()


        //????????????html??????
        //x5Webview.loadUrl("file:///android_asset/hello.html");
        //????????????URL
        x5WebView.loadUrl(url)
    }

    internal inner class MyWebChromeClientExtension2 :ProxyWebChromeClientExtension() {

        override fun onPermissionRequest(origin: String?, l: Long, callback: MediaAccessPermissionsCallback?): Boolean {
             callback?.invoke(origin, l, true) ?: return false
            return true
        }
    }

    internal inner class MyWebViewClient : WebViewClient() {

        /**
         * ??????????????????????????????????????????
         */
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            view?.loadUrl(url)
            return true
        }


        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            Log.d("WebView", "??????????????????")
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            Log.d("WebView", "??????????????????")
            view?.loadUrl(
                "javascript:window.local_obj.onHtml('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');"
            )
        }

        override fun onReceivedError(webView: WebView?, i: Int, s: String?, s1: String?) {
            Log.d("WebView", "***********onReceivedError***********")
            super.onReceivedError(webView, i, s, s1)
        }

        override fun onReceivedSslError(
            view: WebView?,
            handler: SslErrorHandler?,
            error: SslError?
        ) {
            handler?.proceed() // ??????????????????
        }
    }

    internal inner class MyWebChromeClient : WebChromeClient() {

        override fun onShowFileChooser(webView: WebView?, callback: ValueCallback<Array<Uri>>?, params: FileChooserParams?): Boolean {
            mUploadCallback = callback

//            val imagePath = Environment.getExternalStorageDirectory().absolutePath+"/AAA/abc.jpg"
//            callback?.onReceiveValue(arrayOf(Uri.parse(imagePath)))

            pickPhoto()
            return true
        }

        //??????????????????
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
        }

        //??????????????????
        override fun onReceivedTitle(view: WebView?, title: String) {
            super.onReceivedTitle(view, title)
            //????????????Title?????????Activity???title?????????
            //setTitle(title)
        }

        /*  override fun onPermissionRequest(request: PermissionRequest?) {
              runOnUiThread { request?.grant(request.resources) }
          }*/
    }


    private fun pickPhoto() {
        // ?????????????????????????????????????????????
        // ?????????????????????????????????????????????
        val filePath: String =
            (Environment.getExternalStorageDirectory().absolutePath + File.separator + Environment.DIRECTORY_PICTURES + File.separator)
        val fileName = "IMG_" + sdf.format(Date()) + ".jpg"
        imageUri = Uri.fromFile(File(filePath + fileName))

        //Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        //startActivityForResult(intent, REQUEST_CODE);

        // ???????????????????????????????????????,??????????????????????????????????????????
        //Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        //i.addCategory(Intent.CATEGORY_OPENABLE);
        //i.setType("image/*");
        //startActivityForResult(Intent.createChooser(i, "Image Chooser"), REQUEST_CODE);

        //Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        //startActivityForResult(intent, REQUEST_CODE);

        // ???????????????????????????????????????,??????????????????????????????????????????
        //Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        //i.addCategory(Intent.CATEGORY_OPENABLE);
        //i.setType("image/*");
        //startActivityForResult(Intent.createChooser(i, "Image Chooser"), REQUEST_CODE);
        val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        val Photo = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        val chooserIntent = Intent.createChooser(Photo, "Image Chooser")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf<Parcelable>(captureIntent))
        startActivityForResult(chooserIntent, REQUEST_CODE)
    }

    /*override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (x5Webview.canGoBack()) {
                x5Webview.goBack()
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }*/

    inner class MyX5WebChromeClientExtension : ProxyWebChromeClientExtension() {

        override fun onPermissionRequest(
            origin: String?,
            l: Long,
            callback: MediaAccessPermissionsCallback?
        ): Boolean {
            callback?.invoke(origin, l, true) ?: return false
            return true
        }

    }

    class InJavaScriptLocalObj {
        // js ?????? Android
        @JavascriptInterface
        fun onHtml(html: String?) {
            val document = Jsoup.parseBodyFragment(html)
            if (document != null) {
                Log.d("aaaa", "onHtml: ??????document??????")
                /* Log.d("aaaa", "????????????,onHtml#html:" + document.html())
                val elements = document.select(".video")
                if (elements != null && !elements.isEmpty()) {
                    val element = elements[0]
                    val src = element.attr("src")
                    Log.d("aaaa", "onHtml:video=src:$src")
                }*/
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //?????????????????????
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                //??????????????????ValueCallback??????onReceiveValue???
                updatePhotos()
                if (data != null) {
                    // ?????????????????????????????????????????????
                    val results: Array<Uri>
                    val uriData = data.data
                    if (uriData != null) {
                        results = arrayOf(uriData)
                        for (uri in results) {
                            Log.e(TAG, "????????????URI???$uri")
                        }
                        mUploadCallback?.onReceiveValue(results)
                    } else {
                        mUploadCallback?.onReceiveValue(null)
                    }
                } else {
                    Log.e(TAG, "??????????????????" + imageUri.toString())
                    mUploadCallback?.onReceiveValue(arrayOf(imageUri!!))
                }
            } else if (resultCode == RESULT_CANCELED) {
                //??????????????????ValueCallback??????null???
                mUploadCallback?.onReceiveValue(null)
            }
        }
    }

    private fun updatePhotos() {
        // ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        intent.data = imageUri
        sendBroadcast(intent)
    }
}