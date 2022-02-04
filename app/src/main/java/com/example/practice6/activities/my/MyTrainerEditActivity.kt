package com.example.practice6.activities.my

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.practice6.App
import com.example.practice6.R
import com.example.practice6.utils.noAnimation
import kotlinx.android.synthetic.main.activity_my_trainer_edit.*
import kotlinx.android.synthetic.main.top_bar.*

class MyTrainerEditActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_trainer_edit)

        top_bar_title.text = "트레이너 정보 수정"
        btn_right.setImageResource(R.drawable.bg_empty)
        btn_left.setOnClickListener(this)

        edit_gym.setOnClickListener(this)
        edit_basic.setOnClickListener(this)
        edit_certificate.setOnClickListener(this)
    }

    override fun finish() {
        super.finish()
        noAnimation()
    }

    override fun onClick(view: View?) {
        when(view) {
            btn_left -> {
                finish()
            }
            edit_gym -> {
                val intent = Intent(App.instance, MyTrainerEditGymActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
            }
            edit_basic -> {
                val intent = Intent(App.instance, MyTrainerEditBasicActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
            }
            edit_certificate -> {
                val intent = Intent(App.instance, MyTrainerEditCertificateActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
            }
        }
    }
}
