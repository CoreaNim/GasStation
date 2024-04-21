package com.gasstation.domain.repository

import android.content.SharedPreferences
import com.gasstation.domain.model.DistanceType
import com.gasstation.domain.model.GasStationType
import com.gasstation.domain.model.MapType
import com.gasstation.domain.model.OilType
import com.gasstation.domain.model.SettingType
import com.gasstation.domain.model.SortType
import com.gasstation.extensions.string

class SharePrefsRepository(sharePrefs: SharedPreferences) {

    var distanceType by sharePrefs.string(defaultValue = DistanceType.D3.distance)
    var oilType by sharePrefs.string(defaultValue = OilType.B027.oil)
    var gasStationType by sharePrefs.string(defaultValue = GasStationType.ALL.gasStation)
    var sortType by sharePrefs.string(defaultValue = SortType.DISTANCE.sortType)
    var mapType by sharePrefs.string(defaultValue = MapType.TMAP.map)

    fun saveSetting(settingType: SettingType, type: String) {
        when (settingType) {
            SettingType.DISTANCE_TYPE -> {
                distanceType = type
            }

            SettingType.OIL_TYPE -> {
                oilType = type
            }

            SettingType.GAS_STATION_TYPE -> {
                gasStationType = type
            }

            SettingType.SORT_TYPE -> {
                sortType = type
            }

            SettingType.MAP_TYPE -> {
                mapType = type
            }
        }
    }

}