package com.example.practice6.activities.my

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.practice6.App
import com.example.practice6.CertificateAddActivity
import com.example.practice6.R
import com.example.practice6.model.CertificateItem
import com.example.practice6.model.GymItem
import com.example.practice6.recyclerview.CertificateRecyclerAdapter
import com.example.practice6.recyclerview.GymRecyclerAdapter
import com.example.practice6.retrofit.RetrofitManager
import com.example.practice6.utils.Constants
import com.example.practice6.utils.Constants.TAG
import com.example.practice6.utils.RESPONSE_STATUS
import com.example.practice6.utils.SharedPrefManager
import com.example.practice6.utils.noAnimation
import kotlinx.android.synthetic.main.activity_gym_search.*
import kotlinx.android.synthetic.main.activity_gym_search.gym_recycler_view
import kotlinx.android.synthetic.main.activity_my_trainer_edit_certificate.*
import kotlinx.android.synthetic.main.list_item_certificate.*
import kotlinx.android.synthetic.main.top_bar.*
import java.util.ArrayList

class MyTrainerEditCertificateActivity : AppCompatActivity(), View.OnClickListener {

    private var certificateList = ArrayList<CertificateItem>()
    private var certificateRecyclerAdapter: CertificateRecyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_trainer_edit_certificate)

        btn_right.setImageResource(R.drawable.ic_add)
        top_bar_title.text = "내 자격증"
        btn_left.setOnClickListener(this)
        btn_right.setOnClickListener(this)

        certificateRecyclerViewSetting(certificateList)
    }

    override fun onResume() {
        super.onResume()

        getCertificateListApiCall()
    }

    override fun finish() {
        super.finish()
        noAnimation()
    }

    override fun onClick(view: View?) {
        when (view) {
            btn_left -> {
                finish()
            }
            btn_right -> { // 자격증 추가
                val intent = Intent(App.instance, CertificateAddActivity::class.java)
                startActivity(intent)
            }
        }
    }

    // 자격증 리스트 초기 셋팅
    private fun certificateRecyclerViewSetting(certificateList: ArrayList<CertificateItem>) {
        this.certificateRecyclerAdapter = CertificateRecyclerAdapter()
        this.certificateRecyclerAdapter!!.submitList(certificateList)

        certificate_recycler_view.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        certificate_recycler_view.adapter = this.certificateRecyclerAdapter
    }

    // 자격증 목록 불러오기
    private fun getCertificateListApiCall() {
        RetrofitManager.instance.getCertificateList(token = SharedPrefManager.getToken(),
            completion = { responseState, list ->
                when (responseState) {
                    RESPONSE_STATUS.OKAY -> {
                        if (list != null) {
                            this.certificateList.clear()
                            this.certificateList = list
                            this.certificateRecyclerAdapter?.submitList(this.certificateList)
                            this.certificateRecyclerAdapter?.notifyDataSetChanged()
                        }
                    }
                    RESPONSE_STATUS.FAIL -> {
                        Log.d(TAG,
                            "MyTrainerEditCertificateActivity - getCertificateListApiCall() called 실패")
                    }
                }
            })
    }

}