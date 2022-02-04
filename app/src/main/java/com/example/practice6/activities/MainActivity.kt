package com.example.practice6.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.core.view.isVisible
import com.example.practice6.App
import com.example.practice6.R
import com.example.practice6.activities.my.MyTrainerInfoActivity
import com.example.practice6.model.*
import com.example.practice6.utils.CLIECK_MENU
import com.example.practice6.utils.Constants.TAG
import com.example.practice6.utils.SharedPrefManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import com.example.practice6.retrofit.RetrofitManager
import com.example.practice6.utils.RESPONSE_STATUS
import kotlinx.android.synthetic.main.simple_alert_gray.*

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener,
    View.OnClickListener {

    private lateinit var homeFragment: HomeFragment
    private lateinit var loginFragment: LoginFragment
    private lateinit var myPageFragment: MyPageFragment

    private var clickedMenu: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "MainActivity - onCreate() called")

        bottom_nav.setOnNavigationItemSelectedListener(this)

        btn_alert_close.setOnClickListener(this)
        alert_text.setOnClickListener(this)

        homeFragment = HomeFragment.newInstance()
        supportFragmentManager.beginTransaction().add(R.id.fragment_frame, homeFragment).commit()
    }

    override fun onResume() {
        super.onResume()

        // 로그인 유지 - 토큰이 있는 경우, 내 정보 불러오기
        if (SharedPrefManager.getToken().isNotEmpty() && UserObject.userId == null) {
            getMyInformationApiCall()
        } else if (SharedPrefManager.getToken().isNotEmpty() && UserObject.userId != null) {
            // 로그인 하고 메인으로 넘어오는 경우
            getAuthorityApiCall()
        }

        // 로그인이 안 된 경우 알림장 무조건 안 띄우기
        if (SharedPrefManager.getToken().isEmpty() || TrainerObject.closeMode) {
            include_alert.isVisible = false
        }

        // 이전에 클릭했던 메뉴로 던지기
        if (clickedMenu?.isNotEmpty() == true) {
            when (clickedMenu) {
                CLIECK_MENU.CHAT -> { // 채팅
                    if (SharedPrefManager.getToken().isNotEmpty()) {
                        moveChatFragment()
                    } else {
                        moveLoginFragment()
                    }
                }
                CLIECK_MENU.MY_PAGE -> { // 마이페이지
                    if (SharedPrefManager.getToken().isNotEmpty()) {
                        moveMyPageFragment()
                    } else {
                        moveLoginFragment()
                    }
                }
            }
        }
    }

    override fun onClick(view: View?) {
        when (view) {
            // 정보 미입력 알림창
            btn_alert_close -> {
                include_alert.isVisible = false
                TrainerObject.closeMode = true
            }
            // 알림창 - 입력창으로 이동
            alert_text -> {
                val intent = Intent(App.instance, MyTrainerInfoActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        Log.d(TAG, "MainActivity - onNavigationItemSelected() called / $item")

        when (item.itemId) {
            R.id.menu_home -> {
                Log.d(TAG, "MainActivity - 홈 클릭")
                homeFragment = HomeFragment.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.fragment_frame, homeFragment)
                    .commit()
            }
            R.id.menu_chat -> {
                Log.d(TAG, "MainActivity - 채팅 클릭")
                clickedMenu = CLIECK_MENU.CHAT
                if (SharedPrefManager.getToken().isNotEmpty()) {
                    moveChatFragment()
                } else {
                    moveLoginFragment()
                }
            }
            R.id.menu_my_page -> {
                Log.d(TAG, "MainActivity - 마이페이지 클릭")
                clickedMenu = CLIECK_MENU.MY_PAGE
                if (SharedPrefManager.getToken().isNotEmpty()) {
                    moveMyPageFragment()
                } else {
                    moveLoginFragment()
                }
            }
        }

        return true
    }

    // 채팅 화면으로 넘기기
    private fun moveChatFragment() {

    }

    // 마이페이지 화면으로 넘기기
    private fun moveMyPageFragment() {
        Log.d(TAG, "myPageFragment")
        myPageFragment = MyPageFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_frame, myPageFragment).commit()
    }

    // 로그인 화면으로 넘기기
    private fun moveLoginFragment() {
        Log.d(TAG, "loginFragment")
        loginFragment = LoginFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_frame, loginFragment).commit()
    }

    // 내 정보 불러오기
    private fun getMyInformationApiCall() {
        Log.d(TAG, "MainActivity - getMyInformationApiCall() called")

        RetrofitManager.instance.getMyInformation(token = SharedPrefManager.getToken(),
            completion = { responseState ->
                when (responseState) {
                    RESPONSE_STATUS.OKAY -> {
                        getAuthorityApiCall()
                    }
                    RESPONSE_STATUS.FAIL -> {
                        Log.d(TAG, "MainActivity - getMyInformation() 내 정보 불러오기 실패")
                    }
                }
            })
    }

    // 수강생 혹은 트레이너 정보가 입력되었는지 여부
    fun getAuthorityApiCall() {
        RetrofitManager.instance.getAuthority(token = SharedPrefManager.getToken(),
            completion = { responseState, authority, status ->
                when (responseState) {
                    RESPONSE_STATUS.OKAY -> {
                        if (authority == "ROLE_MEMBERSHIP") {
                            alert_text.text = "수강생 정보를 등록하고 트레이너와 함께해보세요!"
                        } else if (authority == "ROLE_TRAINER") {
                            TrainerObject.status = status
                            alert_text.text = "트레이너 정보를 등록하고 수강생을 만나보세요!"
                        }

                        if(!TrainerObject.closeMode) {
                            include_alert.isVisible = !status
                        }
                    }
                    RESPONSE_STATUS.FAIL -> {
                        include_alert.isVisible = false
                    }
                }
            })
    }

}