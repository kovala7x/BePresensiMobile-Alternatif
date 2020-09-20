package com.trateg.bepresensimobile.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StatusSurat(
    @SerializedName("kd_status_surat")
    val kdStatusSurat: Int?,
    @SerializedName("keterangan_surat")
    val keteranganSurat: String?
) : Parcelable