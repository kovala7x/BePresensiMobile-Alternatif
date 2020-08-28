package com.trateg.bepresensimobile.model


import com.google.gson.annotations.SerializedName

data class Beacon(
    @SerializedName("kd_beacon")
    val kdBeacon: String?,
    @SerializedName("mac_address")
    val macAddress: String?,
    @SerializedName("major")
    val major: String?,
    @SerializedName("minor")
    val minor: String?
)