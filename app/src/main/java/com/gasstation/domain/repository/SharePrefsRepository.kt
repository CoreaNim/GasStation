package com.gasstation.domain.repository

import android.content.SharedPreferences
import com.gasstation.domain.model.DistanceType
import com.gasstation.domain.model.GasStationType
import com.gasstation.domain.model.MapType
import com.gasstation.domain.model.OilType
import com.gasstation.domain.model.SortType
import com.gasstation.extensions.string

class SharePrefsRepository(sharePrefs: SharedPreferences) {

    var distanceType by sharePrefs.string(defaultValue = DistanceType.D20.distance)
    var oilType by sharePrefs.string(defaultValue = OilType.B027.oil)
    var gasStationType by sharePrefs.string(defaultValue = GasStationType.ALL.gasStation)
    var sortType by sharePrefs.string(defaultValue = SortType.DISTANCE.sortType)
    var mapType by sharePrefs.string(defaultValue = MapType.TMAP.map)

}