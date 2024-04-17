package com.gasstation.data.network

import com.gasstation.domain.model.Coord2address
import com.gasstation.domain.model.TransCoord
import retrofit2.http.GET
import retrofit2.http.Query

interface KakaoService {
    @GET("/v2/local/geo/coord2address.json")
    suspend fun coord2address(
        @Query("x") x: Double,
        @Query("y") y: Double,
        @Query("input_coord") inputCoord: String
    ): Coord2address

    @GET("v2/local/geo/transcoord.json")
    suspend fun tanscoord(
        @Query("x") x: Double,
        @Query("y") y: Double,
        @Query("input_coord") inputCoord: String,
        @Query("output_coord") outputCoord: String
    ): TransCoord
}