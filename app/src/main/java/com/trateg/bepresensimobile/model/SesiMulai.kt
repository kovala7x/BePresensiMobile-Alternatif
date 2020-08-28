package com.trateg.bepresensimobile.model


import com.google.gson.annotations.SerializedName

data class SesiMulai(
    @SerializedName("jam_berakhir")
    val jamBerakhir: String?,
    @SerializedName("jam_mulai")
    val jamMulai: String?,
    @SerializedName("kd_sesi")
    val kdSesi: Int?
)