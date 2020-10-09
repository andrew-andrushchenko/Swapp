package ua.andrii.andrushchenko.swapp.api

import retrofit2.http.GET
import retrofit2.http.Query

interface StarWarsApi {

    @GET("people/")
    suspend fun getPeopleByPage(@Query("page") page: Int): StarWarsApiResponse

    @GET("people/")
    suspend fun getPeopleByName(@Query("search") name: String): StarWarsApiResponse

    companion object {
        const val BASE_URL = "https://swapi.dev/api/"
    }

}