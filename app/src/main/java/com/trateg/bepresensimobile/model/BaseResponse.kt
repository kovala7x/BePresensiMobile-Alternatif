package com.trateg.bepresensimobile.model


import com.google.gson.annotations.SerializedName

data class BaseResponse(
    @SerializedName("data")
    val data: Data?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("success")
    val success: Boolean?
)

data class BaseResponseList(
    @SerializedName("data")
    val data: ListData?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("success")
    val success: Boolean?
)