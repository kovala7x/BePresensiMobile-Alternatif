package com.trateg.bepresensimobile.model


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("jadwal")
    val jadwal: Jadwal?,
    @SerializedName("auth")
    val auth: Auth?,
    @SerializedName("rekapitulasi")
    val rekapitulasi: Rekapitulasi?
)

data class ListData(
    @SerializedName("jadwal")
    val jadwal: List<Jadwal>?

)