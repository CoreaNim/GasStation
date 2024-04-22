package com.gasstation.domain.model

enum class DistanceType(val distance: String) {
    D3("3km"), D4("4km"), D5("5km");

    companion object {
        fun getDistance(distance: String): String =
            when (distance) {
                D3.distance -> "3000"
                D4.distance -> "4000"
                D5.distance -> "5000"
                else -> "3000"
            }
    }
}