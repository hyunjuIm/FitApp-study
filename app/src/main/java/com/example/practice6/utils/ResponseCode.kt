package com.example.practice6.utils

fun successCode(code: String): String {
    when (code) {
        "A1000" -> return "사용자 생성"
        "A1100" -> return "사용자 정보 조회"
        "A1200" -> return "사용자 정보 수정"
        "A1210" -> return "사용자 비밀번호 수정"
        "A1220" -> return "사용자 권한 정보 수정"
        "A1300" -> return "사용자 삭제"

        "A2000" -> return "로그인"
        "A2100" -> return "로그아웃"
        "A2200" -> return "토큰 재발급"
        "A2300" -> return "문자 발신 성공"
        "A2400" -> return "휴대폰 인증 성공"
        "A2410" -> return "휴대폰 인증 실패"
        "A2500" -> return "성공-이메일 인증번호 발신"
        "A2600" -> return "실패-이메일 인증"
        "A2610" -> return "실패-이메일 인증"

        "A3110" -> return "트레이너 생성"
        "A3111" -> return "트레이너 시설등록"
        "A3120" -> return "트레이너 조회"
        "A3130" -> return "트레이너 수정"
        "A3140" -> return "트레이너 삭제"

        "A3210" -> return "시설 생성"
        "A3220" -> return "시설 조회"
        "A3230" -> return "시설 수정"
        "A3240" -> return "시설 삭제"

        "A3310" -> return "자격증 생성"
        "A3320" -> return "자격증 조회"
        "A3340" -> return "자격증 삭제"

        else -> return ""
    }
}

fun errorCode(code: String): String {
    when (code) {
        "E9000" -> return "예외 처리 되지 않은 오류"
        "E9900" -> return "해당 작업에 대한 권한이 없음"
        "E2000" -> return "빈값이 존재함"
        "E2100" -> return "DB에서 찾을 수 없음"
        "E2200" -> return "값이 너무 큼"
        "E2300" -> return "값이 너무 작음"

        "E3000" -> return "액세스 권한이 없음"
        "E3100" -> return "토큰이 만료됨"
        "E3110" -> return "잘못된 타입의 토큰임"
        "E3120" -> return "지원되지 않는 토큰임"
        "E3200" -> return "리프레시 토큰이 만료됨"
        "E3300" -> return "리프레시 토큰이 일치하지 않음"
        "E3400" -> return "사용자 ID,PW가 일치하지 않음"
        "E3500" -> return "휴대폰 인증번호가 일치하지 않음"

        "E1000" -> return "사용자을 찾을 수 없음"
        "E1100" -> return "이미 사용자가 존재함"
        "E1200" -> return "이미 존재하는 이메일임"

        else -> return ""
    }
}