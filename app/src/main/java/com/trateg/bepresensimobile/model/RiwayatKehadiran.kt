package com.trateg.bepresensimobile.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RiwayatKehadiran (
    @SerializedName("tanggal")
    val tanggal: String?,
    @SerializedName("jadwal")
    val jadwal: List<Jadwal>?
) : Parcelable