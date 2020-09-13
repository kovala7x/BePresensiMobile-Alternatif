package com.trateg.bepresensimobile.model


import com.google.gson.annotations.SerializedName

data class Status(
    @SerializedName("kd_status_rekapitulasi")
    val kdStatusRekapitulasi: String?,
    @SerializedName("keterangan_rekapitulasi")
    val keteranganRekapitulasi: String?
)