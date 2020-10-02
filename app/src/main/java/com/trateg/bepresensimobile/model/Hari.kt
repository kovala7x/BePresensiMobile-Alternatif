package com.trateg.bepresensimobile.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Hari(
    @SerializedName("kd_hari")
    val kdHari: Int?,
    @SerializedName("nama_hari")
    val namaHari: String?
) : Parcelable