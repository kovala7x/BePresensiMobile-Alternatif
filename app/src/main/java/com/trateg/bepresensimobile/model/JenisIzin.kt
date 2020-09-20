package com.trateg.bepresensimobile.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class JenisIzin(
    @SerializedName("kd_status_presensi")
    val kdStatusPresensi: String?,
    @SerializedName("keterangan_presensi")
    val keteranganPresensi: String?
) : Parcelable