package com.gasstation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.gasstation.common.ResultWrapper
import com.gasstation.domain.model.RESULT
import com.gasstation.domain.model.SortType
import com.gasstation.domain.repository.GasStationRepository
import com.gasstation.domain.repository.SharePrefsRepository
import com.gasstation.extensions.resultCallbackFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val gasStationRepository: GasStationRepository,
    private val sharePrefsRepo: SharePrefsRepository
) : ViewModel() {
    val currentAddress = MutableStateFlow<ResultWrapper<String>>(ResultWrapper.Start)
    val gasStationsResult = MutableStateFlow<ResultWrapper<RESULT>>(ResultWrapper.Start)
    private val sortType = mutableStateOf(sharePrefsRepo.sortType)
    fun getCurrentAddress() = currentAddress.value

    fun getSortType() = sortType.value

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
                sharePrefsRepo.oilType
            ).apply {
                if (this is ResultWrapper.Success) {
                    Timber.i("list size = " + takeValueOrThrow().OIL.size)
                }
            }
        }
}