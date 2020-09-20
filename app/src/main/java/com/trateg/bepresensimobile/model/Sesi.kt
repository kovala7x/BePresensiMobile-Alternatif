package com.trateg.bepresensimobile.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Sesi(
    @SerializedName("jam_berakhir")
    val jamBerakhir: String?,
    @SerializedName("jam_mulai")
    val jamMulai: String?,
    @SerializedName("kd_sesi")
    val kdSesi: Int?
) : Parcelable