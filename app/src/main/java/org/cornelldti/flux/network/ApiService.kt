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
    suspend fun getFacilityInfo(@Query("id") id: String? = null): List<FacilityInfo>

    @GET(HOW_DENSE)
    suspend fun getHowDense(@Query("id") id: String? = null): List<HowDense>

    @GET(FACILITY_HOURS)
    suspend fun getFacilityHours(
        @Query("id") id: String,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String,
    ): List<FacilityHourList>
    
    @GET(MENU_DATA)
    suspend fun getMenuData(
        @Query("id") id: String,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String? = null,
        @Query("q") query: String? = null
    ): List<MenuData>
}

object Api {
    @ExperimentalSerializationApi
    val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}