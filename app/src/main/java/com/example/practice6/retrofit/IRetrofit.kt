package com.example.practice6.retrofit

import com.example.practice6.CertificateAddActivity
import com.example.practice6.activities.LoginActivity
import com.example.practice6.activities.MyPageFragment
import com.example.practice6.activities.join.JoinNameFragment
import com.example.practice6.activities.my.*
import com.example.practice6.model.*
import com.example.practice6.utils.API
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.*

import retrofit2.http.Body

interface IRetrofit {

    // 인증번호 요청
    @POST(API.REQUEST_CODE)
    fun requestVerificationCode(@Body phone: LoginActivity.PhoneInfo): Call<JsonElement>

    // 인증번호 확인
    @POST(API.REQUEST_CODE_VERIFICATION)
    fun checkTheVerificationNumber(@Body phoneCode: LoginActivity.PhoneCodeInfo): Call<JsonElement>

    // 회원가입
    @POST(API.MEMBER_USER)
    fun joinMember(@Body joinInfo: JoinNameFragment.JoinInfo): Call<JsonElement>

    // 아이디 중복확인
    @GET(API.ACCOUNT_EXISTS_ID)
    fun checkExistsUserId(@Path("id") userId: String): Call<JsonElement>

    // 로그인
    @POST(API.ACCOUNT_LOGIN)
    fun loginMember(@Body login: Login): Call<JsonElement>

    // 마이페이지 내 정보 조회
    @GET(API.MEMBER_USER)
    fun getMyInformation(@Header("token") token: String): Call<JsonElement>

    // 마이페이지 내 정보 수정
    @PUT(API.EDIT_MY_INFO)
    fun editMyInformation(@Header("token") token: String, @Body myInfo: MyPageEditActivity.MyEditInfo): Call<JsonElement>

    // 회원 탈퇴
    @DELETE(API.MEMBER_USER)
    fun removeMember(@Header("token") token: String): Call<JsonElement>

    // 수강생 ↔ 트레이너 전환
    @PUT(API.AUTHORITY)
    fun changeAuthority(@Header("token") token: String, @Body authority: MyPageFragment.Authority): Call<JsonElement>

    // 권한 정보 상태 조회
    @GET(API.USER_STATUS)
    fun getAuthority(@Header("token") token: String): Call<JsonElement>

    // 트레이너 생성
    @POST(API.TRAINER)
    fun createTrainer(@Header("token") token: String, @Body myTrainerInfo: MyTrainerEditBasicActivity.TrainerInfo): Call<JsonElement>

    // 트레이너 조회
    @GET(API.TRAINER)
    fun getTrainerInformation(@Header("token") token: String): Call<JsonElement>

    // 트레이너 정보 수정
    @PUT(API.TRAINER)
    fun editTrainerInformation(@Header("token") token: String, @Body myTrainerInfo: MyTrainerEditBasicActivity.TrainerInfo): Call<JsonElement>

    // 시설 등록
    @POST(API.GYM)
    fun createGym(@Header("token") token: String, @Body gym: GymCreateActivity.Gym): Call<JsonElement>

    // 시설 리스트 이름으로 조회
    @GET(API.GYM)
    fun searchGymList(@Header("token") token: String, @Query("searchWord") gymName:String): Call<JsonElement>

    // 트레이너 시설 등록
    @PUT(API.TRAINER_REGISTER_GYM)
    fun registerGymToTrainer(@Header("token") token: String, @Body gym: MyTrainerEditGymActivity.Gym): Call<JsonElement>

    // 자격증 추가
    @POST(API.CERTIFICATE)
    fun addCertificate(@Header("token") token: String, @Body certificate: CertificateAddActivity.Certificate): Call<JsonElement>

    // 자격증 삭제
    @DELETE(API.DELETE_CERTIFICATE)
    fun deleteCertificate(@Header("token") token: String, @Path("certificateId") certificateId: String): Call<JsonElement>

    // 자격증 목록 조회
    @GET(API.CERTIFICATE)
    fun getCertificateList(@Header("token") token: String): Call<JsonElement>
}
