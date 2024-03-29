package ua.andrii.andrushchenko.swapp.source.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface StarWarsApi {

    @GET("people/")
    suspend fun getPeopleByPage(@Query("page") page: Int?): StarWarsApiResponse

    companion object {
        const val BASE_URL = "https://swapi.dev/api/"
        const val DEFAULT_PAGE_SIZE = 10
    }

}