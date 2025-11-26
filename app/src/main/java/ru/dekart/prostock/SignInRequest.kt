package ru.dekart.prostock

import com.google.gson.annotations.SerializedName

data class SignInRequest(
    @SerializedName("Login")
    val login: String,
    @SerializedName("Password")
    val password: String
)