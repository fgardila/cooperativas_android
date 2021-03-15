package com.code93.linkcoop.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Transaction(
        @SerializedName("_bitmap")
        var bitmap: String? = "",
        @SerializedName("_message_code")
        var messageCode: String? = "",
        @SerializedName("_code")
        var code: String? = "",
        @SerializedName("_namet")
        var nameTransaction: String? = "",
        @SerializedName("_tag_request")
        var tagRequest: String = "",
        @SerializedName("_tag_reply")
        var tagReply: String = "",
        @SerializedName("_cost")
        var cost: String? = "",
        @SerializedName("_subservice")
        var subService: String? = "",
        var referencias: List<Referencias>? = ArrayList()
) : Parcelable { }
