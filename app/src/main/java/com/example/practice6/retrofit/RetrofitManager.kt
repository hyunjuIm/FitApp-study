package com.example.practice6.retrofit

import android.util.Log
import com.example.practice6.CertificateAddActivity
import com.example.practice6.activities.LoginActivity
import com.example.practice6.activities.MyPageFragment
import com.example.practice6.activities.join.JoinNameFragment
import com.example.practice6.activities.my.*
import com.example.practice6.model.*
import com.example.practice6.utils.*
import com.example.practice6.utils.Constants.TAG
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.ArrayList

class RetrofitManager {
    companion object {
        val instance = RetrofitManager()
    }

    // http 콜 만들기
    // 레트로핏 인터페이스 가져오기
    private val iRetrofit: IRetrofit? =
        RetrofitClient.getClient(API.BASE_URL)?.create(IRetrofit::class.java)

    // 인증번호 요청
    fun requestVerificationCode(
        phoneNumBer: LoginActivity.PhoneInfo,
        completion: (RESPONSE_STATUS, String?) -> Unit,
    ) {
        val call = iRetrofit?.requestVerificationCode(phoneNumBer) ?: return

        call.enqueue(object : retrofit2.Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(TAG, "RetrofitManager - onResponse() / 인증번호 요청 response : $response")

                val responseCode = response.code() / 100
                Log.d(TAG, "RetrofitManager - onResponse() called $responseCode")

                when (responseCode) {
                    2 -> {
                        response.body()?.let {
                            val body = it.asJsonObject
                            val code = body.get("code").asString
                            if (successCode(code).isNotEmpty()) {
                                val data = body.get("data").asString

                                completion(RESPONSE_STATUS.OKAY, data)
                            }
                        }
                    }
                    else -> {
                        completion(RESPONSE_STATUS.FAIL, null)
                    }
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d(TAG, "RetrofitManager - onFailure() called / t : $t")
                completion(RESPONSE_STATUS.FAIL, null)
            }
        })
    }

    // 인증번호 확인
    fun checkTheVerificationNumber(
        phoneCode: LoginActivity.PhoneCodeInfo,
        completion: (RESPONSE_STATUS, String?, Boolean?) -> Unit,
    ) {
        val call = iRetrofit?.checkTheVerificationNumber(phoneCode) ?: return

        call.enqueue(object : retrofit2.Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(TAG, "RetrofitManager - onResponse() / 인증번호 확인 response : $response")

                val responseCode = response.code() / 100
                Log.d(TAG, "RetrofitManager - onResponse() called $responseCode")

                when (responseCode) {
                    2 -> {
                        response.body()?.let {
                            val body = it.asJsonObject
                            val code = body.get("code").asString
                            if (successCode(code).isNotEmpty()) {
                                if (code == "A2400") {
                                    val data = body.get("data").asJsonObject
                                    val userId = data.get("userId").asString
                                    val alreadyExist = data.get("alreadyExist").asBoolean

                                    completion(RESPONSE_STATUS.OKAY, userId, alreadyExist)
                                } else {
                                    completion(RESPONSE_STATUS.FAIL, null, null)
                                }
                            }
                        }
                    }
                    else -> {
                        completion(RESPONSE_STATUS.FAIL, null, null)
                    }
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d(TAG, "RetrofitManager - onFailure() called / t : $t")
                completion(RESPONSE_STATUS.FAIL, null, null)
            }
        })
    }

    // 멤버 로그인 토큰 저장
    fun loginMember(login: Login, completion: (RESPONSE_STATUS, Token?) -> Unit) {

        val call = iRetrofit?.loginMember(login) ?: return

        call.enqueue(object : retrofit2.Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(TAG, "RetrofitManager - onResponse() / 로그인 response : $response")

                val responseCode = response.code() / 100
                Log.d(TAG, "RetrofitManager - onResponse() called $responseCode")

                when (responseCode) {
                    2 -> {
                        response.body()?.let {
                            val body = it.asJsonObject
                            val code = body.get("code").asString
                            if (successCode(code).isNotEmpty()) {
                                val data = body.get("data").asJsonObject

                                val grantType = data.get("grantType").asString
                                val accessToken = data.get("accessToken").asString

                                val tokenData = Token(grantType, accessToken)

                                completion(RESPONSE_STATUS.OKAY, tokenData)
                            }
                        }
                    }
                    else -> {
                        completion(RESPONSE_STATUS.FAIL, null)
                    }
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d(TAG, "RetrofitManager - onFailure() called / t : $t")
                completion(RESPONSE_STATUS.FAIL, null)
            }
        })
    }

    // 아이디 중복 검사
    fun checkExistsUserId(
        userId: String,
        completion: (RESPONSE_STATUS, result: Boolean) -> Unit,
    ) {
        val call = iRetrofit?.checkExistsUserId(userId) ?: return

        call.enqueue(object : retrofit2.Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(TAG, "RetrofitManager - 아이디 중복확인 response : $response")

                val responseCode = response.code() / 100
                Log.d(TAG, "RetrofitManager - responseCode: $responseCode")

                when (responseCode) {
                    2 -> {
                        response.body()?.let {
                            val result = it.asBoolean
                            Log.d(TAG, "RetrofitManager - body: $result")
                            completion(RESPONSE_STATUS.OKAY, result)
                        }
                    }
                    else -> {
                        completion(RESPONSE_STATUS.FAIL, false)
                    }
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d(TAG, "RetrofitManager - onFailure() called / t : $t")
                completion(RESPONSE_STATUS.FAIL, false)
            }
        })
    }

    // 회원가입
    fun joinMember(
        joinInfo: JoinNameFragment.JoinInfo,
        completion: (RESPONSE_STATUS, Boolean) -> Unit,
    ) {
        val call = iRetrofit?.joinMember(joinInfo) ?: return

        call.enqueue(object : retrofit2.Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(TAG, "RetrofitManager - onResponse() / 회원가입 response : $response")

                val responseCode = response.code() / 100
                Log.d(TAG, "RetrofitManager - onResponse() called $responseCode")

                when (responseCode) {
                    2 -> {
                        response.body()?.let {
                            val body = it.asJsonObject
                            val code = body.get("code").asString
                            if (successCode(code).isNotEmpty()) {
                                completion(RESPONSE_STATUS.OKAY, true)
                            }
                        }
                    }
                    else -> {
                        completion(RESPONSE_STATUS.FAIL, false)
                    }
                }

            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d(TAG, "RetrofitManager - onFailure() called / t : $t")
                completion(RESPONSE_STATUS.FAIL, false)
            }

        })
    }

    // 내 정보 조회
    fun getMyInformation(token: String, completion: (RESPONSE_STATUS) -> Unit) {

        val call = iRetrofit?.getMyInformation(token) ?: return

        call.enqueue(object : retrofit2.Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(TAG, "RetrofitManager - getMyInformation() / 내 정보 response : $response")

                val responseCode = response.code() / 100
                Log.d(TAG, "RetrofitManager - getMyInformation() called $responseCode")

                when (responseCode) {
                    2 -> {
                        response.body()?.let {
                            val body = it.asJsonObject
                            val code = body.get("code").asString
                            if (successCode(code).isNotEmpty()) {
                                val data = body.get("data").asJsonObject

                                val userId = data.get("userId").asString
                                val name = data.get("name").asString
                                val authority = data.get("authority").asString

                                var lastLoginTime = ""
                                if (!data.get("lastLoginTime").isJsonNull) {
                                    lastLoginTime = data.get("lastLoginTime").asString
                                }

                                UserObject.userId = userId
                                UserObject.name = name
                                UserObject.authority = authority
                                UserObject.lastLoginTime = lastLoginTime

                                completion(RESPONSE_STATUS.OKAY)
                            }
                        }
                    }
                }

            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d(TAG, "RetrofitManager - onFailure() called / t : $t")
                completion(RESPONSE_STATUS.FAIL)
            }

        })

    }

    // 내 정보 수정
    fun editMyInformation(
        token: String,
        myInfo: MyPageEditActivity.MyEditInfo,
        completion: (RESPONSE_STATUS) -> Unit,
    ) {
        val call = iRetrofit?.editMyInformation(token, myInfo) ?: return

        call.enqueue(object : retrofit2.Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(TAG, "RetrofitManager - editMyInformation() / 내 정보 수정 response : $response")

                val responseCode = response.code() / 100
                Log.d(TAG, "RetrofitManager - editMyInformation() called $responseCode")

                when (responseCode) {
                    2 -> {
                        response.body()?.let {
                            val body = it.asJsonObject
                            val code = body.get("code").asString
                            if (successCode(code).isNotEmpty()) {
                                UserObject.name = myInfo.name

                                completion(RESPONSE_STATUS.OKAY)
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d(TAG, "RetrofitManager - onFailure() called / t : $t")
                completion(RESPONSE_STATUS.FAIL)
            }

        })
    }

    // 회원 탈퇴
    fun removeMember(token: String, completion: (RESPONSE_STATUS) -> Unit) {
        val call = iRetrofit?.removeMember(token) ?: return

        call.enqueue(object : retrofit2.Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(TAG, "RetrofitManager - removeMember() / 탈퇴 response : $response")

                val responseCode = response.code() / 100
                Log.d(TAG, "RetrofitManager - removeMember() called $responseCode")

                when (responseCode) {
                    2 -> {
                        response.body()?.let {
                            val body = it.asJsonObject
                            val code = body.get("code").asString
                            if (successCode(code).isNotEmpty()) {
                                completion(RESPONSE_STATUS.OKAY)
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d(TAG, "RetrofitManager - onFailure() removeMember / t : $t")
                completion(RESPONSE_STATUS.FAIL)
            }
        })
    }

    // 수강생 ↔ 트레이너 전환
    fun changeAuthority(
        token: String,
        authority: MyPageFragment.Authority,
        completion: (RESPONSE_STATUS) -> Unit,
    ) {
        val call = iRetrofit?.changeAuthority(token, authority) ?: return

        call.enqueue(object : retrofit2.Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(TAG, "RetrofitManager - changeAuthority() / 권한 전환 response : $response")

                val responseCode = response.code() / 100
                Log.d(TAG, "RetrofitManager - changeAuthority() called $responseCode")

                when (responseCode) {
                    2 -> {
                        response.body()?.let {
                            val body = it.asJsonObject
                            val code = body.get("code").asString
                            if (successCode(code).isNotEmpty()) {
                                val data = body.get("data").asJsonObject

                                val grantType = data.get("grantType").asString
                                val accessToken = data.get("accessToken").asString

                                TokenObject.grantType = grantType
                                TokenObject.accessToken = accessToken

                                val tokenData = Token(grantType, accessToken)
                                SharedPrefManager.saveTokenInfo(tokenData)

                                completion(RESPONSE_STATUS.OKAY)
                            }
                        }
                    }
                    else -> {
                        completion(RESPONSE_STATUS.FAIL)
                    }
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d(TAG, "RetrofitManager - onFailure() removeMember / t : $t")
                completion(RESPONSE_STATUS.FAIL)
            }
        })
    }

    // 권한 정보 조회
    fun getAuthority(token: String, completion: (RESPONSE_STATUS, String?, Boolean) -> Unit) {
        val call = iRetrofit?.getAuthority(token) ?: return

        call.enqueue(object : retrofit2.Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(TAG, "RetrofitManager - getAuthority() / 권한 정보 조회 response : $response")

                val responseCode = response.code() / 100
                Log.d(TAG, "RetrofitManager - getAuthority() called $responseCode")

                when (responseCode) {
                    2 -> {
                        response.body()?.let {
                            val body = it.asJsonObject
                            val code = body.get("code").asString
                            if (successCode(code).isNotEmpty()) {
                                val data = body.get("data").asJsonObject
                                val authority = data.get("authority").asString
                                val status = data.get("status").asBoolean

                                UserObject.authority = authority

                                completion(RESPONSE_STATUS.OKAY, authority, status)
                            }
                        }
                    }
                    else -> {
                        completion(RESPONSE_STATUS.FAIL, null, false)
                    }
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d(TAG, "RetrofitManager - onFailure() removeMember / t : $t")
                completion(RESPONSE_STATUS.FAIL, null, false)
            }
        })
    }

    // 트레이너 생성
    fun createTrainer(
        token: String,
        myTrainerInfo: MyTrainerEditBasicActivity.TrainerInfo,
        completion: (RESPONSE_STATUS) -> Unit,
    ) {
        val call = iRetrofit?.createTrainer(token, myTrainerInfo) ?: return

        call.enqueue(object : retrofit2.Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(TAG,
                    "RetrofitManager - createTrainer() / 트레이너 생성 response : $response")

                val responseCode = response.code() / 100
                Log.d(TAG, "RetrofitManager - createTrainer() called $responseCode")

                when (responseCode) {
                    2 -> {
                        response.body()?.let {
                            val body = it.asJsonObject
                            val code = body.get("code").asString
                            if (successCode(code).isNotEmpty()) {
                                val data = body.get("data").asJsonObject

                                TrainerObject.sports = data.get("sports").asString
                                TrainerObject.simpleIntroduction =
                                    data.get("simpleIntroduction").asString
                                TrainerObject.career = data.get("career").asInt
                                TrainerObject.detailIntroduction =
                                    data.get("detailIntroduction").asString

                                completion(RESPONSE_STATUS.OKAY)
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d(TAG, "RetrofitManager - onFailure() removeMember / t : $t")
                completion(RESPONSE_STATUS.FAIL)
            }
        })
    }

    // 트레이너 조회
    fun getTrainerData(token: String, completion: (RESPONSE_STATUS) -> Unit) {
        val call = iRetrofit?.getTrainerInformation(token) ?: return

        call.enqueue(object : retrofit2.Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(TAG, "RetrofitManager - getTrainerData() / 트레이너 조회 response : $response")

                val responseCode = response.code() / 100
                Log.d(TAG, "RetrofitManager - getTrainerData() called $responseCode")

                when (responseCode) {
                    2 -> {
                        response.body()?.let {
                            val body = it.asJsonObject
                            val code = body.get("code").asString
                            if (successCode(code).isNotEmpty()) {
                                val data = body.get("data").asJsonObject

                                val id = data.get("id").asString
                                var sports = ""
                                if (!data.get("sports").isJsonNull) {
                                    sports = data.get("sports").asString
                                }
                                var simpleIntroduction = ""
                                if (!data.get("simpleIntroduction").isJsonNull) {
                                    simpleIntroduction = data.get("simpleIntroduction").asString
                                }
                                val career = data.get("career").asInt
                                var detailIntroduction = ""
                                if (!data.get("detailIntroduction").isJsonNull) {
                                    detailIntroduction = data.get("detailIntroduction").asString
                                }
                                var gymDto: GymDto? = null
                                if (!data.get("gymDto").isJsonNull) {
                                    val gymData = data.get("gymDto").asJsonObject

                                    val gymDataId = gymData.get("id").asString
                                    val gymDataName = gymData.get("name").asString
                                    val gymDatZipcode = gymData.get("zipCode").asString
                                    val gymDataAddress = gymData.get("address").asString
                                    val gymDataBuildingNum = gymData.get("buildingNum").asString
                                    val gymDataBusinessRegistrationNum =
                                        gymData.get("businessRegistrationNum").asString
                                    val gymDataOwnerId = gymData.get("ownerId").asString
                                    val gymDataOwnerName = gymData.get("ownerName").asString

                                    gymDto = GymDto(gymDataId,
                                        gymDataName,
                                        gymDatZipcode,
                                        gymDataAddress,
                                        gymDataBuildingNum,
                                        gymDataBusinessRegistrationNum,
                                        gymDataOwnerId,
                                        gymDataOwnerName)
                                }
                                var certificateDtoList = ""
                                if (!data.get("certificateDtoList").isJsonNull) {
                                    certificateDtoList = data.get("certificateDtoList").asString
                                }

                                TrainerObject.id = id
                                TrainerObject.sports = sports
                                TrainerObject.simpleIntroduction = simpleIntroduction
                                TrainerObject.career = career
                                TrainerObject.detailIntroduction = detailIntroduction
                                TrainerObject.gymDto = gymDto
                                TrainerObject.certificateDtoList = certificateDtoList
//                                TrainerObject.closeMode = false

                                completion(RESPONSE_STATUS.OKAY)
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d(TAG, "RetrofitManager - onFailure() removeMember / t : $t")
                completion(RESPONSE_STATUS.FAIL)
            }
        })
    }

    // 트레이너 정보 수정
    fun editTrainerInformation(
        token: String,
        myTrainerInfo: MyTrainerEditBasicActivity.TrainerInfo,
        completion: (RESPONSE_STATUS) -> Unit,
    ) {
        val call = iRetrofit?.editTrainerInformation(token, myTrainerInfo) ?: return

        call.enqueue(object : retrofit2.Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(TAG,
                    "RetrofitManager - editTrainerInformation() / 트레이너 정보 수정 response : $response")

                val responseCode = response.code() / 100
                Log.d(TAG, "RetrofitManager - editTrainerInformation() called $responseCode")

                when (responseCode) {
                    2 -> {
                        response.body()?.let {
                            val body = it.asJsonObject
                            val code = body.get("code").asString
                            if (successCode(code).isNotEmpty()) {
                                val data = body.get("data").asJsonObject

                                TrainerObject.sports = data.get("sports").asString
                                TrainerObject.simpleIntroduction =
                                    data.get("simpleIntroduction").asString
                                TrainerObject.career = data.get("career").asInt
                                TrainerObject.detailIntroduction =
                                    data.get("detailIntroduction").asString

                                completion(RESPONSE_STATUS.OKAY)
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d(TAG, "RetrofitManager - onFailure() removeMember / t : $t")
                completion(RESPONSE_STATUS.FAIL)
            }
        })
    }

    // 시설 등록
    fun createGym(
        token: String,
        gym: GymCreateActivity.Gym,
        completion: (RESPONSE_STATUS) -> Unit,
    ) {
        val call = iRetrofit?.createGym(token, gym) ?: return

        call.enqueue(object : retrofit2.Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(TAG,
                    "RetrofitManager - createGym() / 시설 등록 response : $response")

                val responseCode = response.code() / 100
                Log.d(TAG, "RetrofitManager - createGym() called $responseCode")

                when (responseCode) {
                    2 -> {
                        response.body()?.let {
                            val body = it.asJsonObject
                            val code = body.get("code").asString
                            if (successCode(code).isNotEmpty()) {
                                completion(RESPONSE_STATUS.OKAY)
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d(TAG, "RetrofitManager - onFailure() removeMember / t : $t")
                completion(RESPONSE_STATUS.FAIL)
            }
        })
    }

    // 시설 검색
    fun searchGymList(
        token: String,
        gymName: String,
        completion: (RESPONSE_STATUS, ArrayList<GymItem>?) -> Unit,
    ) {
        val call = iRetrofit?.searchGymList(token, gymName) ?: return

        call.enqueue(object : retrofit2.Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(TAG,
                    "RetrofitManager - searchGymList() / 시설 검색 response : $response")

                val responseCode = response.code() / 100
                Log.d(TAG, "RetrofitManager - searchGymList() called $responseCode")

                when (responseCode) {
                    2 -> {
                        response.body()?.let {
                            val body = it.asJsonObject
                            val code = body.get("code").asString
                            if (successCode(code).isNotEmpty()) {
                                val gymArray = ArrayList<GymItem>()

                                val results = body.getAsJsonArray("data")

                                results.forEach { resultItem ->
                                    val resultItemObject = resultItem.asJsonObject

                                    val id = resultItemObject.get("id").asString
                                    val name = resultItemObject.get("name").asString
                                    val address = resultItemObject.get("address").asString
                                    val buildingNum = resultItemObject.get("buildingNum").asString

                                    val gymItem = GymItem(
                                        id = id,
                                        name = name,
                                        address = address,
                                        buildingNum = buildingNum
                                    )
                                    gymArray.add(gymItem)
                                }

                                completion(RESPONSE_STATUS.OKAY, gymArray)
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d(TAG, "RetrofitManager - onFailure() removeMember / t : $t")
                completion(RESPONSE_STATUS.FAIL, null)
            }
        })
    }

    // 트레이너 시설 등록
    fun registerGymToTrainer(
        token: String,
        gym: MyTrainerEditGymActivity.Gym,
        completion: (RESPONSE_STATUS) -> Unit,
    ) {
        val call = iRetrofit?.registerGymToTrainer(token, gym) ?: return

        call.enqueue(object : retrofit2.Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(TAG,
                    "RetrofitManager - registerGymToTrainer() / 트레이너 시설 등록 response : $response")

                val responseCode = response.code() / 100
                Log.d(TAG, "RetrofitManager - registerGymToTrainer() called $responseCode")

                when (responseCode) {
                    2 -> {
                        response.body()?.let {
                            val body = it.asJsonObject
                            val code = body.get("code").asString

                            if (successCode(code).isNotEmpty()) {
                                val data = body.get("data").asJsonObject

                                var gymDto: GymDto? = null
                                if (!data.get("gymDto").isJsonNull) {
                                    val gymData = data.get("gymDto").asJsonObject

                                    val gymDataId = gymData.get("id").asString
                                    val gymDataName = gymData.get("name").asString
                                    val gymDatZipcode = gymData.get("zipCode").asString
                                    val gymDataAddress = gymData.get("address").asString
                                    val gymDataBuildingNum = gymData.get("buildingNum").asString
                                    val gymDataBusinessRegistrationNum =
                                        gymData.get("businessRegistrationNum").asString
                                    val gymDataOwnerId = gymData.get("ownerId").asString
                                    val gymDataOwnerName = gymData.get("ownerName").asString

                                    gymDto = GymDto(gymDataId,
                                        gymDataName,
                                        gymDatZipcode,
                                        gymDataAddress,
                                        gymDataBuildingNum,
                                        gymDataBusinessRegistrationNum,
                                        gymDataOwnerId,
                                        gymDataOwnerName)
                                }

                                TrainerObject.gymDto = gymDto

                                completion(RESPONSE_STATUS.OKAY)
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d(TAG, "RetrofitManager - onFailure() removeMember / t : $t")
                completion(RESPONSE_STATUS.FAIL)
            }
        })
    }

    // 자격증 등록
    fun addCertificate(
        token: String,
        certificate: CertificateAddActivity.Certificate,
        completion: (RESPONSE_STATUS) -> Unit,
    ) {
        val call = iRetrofit?.addCertificate(token, certificate) ?: return

        call.enqueue(object : retrofit2.Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(TAG,
                    "RetrofitManager - addCertificate() / 자격증 등록 response : $response")

                val responseCode = response.code() / 100
                Log.d(TAG, "RetrofitManager - addCertificate() called $responseCode")

                when (responseCode) {
                    2 -> {
                        response.body()?.let {
                            val body = it.asJsonObject
                            val code = body.get("code").asString
                            if (successCode(code).isNotEmpty()) {
                                completion(RESPONSE_STATUS.OKAY)
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d(TAG, "RetrofitManager - onFailure() removeMember / t : $t")
                completion(RESPONSE_STATUS.FAIL)
            }
        })
    }

    // 자격증 삭제
    fun deleteCertificate(
        token: String,
        certificateId: String,
        completion: (RESPONSE_STATUS) -> Unit,
    ) {
        val call = iRetrofit?.deleteCertificate(token, certificateId) ?: return

        call.enqueue(object : retrofit2.Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(TAG, "RetrofitManager - deleteCertificate() / 자격증 삭제 response : $response")

                val responseCode = response.code() / 100
                Log.d(TAG, "RetrofitManager - deleteCertificate() called $responseCode")

                when (responseCode) {
                    2 -> {
                        response.body()?.let {
                            val body = it.asJsonObject
                            val code = body.get("code").asString
                            if (successCode(code).isNotEmpty()) {
                                completion(RESPONSE_STATUS.OKAY)
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d(TAG, "RetrofitManager - onFailure() removeMember / t : $t")
                completion(RESPONSE_STATUS.FAIL)
            }
        })
    }

    // 자격증 목록 조회
    fun getCertificateList(
        token: String, completion: (RESPONSE_STATUS, ArrayList<CertificateItem>?) -> Unit,
    ) {
        val call = iRetrofit?.getCertificateList(token) ?: return

        call.enqueue(object : retrofit2.Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(TAG,
                    "RetrofitManager - getCertificateList() / 자격증 목록 조회 response : $response")

                val responseCode = response.code() / 100
                Log.d(TAG, "RetrofitManager - getCertificateList() called $responseCode")

                when (responseCode) {
                    2 -> {
                        response.body()?.let {
                            val body = it.asJsonObject
                            val code = body.get("code").asString
                            if (successCode(code).isNotEmpty()) {
                                val certificateListArray = ArrayList<CertificateItem>()

                                val results = body.getAsJsonArray("data")

                                results.forEach { resultItem ->
                                    val resultItemObject = resultItem.asJsonObject

                                    val id = resultItemObject.get("id").asString
                                    val name = resultItemObject.get("name").asString

                                    var issuer: String? = null
                                    if (!resultItemObject.get("issuer").isJsonNull) {
                                        issuer =
                                            resultItemObject.get("issuer").asString
                                    }

                                    var acquisitionDate: String? = null
                                    if (!resultItemObject.get("acquisitionDate").isJsonNull) {
                                        acquisitionDate =
                                            resultItemObject.get("acquisitionDate").asString
                                    }


                                    val certificateItem = CertificateItem(
                                        id = id,
                                        name = name,
                                        issuer = issuer,
                                        acquisitionDate = acquisitionDate
                                    )
                                    certificateListArray.add(certificateItem)
                                }

                                completion(RESPONSE_STATUS.OKAY, certificateListArray)
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d(TAG, "RetrofitManager - onFailure() removeMember / t : $t")
                completion(RESPONSE_STATUS.FAIL, null)
            }
        })
    }
}