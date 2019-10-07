package com.example.achar.webService.models.sucsess

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RegionModel(
    @SerializedName("city_object")
    val city: CityModel,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("state_object")
    val state: StateModel
) : Parcelable