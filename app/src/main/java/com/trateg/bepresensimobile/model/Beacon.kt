package com.trateg.bepresensimobile.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Beacon(
    @SerializedName("kd_beacon")
    val kdBeacon: String?,
    @SerializedName("mac_address")
    val macAddress: String?,
    @SerializedName("major")
    val major: String?,
    @SerializedName("minor")
    val minor: String?
) : Parcelable