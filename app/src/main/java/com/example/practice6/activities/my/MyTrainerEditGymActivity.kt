package com.example.practice6.activities.my

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.practice6.App
import com.example.practice6.R
import com.example.practice6.activities.FindAddressActivity
import com.example.practice6.model.TrainerObject
import com.example.practice6.retrofit.RetrofitManager
import com.example.practice6.utils.Constants
import com.example.practice6.utils.Constants.TAG
import com.example.practice6.utils.RESPONSE_STATUS
import com.example.practice6.utils.SharedPrefManager
import com.example.practice6.utils.noAnimation
import kotlinx.android.synthetic.main.activity_gym_create.*
import kotlinx.android.synthetic.main.activity_my_trainer_edit_gym.*
import kotlinx.android.synthetic.main.top_bar.*

class MyTrainerEditGymActivity : AppCompatActivity(), View.OnClickListener {

    private var gymId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_trainer_edit_gym)

        btn_right.setImageResource(R.drawable.bg_empty)
        top_bar_title.text = "소속 등록"
        btn_left.setOnClickListener(this)

        edit_my_gym.text = TrainerObject.gymDto?.name

        edit_my_gym.setOnClickListener(this)
        btn_save_gym.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view) {
            btn_left -> {
                finish()
            }
            edit_my_gym -> {
                val intent = Intent(App.instance, GymSearchActivity::class.java)
                startActivityForResult(intent, 3000)
            }
            btn_save_gym -> {
                if (!gymId.isNullOrEmpty()) {
                    registerGymToTrainerApiCall()
                }
            }
        }
    }

    override fun finish() {
        super.finish()
        noAnimation()
    }

    // 시설 선택 완료
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "MyTrainerEditGymActivity - onActivityResult() called")

        if (resultCode == RESULT_OK) {
            val id = data?.getStringExtra("id")
            val name = data?.getStringExtra("name")
            Log.d(TAG, "받아온 시설 데이터 -> id :$id, name: $name")

            edit_my_gym.text = name
            gymId = id
        }
    }

    data class Gym(val id: String)

    // 트레이너 시설 등록
    private fun registerGymToTrainerApiCall() {
        val gym = Gym(gymId!!)

        RetrofitManager.instance.registerGymToTrainer(token = SharedPrefManager.getToken(),
            gym = gym,
            completion = { responseState ->
                when (responseState) {
                    RESPONSE_STATUS.OKAY -> {
                        Toast.makeText(this, "소속이 등록되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            })
    }

}