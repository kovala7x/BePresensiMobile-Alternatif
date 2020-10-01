package com.trateg.bepresensimobile.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PersentaseKehadiran(
    @SerializedName("kd_sesi")
    val kdSesi: Int?,
    @SerializedName("persentase")
    val persentase: String?
) : Parcelable