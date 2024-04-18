package com.gasstation.domain.repository

import com.gasstation.common.ResultWrapper
import com.gasstation.const.Const
import com.gasstation.data.network.KakaoService
import com.gasstation.data.network.OpinetService
import com.gasstation.domain.model.DistanceType
import com.gasstation.domain.model.OPINET
import com.gasstation.domain.model.OilType
import com.gasstation.domain.model.RESULT
import com.gasstation.domain.model.SortType
import com.gasstation.extensions.safeApiCall
import javax.inject.Inject

class GasStationRepository @Inject constructor(
    private val kakaoService: KakaoService,
    private val opinetService: OpinetService
) {

    suspend fun getGasStationLists(x: Double, y: Double, inputCoord: String, outputCoord: String) {
        val coord2address = kakaoService.coord2address(x, y, inputCoord)
        val transCoord = kakaoService.tanscoord(x, y, inputCoord, outputCoord)

        val documents = transCoord.documents?.first()
        if (documents?.x != null && documents?.y != null) {
            val opinet = opinetService.findAllGasStation(
                Const.OPINET_API_KEY,
                documents.x,
                documents.y,
                DistanceType.getDistance(DistanceType.D5.distance),
                SortType.getSort(SortType.DISTANCE.sortType),
                OilType.getOilType(OilType.B027.oil),
                "json"
            )
        }
    }

    suspend fun getGasStationList(
        x: Double,
        y: Double,
        inputCoord: String,
        outputCoord: String
    ): ResultWrapper<OPINET> {
        return safeApiCall {
            val coord2address = kakaoService.coord2address(x, y, inputCoord)
            val transCoord = kakaoService.tanscoord(x, y, inputCoord, outputCoord)
            var result = OPINET(RESULT(emptyList()))
            val documents = transCoord.documents?.first()
            if (documents?.x != null && documents?.y != null) {
                result = opinetService.findAllGasStation(
                    Const.OPINET_API_KEY,
                    documents.x,
                    documents.y,
                    DistanceType.getDistance(DistanceType.D5.distance),
                    SortType.getSort(SortType.DISTANCE.sortType),
                    OilType.getOilType(OilType.B027.oil),
                    "json"
                )
            }
            result
        }
    }
}