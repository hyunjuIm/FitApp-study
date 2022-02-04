package com.example.practice6.utils

object Constants {
    const val TAG: String = "로그"
}

object CLIECK_MENU {
    const val CHAT = "chat"
    const val MY_PAGE = "my_page"
}

enum class RESPONSE_STATUS {
    OKAY,
    FAIL
}

object API {
    const val BASE_URL = "https://fapi.leescode.com/"
    // 인증번호 요청
    const val REQUEST_CODE = "account/phone/cert"
    // 인증번호 확인
    const val REQUEST_CODE_VERIFICATION = "account/phone/verify"
    // 로그인
    const val ACCOUNT_LOGIN = "account/login"

    // 아이디 중복검사
    const val ACCOUNT_EXISTS_ID = "account/exists/id/{id}"
    // 사용자 가입, 정보 조회
    const val MEMBER_USER = "user"
    // 내 정보 수정
    const val EDIT_MY_INFO = "user/info"

    // 수강생 ↔ 트레이너 전환, 권한 정보 조회
    const val AUTHORITY = "account/change/auth"
    // 권한 정보 상태 조회
    const val USER_STATUS = "user/status"

    // 트레이너 조회
    const val TRAINER = "trainer"

    // 시설
    const val GYM = "gym"
    const val TRAINER_REGISTER_GYM = "trainer/register/gym"

    // 자격증
    const val CERTIFICATE = "certificate"
    const val DELETE_CERTIFICATE = "certificate/{certificateId}"
}