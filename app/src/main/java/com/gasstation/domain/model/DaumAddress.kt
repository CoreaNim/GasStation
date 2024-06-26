package com.gasstation.domain.model

data class Coord2address(
    var documents: List<AddressDocument>?,
    var meta: Meta
)

data class AddressDocument(
    var road_address: Road_address,
    var address: Address
)

data class Road_address(
    var building_name: String,
    var sub_building_no: String,
    var region_2depth_name: String,
    var underground_yn: String,
    var region_1depth_name: String,
    var zone_no: String,
    var address_name: String,
    var road_name: String,
    var main_building_no: String,
    var region_3depth_name: String
)

data class Address(
    var main_address_no: String,
    var zip_code: String,
    var region_2depth_name: String,
    var sub_address_no: String,
    var region_1depth_name: String,
    var mountain_yn: String,
    var address_name: String,
    var region_3depth_name: String
)

data class Meta(var total_count: Int)

data class TransCoord(
    var documents: List<CoordDocument>?,
    var meta: Meta
)

data class CoordDocument(var x: Double, var y: Double)