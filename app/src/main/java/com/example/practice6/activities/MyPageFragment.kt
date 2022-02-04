package com.example.practice6.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.practice6.App
import com.example.practice6.R
import com.example.practice6.model.UserObject
import com.example.practice6.retrofit.RetrofitManager
import com.example.practice6.utils.Constants.TAG
import com.example.practice6.utils.RESPONSE_STATUS
import com.example.practice6.utils.SharedPrefManager
import kotlinx.android.synthetic.main.fragment_my_page.*
import kotlinx.android.synthetic.main.top_bar.*
import androidx.core.view.isVisible
import com.example.practice6.activities.my.MyPageEditActivity
import com.example.practice6.activities.my.MyTrainerInfoActivity
import com.example.practice6.model.logoutSetData
import kotlinx.android.synthetic.main.activity_main.*

class MyPageFragment : Fragment(), View.OnClickListener {

    private lateinit var loginFragment: LoginFragment

    lateinit var mainActivity: MainActivity

    companion object {
        fun newInstance(): MyPageFragment {
            return MyPageFragment()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_my_page, container, false)
    }

    override fun onStart() {
        super.onStart()
        
        Log.d(TAG, "MyPageFragment - onStart() called")
        
        top_bar_title.text = "마이페이지"
        btn_left.setImageResource(R.drawable.bg_empty)
        btn_right.setImageResource(R.drawable.ic_edit)

        btn_right.setOnClickListener(this)
        tv_edit_trainer.setOnClickListener(this)
        tv_logout.setOnClickListener(this)
        tv_leave.setOnClickListener(this)
        tv_edit_my.setOnClickListener(this)
        btn_change.setOnClickListener(this)

        tv_my_name.text = UserObject.name

        if (UserObject.authority == "ROLE_MEMBERSHIP") {
            setMemberUi()
        } else if (UserObject.authority == "ROLE_TRAINER") {
            setTrainerUi()
        }
    }

    override fun onClick(view: View?) {
        when (view) {
            btn_right, tv_edit_my -> { // 내 정보 수정
                val intent = Intent(App.instance, MyPageEditActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
            }
            btn_change -> { // 트레이너 ↔ 수강생 전환
                changeAuthorityApiCall()
            }
            tv_edit_trainer -> { // 트레이너 정보
                val intent = Intent(App.instance, MyTrainerInfoActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
            }
            tv_logout -> { // 로그아웃
                mainActivity.include_alert.isVisible = false
                logoutSetData()
                moveLoginFragment()
            }
            tv_leave -> { // 회원탈퇴
                removeMemberApiCall()
            }
        }
    }

    // 로그인 프래그먼트로 이동
    private fun moveLoginFragment() {
        loginFragment = LoginFragment.newInstance()
        (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_frame, loginFragment).commit()
    }

    private fun setTrainerUi() {
        tv_edit_trainer.isVisible = true
        tv_my_badge.text = "트레이너"
        btn_change.text = "수강생으로 전환"
    }

    private fun setMemberUi() {
        tv_edit_trainer.isVisible = false
        tv_my_badge.text = "수강생"
        btn_change.text = "트레이너로 전환"
    }

    data class Authority(
        val authority: String,
    )

    // 수강생 ↔ 트레이너 전환
    private fun changeAuthorityApiCall() {
        animation_change.isVisible = true

        var authority: Authority? = null
        if (UserObject.authority!! == "ROLE_TRAINER") {
            authority = Authority("ROLE_MEMBERSHIP")
        } else if (UserObject.authority!! == "ROLE_MEMBERSHIP") {
            authority = Authority("ROLE_TRAINER")
        }

        RetrofitManager.instance.changeAuthority(token = SharedPrefManager.getToken(),
            authority = authority!!,
            completion = { responseState ->
                when (responseState) {
                    RESPONSE_STATUS.OKAY -> {
                        if (UserObject.authority == "ROLE_TRAINER") {
                            UserObject.authority = "ROLE_MEMBERSHIP"
                            setMemberUi()
                        } else if (UserObject.authority == "ROLE_MEMBERSHIP") {
                            UserObject.authority = "ROLE_TRAINER"
                            setTrainerUi()
                        }
                        mainActivity.getAuthorityApiCall()
                        animation_change.isVisible = false
                    }
                    RESPONSE_STATUS.FAIL -> {
                        animation_change.isVisible = false
                    }
                }
            })
    }

    // 회원 탈퇴
    private fun removeMemberApiCall() {
        RetrofitManager.instance.removeMember(token = SharedPrefManager.getToken(),
            completion = { responseState ->
                when (responseState) {
                    RESPONSE_STATUS.OKAY -> {
                        moveLoginFragment()
                    }
                    RESPONSE_STATUS.FAIL -> {
                        Log.d(TAG, "MainActivity - getMyInformation() 회원 탈퇴 실패")
                    }
                }
            })
    }

}