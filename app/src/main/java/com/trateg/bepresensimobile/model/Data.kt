package com.trateg.bepresensimobile.model


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("jadwal")
    val jadwal: List<Jadwal>?
)