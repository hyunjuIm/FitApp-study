package com.example.practice6.activities.join

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.practice6.R
import com.example.practice6.model.JoinObject
import com.example.practice6.model.Login
import com.example.practice6.model.TokenObject
import com.example.practice6.model.UserObject
import com.example.practice6.retrofit.RetrofitManager
import com.example.practice6.utils.Constants.TAG
import com.example.practice6.utils.RESPONSE_STATUS
import com.example.practice6.utils.SharedPrefManager
import com.example.practice6.utils.onMyTextChanged
import kotlinx.android.synthetic.main.fragment_join_name.*

class JoinNameFragment : Fragment() {

    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_join_name, container, false)
    }

    // 회원가입
    data class JoinInfo(
        val userId: String?,
        val name: String?,
        val authority: String?,
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        input_name.onMyTextChanged {
            if (it.toString().isEmpty()) {
                emptyName()
            } else {
                passName()
            }
        }

        btn_next.setOnClickListener {
            if (input_name.text.isEmpty()) {
                emptyName()
            } else {
                passName()
                joinMemberApiCall(view)
            }
        }
    }

    private fun joinMemberApiCall(view: View) {
        JoinObject.name = input_name.text.toString()
        Log.d(TAG, "JoinNameFragment - JoinObject.name: ${JoinObject.name}")

        val join = JoinInfo(JoinObject.userId, JoinObject.name, JoinObject.authority)

        RetrofitManager.instance.joinMember(joinInfo = join,
            completion = { responseState, result ->
                when (responseState) {
                    RESPONSE_STATUS.OKAY -> {
                        if (result) {
                            navController = Navigation.findNavController(view)
                            navController.navigate(R.id.action_joinNameFragment_to_joinSuccessFragment)
                        }
                    }
                    RESPONSE_STATUS.FAIL -> {
                        Log.d(TAG, "JoinNameFragment - joinMemberApiCall() 회원가입 실패")
                    }
                }
            })
    }

    private fun emptyName() {
        btn_next.isEnabled = false
        tv_chk_name.text = "닉네임을 입력해주세요."
        input_name.setBackgroundResource(R.drawable.rounded_red_edittext)
    }

    private fun passName() {
        btn_next.isEnabled = true
        tv_chk_name.text = ""
        input_name.setBackgroundResource(R.drawable.rounded_edittext)
    }
}