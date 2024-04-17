package com.gasstation.data.network

import com.gasstation.domain.model.OPINET
import retrofit2.http.GET
import retrofit2.http.Query

interface OpinetService {
    @GET("/api/aroundAll.do")
    suspend fun findAllGasStation(
        @Query("code") code: String,
        @Query("x") x: Double,
        @Query("y") y: Double,
        @Query("radius") radius: String,
        @Query("sort") sort: String,
        @Query("prodcd") prodcd: String,
        @Query("out") out: String
    ): OPINET
}