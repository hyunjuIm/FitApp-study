package com.example.practice6.activities.my

import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.practice6.R
import com.example.practice6.model.UserObject
import com.example.practice6.retrofit.RetrofitManager
import com.example.practice6.utils.Constants.TAG
import com.example.practice6.utils.RESPONSE_STATUS
import com.example.practice6.utils.SharedPrefManager
import com.example.practice6.utils.noAnimation
import kotlinx.android.synthetic.main.activity_my_page_edit.*
import kotlinx.android.synthetic.main.top_bar.*

class MyPageEditActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page_edit)

        top_bar_title.text = "내 정보 수정"
        btn_right.setImageResource(R.drawable.bg_empty)
        btn_left.setOnClickListener(this)

        input_edit_name.setText(UserObject.name)

        btn_my_edit.setOnClickListener(this)
    }

    override fun finish() {
        super.finish()
        noAnimation()
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

    data class MyEditInfo(
        val name: String,
    )

    override fun onClick(view: View?) {
        when (view) {
            btn_left -> { // 닫기
                finish()
            }
            btn_my_edit -> { // 수정하기
                if (checkName()) {
                    val name = input_edit_name.text.toString()
                    val myInfo = MyEditInfo(name)
                    editMyInfoApiCall(myInfo)
                }
            }
        }
    }

    private fun editMyInfoApiCall(myInfo: MyEditInfo) {
        RetrofitManager.instance.editMyInformation(token = SharedPrefManager.getToken(),
            myInfo = myInfo,
            completion = { responseState ->
                when (responseState) {
                    RESPONSE_STATUS.OKAY -> {
                        Toast.makeText(this, "내 정보가 수정되었습니다.", Toast.LENGTH_LONG).show()
                    }
                    RESPONSE_STATUS.FAIL -> {
                        Log.d(TAG, "MyPageEditActivity - editMyInfoApiCall() 내 수정 실패")
                    }
                }
            })
    }

    private fun checkName(): Boolean {
        return if (input_edit_name.text.isEmpty()) {
            chk_edit_name.text = "이름을 입력해주세요."
            input_edit_name.setBackgroundResource(R.drawable.rounded_red_edittext)
            false
        } else {
            chk_edit_name.text = ""
            input_edit_name.setBackgroundResource(R.drawable.rounded_edittext)
            true
        }
    }
}