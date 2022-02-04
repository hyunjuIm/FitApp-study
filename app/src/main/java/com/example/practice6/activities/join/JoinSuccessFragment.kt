package com.example.practice6.activities.join

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.practice6.R
import com.example.practice6.activities.JoinActivity
import com.example.practice6.model.JoinObject
import com.example.practice6.model.Login
import com.example.practice6.model.TokenObject
import com.example.practice6.retrofit.RetrofitManager
import com.example.practice6.utils.Constants
import com.example.practice6.utils.RESPONSE_STATUS
import com.example.practice6.utils.SharedPrefManager
import kotlinx.android.synthetic.main.fragment_join_success.*

class JoinSuccessFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_join_success, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_end.setOnClickListener {
            loginMemberApiCall(JoinObject.userId!!)
        }
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
        Log.d(Constants.TAG, "MainActivity - getMyInformationApiCall() called")

        RetrofitManager.instance.getMyInformation(token = SharedPrefManager.getToken(),
            completion = { responseState ->
                when (responseState) {
                    RESPONSE_STATUS.OKAY -> {
                        (activity as JoinActivity).finish()
                    }
                    RESPONSE_STATUS.FAIL -> {
                        Log.d(Constants.TAG, "MainActivity - getMyInformation() 내 정보 불러오기 실패")
                    }
                }
            })
    }
}