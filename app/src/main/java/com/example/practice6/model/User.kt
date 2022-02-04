package com.example.practice6.model

import android.util.Log
import com.example.practice6.utils.Constants
import com.example.practice6.utils.Constants.TAG
import com.example.practice6.utils.SharedPrefManager

data class Token(
    val grantType: String,
    val accessToken: String,
)

object TokenObject {
    var grantType: String? = null
    var accessToken: String? = null
}

data class Login(
    val userId: String,
)

object UserObject {
    var userId: String? = null
    var name: String? = null
    var phone: String? = null
    var birthDate: String? = null
    var sex: String? = null
    var authority: String? = null
    var lastLoginTime: String? = null

    fun resetValue() {
        userId = null
        name = null
        phone = null
        birthDate = null
        sex = null
        authority = null
        lastLoginTime = null
    }
}

// 회원가입
object JoinObject {
    var userId: String? = null
    var name: String? = null
    var authority: String? = null

    fun resetValue() {
        userId = null
        name = null
        authority = null
    }

}

object TrainerObject {
    var id: String? = null
    var sports: String? = null
    var simpleIntroduction: String? = null
    var career: Int = 0
    var detailIntroduction: String? = null
    var gymDto: GymDto? = null
    var certificateDtoList: String? = null
    var closeMode = false
    var status = false

    fun resetValue() {
        id = null
        sports = null
        simpleIntroduction = null
        career = 0
        detailIntroduction = null
        gymDto = null
        certificateDtoList = null
        closeMode = false
        status = false
    }
}

data class GymDto(
    val id: String,
    val name: String,
    val zipCode: String,
    val address: String,
    val buildingNum: String,
    val businessRegistrationNum: String,
    val ownerId: String,
    val ownerName: String,
    )

// 로그아웃
fun logoutSetData() {
    Log.d(TAG, "로그아웃")
    SharedPrefManager.allClearUserInfo()
    JoinObject.resetValue()
    UserObject.resetValue()
    TrainerObject.resetValue()
}
