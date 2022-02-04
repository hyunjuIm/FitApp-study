package com.example.practice6.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.practice6.App
import com.example.practice6.R
import com.example.practice6.utils.Constants.TAG
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {

    private lateinit var menu: String

    companion object {
        fun newInstance(): LoginFragment {
            return LoginFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        menu = arguments?.getString("menu").toString()
        Log.d(TAG, "LoginFragment - onCreateView() menu: $menu")

        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onStart() {
        super.onStart()

        btn_request_code.setOnClickListener {
            val intent = Intent(App.instance, LoginActivity::class.java)
            startActivity(intent)
        }
    }

}