package com.example.practice6.utils

import android.util.Log
import com.example.practice6.App
import com.example.practice6.model.Token
import com.example.practice6.utils.Constants.TAG

import android.content.Context.MODE_PRIVATE
import com.example.practice6.model.TokenObject

object SharedPrefManager {

    private const val USER = "user"
    private const val USER_ID = "user_id"

    private const val TOKEN = "token"

    private const val GRANT_TYPE = "grant_type"
    private const val ACCESS_TOKEN = "access_token"

    // 토큰 저장
    fun saveTokenInfo(tokenData: Token) {
        val shared = App.instance.getSharedPreferences(TOKEN, MODE_PRIVATE)
        val editor = shared.edit()

        val grantType = (tokenData.grantType).substring(0, 1)
            .toUpperCase() + (tokenData.grantType).substring(1);
        editor.putString(GRANT_TYPE, grantType)
        editor.putString(ACCESS_TOKEN, tokenData.accessToken)

        editor.apply()
    }

    // 토큰 head 호출
    fun getToken(): String {
        val shared = App.instance.getSharedPreferences(TOKEN, MODE_PRIVATE)
        val grantType = shared.getString(GRANT_TYPE, "")
        val accessToken = shared.getString(ACCESS_TOKEN, "")

        Log.d(TAG, "SharedPrefManager - getToken(): $grantType $accessToken")

        return if (grantType.toString().isNotEmpty() && accessToken.toString().isNotEmpty()) {
            "$grantType $accessToken"
        } else {
            ""
        }
    }

    fun allClearUserInfo() {
        val userShared = App.instance.getSharedPreferences(USER, MODE_PRIVATE)
        val userEditor = userShared.edit()
        userEditor.clear()
        userEditor.apply()

        val tokenShared = App.instance.getSharedPreferences(TOKEN, MODE_PRIVATE)
        val tokenEditor = tokenShared.edit()
        tokenEditor.clear()
        tokenEditor.apply()
    }

}