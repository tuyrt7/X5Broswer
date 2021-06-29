package com.example.x5broswer.web;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.example.x5broswer.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/**
 * Dmeo说明:
 * 当WebView加载网页时获取该网页中的内容.
 * 参考资料:
 * http://www.maxters.net/2012/02/android-webview-get-html-source/
 */
public class OriginWebActivity extends AppCompatActivity {

    public static void start(Context context) {
        Intent starter = new Intent(context, OriginWebActivity.class);
        context.startActivity(starter);
    }

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_origin);
        init();
    }

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    private void init() {
        Log.d("aaaa", "开始加载init: ");
        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        //mWebView.getSettings().setPluginEnabled(true);
        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        mWebView.requestFocus();
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.loadUrl("https://www.baidu.com");
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                view.loadUrl("javascript:window.local_obj.onHtml('<head>'+"
                        + "document.getElementsByTagName('html')[0].innerHTML+'</head>');");

            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

        });
    }

    final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void onHtml(String html) {
            Document document = Jsoup.parseBodyFragment(html);
            if (document != null) {
                Log.d("aaaa", "onHtml: 解析document成功");
                Log.d("aaaa", "解析成功,onHtml#html:"+document.html());
                Elements elements = document.select(".video");
                if (elements != null && !elements.isEmpty()) {
                    Element element = elements.get(0);
                    String src = element.attr("src");
                    Log.d("aaaa", "onHtml:video=src:"+src);
                }
            }
        }
    }
}

