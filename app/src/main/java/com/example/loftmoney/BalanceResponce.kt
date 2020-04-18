package com.example.loftmoney

import com.google.gson.annotations.SerializedName

class BalanceResponce {
    var status: String? = null

    @SerializedName("total_expenses")
    var totalExpences = 0f

    @SerializedName("total_income")
    var totalIncome = 0f

}