package com.example.practice6

import android.app.DatePickerDialog
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.practice6.retrofit.RetrofitManager
import com.example.practice6.utils.Constants.TAG
import com.example.practice6.utils.RESPONSE_STATUS
import com.example.practice6.utils.SharedPrefManager
import kotlinx.android.synthetic.main.activity_certificate_add.*
import kotlinx.android.synthetic.main.top_bar.*
import java.util.*

class CertificateAddActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_certificate_add)

        btn_left.setImageResource(R.drawable.bg_empty)
        top_bar_title.text = "자격증 등록"
        btn_right.setOnClickListener(this)

        edit_certificate_date.setOnClickListener(this)
        btn_add_certificate.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view) {
            btn_right -> finish()
            // 날짜 선택
            edit_certificate_date -> selectDate()
            // 자격증 등록
            btn_add_certificate -> addCertificateApiCall()
        }
    }

    // 날짜 선택
    private fun selectDate() {
        val cal = Calendar.getInstance()
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, month, day ->
                var date = year.toString()

                date += if ((month + 1) < 10) {
                    "-0${month + 1}"
                } else {
                    "-${month + 1}"
                }

                date += if (day < 10) {
                    "-0$day"
                } else {
                    "-$day"
                }

                edit_certificate_date.text = date
            }
        DatePickerDialog(this,
            dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)).show()
    }

    data class Certificate(
        val name: String,
        val issuer: String,
        val acquisitionDate: String,
    )

    // 자격증 추가
    private fun addCertificateApiCall() {
        val certificate = Certificate(
            edit_certificate_name.text.toString(),
            edit_certificate_issuer.text.toString(),
            edit_certificate_date.text.toString()
        )

        RetrofitManager.instance.addCertificate(token = SharedPrefManager.getToken(),
            certificate = certificate,
            completion = { responseState ->
                when (responseState) {
                    RESPONSE_STATUS.OKAY -> {
                        Toast.makeText(this, "자격증 정보가 등록되었습니다.", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    RESPONSE_STATUS.FAIL -> {
                        Log.d(TAG, "CertificateAddActivity - addCertificateApiCall() called 실패")
                    }
                }
            })
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