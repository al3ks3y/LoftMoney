package com.example.loftmoney

import com.google.gson.annotations.SerializedName

class Status {
    var status: String? = null
    var id = 0

    @SerializedName("auth_token")
    var token: String? = null

}