package com.gasstation.domain.repository

import com.gasstation.common.ResultWrapper
import com.gasstation.const.Const
import com.gasstation.data.network.KakaoService
import com.gasstation.data.network.OpinetService
import com.gasstation.domain.model.CoordDocument
import com.gasstation.domain.model.DistanceType
import com.gasstation.domain.model.GasStationType
import com.gasstation.domain.model.OilType
import com.gasstation.domain.model.RESULT
import com.gasstation.domain.model.SortType
import com.gasstation.domain.model.TransCoord
import com.gasstation.extensions.safeApiCall
import javax.inject.Inject

class GasStationRepository @Inject constructor(
    private val kakaoService: KakaoService,
    private val opinetService: OpinetService
) {

    suspend fun getCurrentAddress(
        x: Double,
        y: Double,
        inputCoord: String
    ): ResultWrapper<String> {
        return safeApiCall {
            val coord2address = kakaoService.coord2address(x, y, inputCoord)
            coord2address.documents?.first()?.address?.address_name ?: ""
        }
    }

    suspend fun transCoord(
        x: Double,
        y: Double,
        inputCoord: String,
        outputCoord: String
    ): CoordDocument? {
        return kakaoService.tanscoord(x, y, inputCoord, outputCoord).documents?.firstOrNull()
    }

    suspend fun getGasStationList(
        x: Double,
        y: Double,
        inputCoord: String,
        outputCoord: String,
        distanceType: String,
        sortType: String,
        oilType: String,
        gasStationType: String,
    ): ResultWrapper<RESULT> {
        return safeApiCall {
            val transCoord = kakaoService.tanscoord(x, y, inputCoord, outputCoord)
            val documents = transCoord.documents?.first()
            if (documents?.x != null && documents.y != null) {
                val result = opinetService.findAllGasStation(
                    Const.OPINET_API_KEY,
                    documents.x,
                    documents.y,
                    DistanceType.getDistance(distanceType),
                    SortType.getSort(sortType),
                    OilType.getOilType(oilType),
                    "json"
                ).apply {
                    this.RESULT.OIL = this.RESULT.OIL.filter {
                        GasStationType.ALL.name == GasStationType.getGasStation(
                            gasStationType
                        ) ||
                                it.POLL_DIV_CD == GasStationType.getGasStation(
                            gasStationType
                        )
                    }
                }
                return@safeApiCall result.RESULT
            } else {
                return@safeApiCall RESULT(emptyList())
            }
        }
    }
}