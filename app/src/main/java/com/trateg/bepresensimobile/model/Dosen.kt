package com.trateg.bepresensimobile.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Dosen(
    @SerializedName("foto_dosen")
    val fotoDosen: String?,
    @SerializedName("id_user")
    val idUser: Int?,
    @SerializedName("kd_dosen")
    val kdDosen: String?,
    @SerializedName("nama_dosen")
    val namaDosen: String?
) : Parcelable