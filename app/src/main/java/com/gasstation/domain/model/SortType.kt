package com.gasstation.domain.model

enum class SortType(val sortType: String) {
    DISTANCE("거리순 보기"), PRICE("가격순 보기");

    companion object {
        fun getSort(sortType: String): String =
            when (sortType) {
                DISTANCE.sortType -> "2"
                else -> "1"
            }
    }
}