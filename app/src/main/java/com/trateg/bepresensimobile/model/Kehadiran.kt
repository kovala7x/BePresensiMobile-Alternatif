package com.trateg.bepresensimobile.model


import com.google.gson.annotations.SerializedName

data class Kehadiran(
    @SerializedName("hadir")
    val hadir: String?
)