package com.gasstation.di

import com.gasstation.const.Const
import com.gasstation.data.network.KakaoInterceptor
import com.gasstation.data.network.KakaoService
import com.gasstation.data.network.OpinetService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    @Named("KAKAO_URL")
    fun provideKakaoUrl() = Const.KAKAO_API_URL

    @Singleton
    @Provides
    @Named("OPINET_URL")
    fun provideOpinetUrl() = Const.OPINET_API_URL

    @Singleton
    @Provides
    fun provideKakaoService(
        @Named("KAKAO_URL") baseUrl: String,
        @Named("KAKAO_OKHTTP") okHttpClient: OkHttpClient
    ): KakaoService {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(KakaoService::class.java)
    }

    @Singleton
    @Provides
    fun provideOpinetService(
        @Named("OPINET_URL") baseUrl: String,
        okHttpClient: OkHttpClient
    ): OpinetService {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpinetService::class.java)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Singleton
    @Provides
    @Named("KAKAO_OKHTTP")
    fun provideKakaoOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(KakaoInterceptor())
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

}