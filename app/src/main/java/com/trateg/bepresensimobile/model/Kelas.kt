package com.trateg.bepresensimobile.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Kelas(
    @SerializedName("angkatan")
    val angkatan: Int?,
    @SerializedName("kd_kelas")
    val kdKelas: String?,
    @SerializedName("kd_wali_dosen")
    val kdWaliDosen: String?,
    @SerializedName("prodi")
    val prodi: String?,
    @SerializedName("tingkat_kelas")
    val tingkatKelas: Int?
) : Parcelable