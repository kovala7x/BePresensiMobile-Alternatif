package com.trateg.bepresensimobile.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Ruang(
    @SerializedName("beacon")
    val beacon: Beacon?,
    @SerializedName("kd_beacon")
    val kdBeacon: String?,
    @SerializedName("kd_ruang")
    val kdRuang: String?,
    @SerializedName("nama_ruang")
    val namaRuang: String?
) : Parcelable