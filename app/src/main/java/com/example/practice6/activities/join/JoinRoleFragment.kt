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
import com.example.practice6.utils.Constants.TAG
import kotlinx.android.synthetic.main.fragment_join_role.*

class JoinRoleFragment : Fragment() {

    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_join_role, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rg_role.setOnCheckedChangeListener { _, checkedId ->
            Log.d(TAG, "JoinRoleFragment - rg_role: ${rg_role.checkedRadioButtonId}")

            btn_next.isEnabled = true

            when (checkedId) {
                R.id.rb_member -> {
                    JoinObject.authority = "ROLE_MEMBERSHIP"
                }
                R.id.rb_trainer -> {
                    JoinObject.authority = "ROLE_TRAINER"
                }
            }
            tv_chk_role.text = ""
        }

        btn_next.setOnClickListener {
            Log.d(TAG, "JoinRoleFragment - JoinObject.authority: ${JoinObject.authority}")
            Log.d(TAG, "JoinRoleFragment - rg_role: ${rg_role.checkedRadioButtonId}")

            if (rg_role.checkedRadioButtonId < 0) {
                tv_chk_role.text = "필수 선택 사항이에요!"
            } else {
                navController = Navigation.findNavController(view)
                navController.navigate(R.id.action_joinRoleFragment_to_joinNameFragment)
            }
        }
    }
}