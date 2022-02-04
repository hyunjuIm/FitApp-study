package com.example.practice6.activities

import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import com.example.practice6.App
import com.example.practice6.R
import com.example.practice6.model.Login
import com.example.practice6.model.TokenObject
import com.example.practice6.retrofit.RetrofitManager
import com.example.practice6.utils.Constants.TAG
import com.example.practice6.utils.RESPONSE_STATUS
import com.example.practice6.utils.SharedPrefManager
import com.example.practice6.utils.onMyTextChanged
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.top_bar.*
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private val PHONE_REGEX =
        "^\\s*(010|011|016|017|018|019)(-|\\)|\\s)*(\\d{3,4})(-|\\s)*(\\d{4})\\s*$"

    private var CERTIFICATION_NUMBER = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        top_bar_title.text = "로그인"
        btn_left.setImageResource(R.drawable.bg_empty)
        btn_right.setOnClickListener(this)
        btn_request_code.setOnClickListener(this)
        btn_start.setOnClickListener(this)

        login_phone.onMyTextChanged {
            if (it.toString().length > 8) {
                btn_request_code.isEnabled = checkPhoneRegex(it.toString())
            } else {
                btn_request_code.isEnabled = false
            }
        }

        login_code.onMyTextChanged {
            btn_start.isEnabled = it.toString().isNotEmpty()
        }
    }

    override fun onClick(view: View?) {
        when (view) {
            btn_right -> finish()
            btn_request_code -> requestVerificationCodeApiCall()
            btn_start -> checkTheVerificationNumberApiCall()
        }
    }

    data class PhoneInfo(
        val phone: String,
    )

    // 인증번호 요청
    private fun requestVerificationCodeApiCall() {
        loading_request_code.isVisible = true

        val phoneInfo = PhoneInfo(login_phone.text.toString())

        RetrofitManager.instance.requestVerificationCode(
            phoneNumBer = phoneInfo,
            completion = { responseState, result ->
                when (responseState) {
                    RESPONSE_STATUS.OKAY -> {
                        CERTIFICATION_NUMBER = result.toString()
                        Log.d(TAG, "LoginActivity - 인증번호 : $CERTIFICATION_NUMBER")

                        test("폰인증이야")

                        loading_request_code.isVisible = false
                        btn_request_code.text = "인증문자 다시 받기"

                        layout_code.isVisible = true
                    }

                    RESPONSE_STATUS.FAIL ->{
                        Log.d(TAG, "LoginActivity - 요청 실패")
                    }
                }
            })
    }

    private fun test(memo:String){
        //memo 로 API 다시호출
        requestVerificationCodeApiCall()
    }

    data class PhoneCodeInfo(
        val phone: String,
        val number: String,
    )

    // 인증번호 확인
    private fun checkTheVerificationNumberApiCall() {
        val phoneCode = PhoneCodeInfo(login_phone.text.toString(), login_code.text.toString())

        RetrofitManager.instance.checkTheVerificationNumber(
            phoneCode = phoneCode,
            completion = { responseState, userId, alreadyExist ->
                when (responseState) {
                    RESPONSE_STATUS.OKAY -> {
                        if (alreadyExist!!) {
                            loginMemberApiCall(userId!!)
                        } else {
                            val intent = Intent(App.instance, JoinActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                            intent.putExtra("userId", userId)
                            startActivity(intent)

                            finish()
                        }
                    }
                }
            })
    }

    // 로그인
    private fun loginMemberApiCall(userId: String) {
        val login = Login(userId)

        RetrofitManager.instance.loginMember(
            login = login,
            completion = { responseState, token ->
                when (responseState) {
                    RESPONSE_STATUS.OKAY -> {
                        TokenObject.grantType = token!!.grantType
                        TokenObject.accessToken = token.accessToken
                        SharedPrefManager.saveTokenInfo(token)

                        getMyInformationApiCall()
                    }
                }
            })

    }

    // 내 정보 불러오기
    private fun getMyInformationApiCall() {
        Log.d(TAG, "MainActivity - getMyInformationApiCall() called")

        RetrofitManager.instance.getMyInformation(token = SharedPrefManager.getToken(),
            completion = { responseState ->
                when (responseState) {
                    RESPONSE_STATUS.OKAY -> {
                        finish()
                    }
                    RESPONSE_STATUS.FAIL -> {
                        Log.d(TAG, "MainActivity - getMyInformation() 내 정보 불러오기 실패")
                    }
                }
            })
    }

    // 휴대폰 번호 정규식 검사
    private fun checkPhoneRegex(phoneNumber: String): Boolean {
        return Pattern.matches(PHONE_REGEX, phoneNumber)
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