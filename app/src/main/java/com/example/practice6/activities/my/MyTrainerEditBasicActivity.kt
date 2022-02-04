package com.example.practice6.activities.my

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.practice6.R
import com.example.practice6.model.TrainerObject
import com.example.practice6.retrofit.RetrofitManager
import com.example.practice6.utils.Constants
import com.example.practice6.utils.RESPONSE_STATUS
import com.example.practice6.utils.SharedPrefManager
import com.example.practice6.utils.noAnimation
import kotlinx.android.synthetic.main.activity_my_trainer_edit_basic.btn_my_trainer_edit
import kotlinx.android.synthetic.main.activity_my_trainer_edit_basic.edit_my_career
import kotlinx.android.synthetic.main.activity_my_trainer_edit_basic.edit_my_detail
import kotlinx.android.synthetic.main.activity_my_trainer_edit_basic.edit_my_simple
import kotlinx.android.synthetic.main.activity_my_trainer_edit_basic.edit_my_sports
import kotlinx.android.synthetic.main.top_bar.*

class MyTrainerEditBasicActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_trainer_edit_basic)

        top_bar_title.text = "기본 정보"
        btn_right.setImageResource(R.drawable.bg_empty)
        btn_left.setOnClickListener(this)

        btn_my_trainer_edit.setOnClickListener(this)

        edit_my_sports.setText(TrainerObject.sports)
        edit_my_simple.setText(TrainerObject.simpleIntroduction)
        edit_my_career.setText(TrainerObject.career.toString())
        edit_my_detail.setText(TrainerObject.detailIntroduction)
    }

    override fun onClick(view: View?) {
        when (view) {
            btn_left -> {
                finish()
            }
            // 트레이너 정보 저장
            btn_my_trainer_edit -> {
                val sports = edit_my_sports.text.toString()
                val simple = edit_my_simple.text.toString()
                val career = Integer.parseInt(edit_my_career.text.toString())
                val detail = edit_my_detail.text.toString()
                val trainerInfo = TrainerInfo(sports, simple, career, detail)

                if (TrainerObject.status) {
                    editTrainerInfoApiCall(trainerInfo)
                } else {
                    createTrainerApiCall(trainerInfo)
                }
            }
        }
    }

    override fun finish() {
        super.finish()
        noAnimation()
    }

    // 트레이너 정보
    data class TrainerInfo(
        val sports: String?,
        val simpleIntroduction: String?,
        val career: Int,
        val detailIntroduction: String?,
    )

    // 트레이너 가입
    private fun createTrainerApiCall(trainerInfo: TrainerInfo) {
        RetrofitManager.instance.createTrainer(token = SharedPrefManager.getToken(),
            myTrainerInfo = trainerInfo
        ) { responseState ->
            when (responseState) {
                RESPONSE_STATUS.OKAY -> {
                    TrainerObject.status = true
                    Toast.makeText(this, "트레이너 정보가 저장되었습니다.", Toast.LENGTH_SHORT).show()
                }
                RESPONSE_STATUS.FAIL -> {
                    Log.d(Constants.TAG, "MyTrainerInfoActivity - editTrainerInfo() 트레이너 정보 수정 실패")
                }
            }
        }
    }

    // 트레이너 정보 수정
    private fun editTrainerInfoApiCall(trainerInfo: TrainerInfo) {
        RetrofitManager.instance.editTrainerInformation(token = SharedPrefManager.getToken(),
            myTrainerInfo = trainerInfo
        ) { responseState ->
            when (responseState) {
                RESPONSE_STATUS.OKAY -> {
                    TrainerObject.status = true
                    Toast.makeText(this, "트레이너 정보가 저장되었습니다.", Toast.LENGTH_SHORT).show()
                }
                RESPONSE_STATUS.FAIL -> {
                    Log.d(Constants.TAG, "MyTrainerInfoActivity - editTrainerInfo() 트레이너 정보 수정 실패")
                }
            }
        }
    }
}