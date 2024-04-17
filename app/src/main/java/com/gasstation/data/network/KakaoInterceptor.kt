package com.gasstation.data.network

import com.gasstation.const.Const
import okhttp3.Interceptor
import okhttp3.Response


class KakaoInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder()
            .addHeader(AUTHORIZATION, KAKAOAK + " " + Const.KAKAO_API_KEY)
        return chain.proceed(newRequest.build())
    }

    companion object {
        const val AUTHORIZATION = "Authorization"
        const val KAKAOAK = "KakaoAK"
    }
}