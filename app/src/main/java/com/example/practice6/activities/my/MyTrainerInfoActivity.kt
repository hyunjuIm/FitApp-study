package com.example.practice6.activities.my

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.practice6.App
import com.example.practice6.R
import com.example.practice6.model.*
import com.example.practice6.retrofit.RetrofitManager
import com.example.practice6.utils.Constants.TAG
import com.example.practice6.utils.RESPONSE_STATUS
import com.example.practice6.utils.SharedPrefManager
import com.example.practice6.utils.noAnimation
import kotlinx.android.synthetic.main.activity_my_trainer_info.*
import kotlinx.android.synthetic.main.top_bar.*

class MyTrainerInfoActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_trainer_info)

        top_bar_title.text = "트레이너 정보"
        btn_right.setImageResource(R.drawable.ic_edit)
        btn_left.setOnClickListener(this)
        btn_right.setOnClickListener(this)

//        btn_my_trainer_edit.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()

        if (TrainerObject.status) {
            trainerApiCall()
        }
    }

    override fun onClick(view: View?) {
        when (view) {
            btn_left -> {
                finish()
            }
            btn_right -> {
                val intent = Intent(App.instance, MyTrainerEditActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
            }
        }
    }

    override fun finish() {
        super.finish()
        noAnimation()
    }

    // 트레이너 정보 조회
    @SuppressLint("SetTextI18n")
    private fun trainerApiCall() {
        RetrofitManager.instance.getTrainerData(token = SharedPrefManager.getToken(),
            completion = { responseState ->
                when (responseState) {
                    RESPONSE_STATUS.OKAY -> {
                        my_gym1.text = TrainerObject.gymDto?.name
                        my_gym2.text = "${TrainerObject.gymDto?.address} ${TrainerObject.gymDto?.buildingNum}"
                        my_sports.text = TrainerObject.sports
                        my_simple.text = TrainerObject.simpleIntroduction
                        my_certificate.text = TrainerObject.certificateDtoList
                        my_detail.text = TrainerObject.detailIntroduction
                    }
                    RESPONSE_STATUS.FAIL -> {
                        Log.d(TAG,
                            "MainActivity - getTrainerData() 트레이너 정보 불러오기 실패")
                    }
                }
            })
    }
}