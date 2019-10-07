package com.example.achar.webService.models.sucsess

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StateModel(
    @SerializedName("state_id")
    val stateId: Int,
    @SerializedName("state_name")
    val stateName: String
) : Parcelable