package com.gasstation.viewmodel

import androidx.lifecycle.ViewModel
import com.gasstation.common.ResultWrapper
import com.gasstation.domain.model.OPINET
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
    val gasStationsResult = MutableStateFlow<ResultWrapper<OPINET>>(ResultWrapper.Start)
    fun getCurrentAddress() = currentAddress.value

    fun getSortyType() = sharePrefsRepo.sortType

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
                    Timber.i("list size = " + takeValueOrThrow().RESULT.OIL.size)
                }
            }
        }
}