package com.example.practice6.activities.my

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.practice6.R
import kotlinx.android.synthetic.main.top_bar.*
import kotlinx.android.synthetic.main.activity_gym_create.*
import android.graphics.Rect
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.practice6.App
import com.example.practice6.activities.FindAddressActivity
import com.example.practice6.retrofit.RetrofitManager
import com.example.practice6.utils.Constants.TAG
import com.example.practice6.utils.RESPONSE_STATUS
import com.example.practice6.utils.SharedPrefManager

class GymCreateActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gym_create)

        btn_left.setImageResource(R.drawable.bg_empty)
        top_bar_title.text = "새 시설 등록"
        btn_right.setOnClickListener(this)

        create_gym_search.setOnClickListener(this)
        btn_create_gym.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view) {
            btn_right -> {
                finish()
            }
            create_gym_search -> { // 우편번호 검색
                val intent = Intent(App.instance, FindAddressActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivityForResult(intent, 3000)
            }
            btn_create_gym -> { // 시설 등록
                checkGymInputData()
            }
        }
    }

    // 다음 주소 받아온 데이터
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "GymCreateActivity - onActivityResult() called")

        if (resultCode == RESULT_OK) {
            val address1 = data?.getStringExtra("address1")
            val address2 = data?.getStringExtra("address2")
            Log.d(TAG, "address1 :$address1, address2: $address2")

            create_gym_address1.text = address1
            create_gym_address2.text = address2
        }
    }

    data class Gym(
        val name: String,
        val businessRegistrationNum: String,
        val zipCode: String,
        val address: String,
        val buildingNum: String,
    )

    // 입력한 시설 정보 빈 값 체크
    private fun checkGymInputData() {
        val name = create_gym_name.text.toString()
        val businessRegistrationNum = create_gym_num.text.toString()
        val zipCode = create_gym_address1.text.toString()
        val address = create_gym_address2.text.toString()
        var buildingNum = create_gym_address3.text.toString()

        var pass = true

        if (name.isEmpty()) {
            tv_gym_name.isVisible = true
            pass = false
        }
        if (businessRegistrationNum.isEmpty()) {
            tv_gym_num.isVisible = true
            pass = false
        }
        if (zipCode.isEmpty() || address.isEmpty()) {
            tv_gym_address.isVisible = true
            pass = false
        }

        if (pass) {
            if (buildingNum.isEmpty()) {
                buildingNum = " "
            }
            val gym = Gym(name, businessRegistrationNum, zipCode, address, buildingNum)
            createGymApiCall(gym)
        }
    }

    // 시설 등록
    private fun createGymApiCall(gym: Gym) {
        RetrofitManager.instance.createGym(token = SharedPrefManager.getToken(), gym = gym,
            completion = { responseState ->
                when (responseState) {
                    RESPONSE_STATUS.OKAY -> {
                        Toast.makeText(this, "시설이 등록되었습니다.", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    RESPONSE_STATUS.FAIL -> {
                        Log.d(TAG, "MainActivity - getMyInformation() 내 정보 불러오기 실패")
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

