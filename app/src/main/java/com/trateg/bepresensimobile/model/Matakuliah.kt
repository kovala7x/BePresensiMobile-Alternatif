package com.trateg.bepresensimobile.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Matakuliah(
    @SerializedName("kd_matakuliah")
    val kdMatakuliah: String?,
    @SerializedName("nama_matakuliah")
    val namaMatakuliah: String?
) : Parcelable