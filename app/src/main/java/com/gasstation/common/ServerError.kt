package com.gasstation.common

import com.google.gson.annotations.SerializedName

class ServerError(
    val code: Int,
    val message: String,
    val details: List<String>
)

fun DtoErrorResponse.toDomainModel() = ServerError(
    code = code ?: 0,
    message = message.orEmpty(),
    details = details?.map { it.orEmpty() }.orEmpty()
)

class DtoErrorResponse(
    @SerializedName("code")
    val code: Int?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("details")
    val details: List<String?>?
)