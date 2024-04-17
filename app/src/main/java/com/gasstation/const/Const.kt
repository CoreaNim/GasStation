package com.gasstation.const

import com.gasstation.BuildConfig

class Const {
    companion object {

        const val DAUM_API_URL: String = "https://dapi.kakao.com/"
        const val OPINET_API_URL: String = "http://www.opinet.co.kr"

        const val SETTING_TYPE = "SETTING_TYPE"
        const val BUS_GET_GAS_LIST = "BUS_GET_GAS_LIST"
        const val BUS_SORT_GAS_LIST = "BUS_SORT_GAS_LIST"

        var OPINET_API_KEY: String = "BuildConfig.OPINET_API_KEY"

    }
}