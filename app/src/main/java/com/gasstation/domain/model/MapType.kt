package com.gasstation.domain.model

enum class MapType(val map: String) {
    TMAP("티맵"), KAKAO("카카오네비"), NAVER("네이버지도");

    companion object {
        fun getMap(map: String): MapType =
            when (map) {
                TMAP.map -> TMAP
                KAKAO.map -> KAKAO
                NAVER.map -> NAVER
                else -> {
                    TMAP
                }
            }
    }
}