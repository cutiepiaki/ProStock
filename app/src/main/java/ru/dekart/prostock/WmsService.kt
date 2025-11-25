package ru.dekart.prostock

import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Body

interface WmsService {
    @POST("wms/signin")
    fun signIn(@Body signInRequest: SignInRequest) : Call<SignInResponse>
}