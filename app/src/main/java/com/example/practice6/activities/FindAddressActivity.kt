package com.example.practice6.activities

import android.annotation.SuppressLint
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.graphics.Rect
import android.view.MotionEvent
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.example.practice6.R
import kotlinx.android.synthetic.main.activity_find_address.*

class FindAddressActivity : AppCompatActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_address)

        web_view_gym.settings.javaScriptEnabled = true
        web_view_gym.addJavascriptInterface(MyJavaScriptInterface(this), "Android")

        web_view_gym.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                web_view_gym.loadUrl("javascript:sample2_execDaumPostcode();")
            }
        }

        web_view_gym.loadUrl("https://leescode.com/daum.html")
    }

    class MyJavaScriptInterface(private val activity: Activity) {
        @JavascriptInterface
        fun processDATA(data: String?) {
            val address1 = data?.substring(0, 5)
            val address2 = data?.substring(7)

            activity.intent.putExtra("address1", address1)
            activity.intent.putExtra("address2", address2)
            activity.setResult(RESULT_OK, activity.intent)
            activity.finish()
        }
    }

    // 키보드 다른 화면 터치시 내리기
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val focusView: View? = currentFocus
        if (focusView != null) {
            val rect = Rect()
            focusView.getGlobalVisibleRect(rect)
            val x = ev.x.toInt()
            val y = ev.y.toInt()
            if (!rect.contains(x, y)) {
                val imm: InputMethodManager =
                    getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(focusView.windowToken, 0)
                focusView.clearFocus()
            }
        }
        return super.dispatchTouchEvent(ev)
    }

}