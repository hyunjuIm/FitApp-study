package com.example.practice6.activities

import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.practice6.R
import com.example.practice6.model.JoinObject
import com.example.practice6.utils.Constants
import kotlinx.android.synthetic.main.activity_join.*
import kotlinx.android.synthetic.main.top_bar.*

class JoinActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        top_bar_title.text = "회원가입"
        btn_left.setImageResource(R.drawable.bg_empty)
        btn_right.setOnClickListener(this)

        if (intent.hasExtra("userId")) {
            val userId = intent.getStringExtra("userId")
            Log.d(Constants.TAG, "SignUpActivity - userId : $userId")
            JoinObject.userId = userId
        }

        navController = fragment_join.findNavController()
    }

    override fun onClick(view: View?) {
        when (view) {
            btn_right -> {
                JoinObject.resetValue()
                finish()
            }
        }
    }

    override fun onDestroy() {
        JoinObject.resetValue()
        super.onDestroy()
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
                imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0)
                focusView.clearFocus()
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}