package com.gasstation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.gasstation.common.ResultWrapper
import com.gasstation.domain.model.Coords
import com.gasstation.domain.model.RESULT
import com.gasstation.domain.model.SettingType
import com.gasstation.domain.model.SortType
import com.gasstation.domain.repository.GasStationRepository
import com.gasstation.domain.repository.SharePrefsRepository
import com.gasstation.extensions.resultCallbackFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val gasStationRepository: GasStationRepository,
    private val sharePrefsRepo: SharePrefsRepository
) : ViewModel() {
    val currentAddress = MutableStateFlow<ResultWrapper<String>>(ResultWrapper.Start)
    val gasStationsResult = MutableStateFlow<ResultWrapper<RESULT>>(ResultWrapper.Start)
    private val sortType = mutableStateOf(sharePrefsRepo.sortType)
    private val oilType = mutableStateOf(sharePrefsRepo.oilType)
    fun getSortType() = sortType.value

    fun getOilType() = oilType.value

    fun getMapType() = sharePrefsRepo.mapType

    fun changeSortType() {
        val gasStations = gasStationsResult.value.takeValueOrThrow().OIL
        if (SortType.DISTANCE.sortType == getSortType()) {
            sharePrefsRepo.sortType = SortType.PRICE.sortType
            sortType.value = SortType.PRICE.sortType
            gasStationsResult.value.takeValueOrThrow().OIL =
                gasStations.sortedBy { it.PRICE }
        } else {
            sharePrefsRepo.sortType = SortType.DISTANCE.sortType
            sortType.value = SortType.DISTANCE.sortType
            gasStationsResult.value.takeValueOrThrow().OIL =
                gasStations.sortedBy { it.DISTANCE }
        }
    }

    fun getCurrentAddress(x: Double, y: Double, inputCoord: String) =
        resultCallbackFlow(currentAddress) {
            gasStationRepository.getCurrentAddress(x, y, inputCoord)
        }

    fun getGasStationList(x: Double, y: Double, inputCoord: String, outputCoord: String) =
        resultCallbackFlow(gasStationsResult) {
            gasStationRepository.getGasStationList(
                x,
                y,
                inputCoord,
                outputCoord,
                sharePrefsRepo.distanceType,
                sharePrefsRepo.sortType,
                sharePrefsRepo.oilType,
                sharePrefsRepo.gasStationType
            ).apply {
                if (this is ResultWrapper.Success) {
                    sortType.value = sharePrefsRepo.sortType
                    oilType.value = sharePrefsRepo.oilType
                }
            }
        }

    fun saveSetting(settingType: SettingType, type: String) {
        sharePrefsRepo.saveSetting(settingType, type)
    }

    fun getCurrentSettingType(settingType: SettingType): String {
        return when (settingType) {
            SettingType.DISTANCE_TYPE -> {
                sharePrefsRepo.distanceType
            }

            SettingType.OIL_TYPE -> {
                sharePrefsRepo.oilType
            }

            SettingType.GAS_STATION_TYPE -> {
                sharePrefsRepo.gasStationType
            }

            SettingType.SORT_TYPE -> {
                sharePrefsRepo.sortType
            }

            SettingType.MAP_TYPE -> {
                sharePrefsRepo.mapType
            }
        }
    }

    suspend fun landingMap(x: Double, y: Double, openMap: (String, String) -> Unit) {
        val result = gasStationRepository.transCoord(x, y, Coords.KTM.name, Coords.WGS84.name)
        if (result != null) {
            openMap(result.x.toString(), result.y.toString())
        }
    }

}