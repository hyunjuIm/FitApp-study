package com.example.practice6.retrofit

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.example.practice6.App
import com.example.practice6.utils.Constants.TAG
import com.example.practice6.utils.RESPONSE_STATUS
import com.example.practice6.utils.errorCode
import com.example.practice6.utils.isJsonArray
import com.example.practice6.utils.isJsonObject
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okio.Buffer
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.util.concurrent.TimeUnit
import okio.BufferedSource
import java.nio.charset.Charset


object RetrofitClient {

    // 레트로핏 클라이언트 선언
    private var retrofitClient: Retrofit? = null

    // 레트로핏 클라이언트 가져오기
    fun getClient(baseUrl: String): Retrofit? {
        Log.d(TAG, "RetrofitClient - getClient() called")

        // okhttp 인스턴스 생성
        val client = OkHttpClient.Builder()

        // 로그를 찍기 위해 로깅 인터셉터 설정
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            Log.d(TAG, "RetrofitClient - log() called / message: $message")
            when {
                message.isJsonObject() -> Log.d(TAG, JSONObject(message).toString(4))
                message.isJsonArray() -> Log.d(TAG, JSONObject(message).toString(4))
                else -> {
                    try {
                        val errorMessage = JSONObject(message).getString("message")
                        val errorCode = JSONObject(message).getString("code")
                        Log.d(TAG, "메시지 : $errorMessage")
                        Log.d(TAG, "코드 : $errorCode")

                        Toast.makeText(App.instance, errorCode(errorCode), Toast.LENGTH_LONG).show()
                    } catch (e: Exception) {
                        Log.d(TAG, message)
                    }
                }
            }
        }

        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        //위에서 설정한 로깅 인터셉터를 okhttp 클라이언트에 추가
        client.addInterceptor(loggingInterceptor)

        // 기본 파라미터 인터셉터 설정
        val baseParameterInterceptor: Interceptor = (Interceptor { chain ->
            Log.d(TAG, "RetrofitClient - intercept() called")
            //오리지날 리퀘스트
            val originalRequest = chain.request()
            val response = chain.proceed(originalRequest)

//            val code = JSONObject(response.peekBody(2048).string()).getString("code")
//            Log.d(TAG, "RetrofitClient - code: $code, ${errorCode(code)}")
//            if ((response.code / 100) != 2) {
//                // ui 스레드가 아닌 곳에서 메인 Looper 스레드에서 돌려야한다!!
//                Handler(Looper.getMainLooper()).post {
//                    Toast.makeText(App.instance, errorCode(code), Toast.LENGTH_LONG).show()
//                }
//            }

            response
        })

        //위에서 설정한 기본 파라미터 인터셉터를 okhttp 클라이언트에 추가
        client.addInterceptor(baseParameterInterceptor)

        //커넥션 타임아웃
        client.connectTimeout(5, TimeUnit.SECONDS)
        client.readTimeout(5, TimeUnit.SECONDS)
        client.writeTimeout(5, TimeUnit.SECONDS)
        client.retryOnConnectionFailure(true)

        if (retrofitClient == null) {
            // 레트로핏 빌더를 통해 인스턴스 생성
            retrofitClient = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                // 위에서 설정한 클라이언트로 레트로핏 클라이언트를 설정
                .client(client.build())
                .build()
        }

        return retrofitClient
    }

}