package com.trateg.bepresensimobile.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Rekapitulasi(
    @SerializedName("alfa")
    val alfa: Int?,
    @SerializedName("izin")
    val izin: Int?,
    @SerializedName("mahasiswa")
    val mahasiswa: Mahasiswa?,
    @SerializedName("sakit")
    val sakit: Int?,
    @SerializedName("status")
    val status: Status?
) : Parcelable