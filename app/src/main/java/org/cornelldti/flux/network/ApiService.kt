package org.cornelldti.flux.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.cornelldti.flux.data.Facility
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

private val headerClient = OkHttpClient.Builder().addInterceptor(HeaderInterceptor())

@ExperimentalSerializationApi
private val retrofit =
    Retrofit.Builder().client(headerClient.build())
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(
            BASE_URL
        ).build()

interface ApiService {
    @GET(FACILITY_LIST)
    suspend fun getFacilityList(): List<Facility>

    @GET(FACILITY_INFO)
    suspend fun getFacilityInfo(@Query("id") id: String?): List<FacilityInfo>

    @GET(HOW_DENSE)
    suspend fun getHowDense(@Query("id") id: String?): List<HowDense>

    @GET(FACILITY_HOURS)
    suspend fun getFacilityHours(@Query("id") id: String): List<FacilityHourList>
}

object Api {
    @ExperimentalSerializationApi
    val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}