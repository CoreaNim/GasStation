package com.gasstation.viewmodel

import androidx.lifecycle.ViewModel
import com.gasstation.common.ResultWrapper
import com.gasstation.domain.model.OPINET
import com.gasstation.domain.repository.GasStationRepository
import com.gasstation.extensions.resultCallbackFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val gasStationRepository: GasStationRepository
) : ViewModel() {
    val gasStationsResult = MutableStateFlow<ResultWrapper<OPINET>>(ResultWrapper.Start)
    fun getGasStationList(x: Double, y: Double, inputCoord: String, outputCoord: String) =
        resultCallbackFlow(gasStationsResult) {
            gasStationRepository.getGasStationList(x, y, inputCoord, outputCoord).apply {
                if (this is ResultWrapper.Success) {
                    Timber.i("list size = " + takeValueOrThrow().RESULT.OIL.size)
                }
            }
        }
}