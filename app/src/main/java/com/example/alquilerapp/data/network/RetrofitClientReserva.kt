package com.example.alquilerapp.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClientReserva {
    private const val BASE = "http://10.0.2.2:8080/"

    val reservaApi: ReservaApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ReservaApi::class.java)
    }

}