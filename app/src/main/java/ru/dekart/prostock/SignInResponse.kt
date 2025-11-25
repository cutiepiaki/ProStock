package ru.dekart.prostock

import com.google.gson.annotations.SerializedName
import java.util.UUID

data class SignInResponse(
    @SerializedName("Success")
    val success: Boolean,
    @SerializedName("Message")
    val message: String,
    @SerializedName("FullName")
    val fullName: String,
    @SerializedName("Token")
    val token: UUID
)