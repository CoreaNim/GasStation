package com.gasstation.domain.model

enum class DistanceType(val distance: String) {
    D3("3km"), D5("5km"), D10("10km");

    companion object {
        fun getDistance(distance: String): String =
            when (distance) {
                D3.distance -> "3000"
                D5.distance -> "5000"
                D10.distance -> "10000"
                else -> "3000"
            }
    }
}