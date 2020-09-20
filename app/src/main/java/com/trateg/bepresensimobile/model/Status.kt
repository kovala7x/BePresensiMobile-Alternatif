package com.trateg.bepresensimobile.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Status(
    @SerializedName("kd_status_rekapitulasi")
    val kdStatusRekapitulasi: String?,
    @SerializedName("keterangan_rekapitulasi")
    val keteranganRekapitulasi: String?
) : Parcelable