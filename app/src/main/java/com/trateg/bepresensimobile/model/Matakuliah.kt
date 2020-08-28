package com.trateg.bepresensimobile.model


import com.google.gson.annotations.SerializedName

data class Matakuliah(
    @SerializedName("kd_matakuliah")
    val kdMatakuliah: String?,
    @SerializedName("nama_matakuliah")
    val namaMatakuliah: String?
)