package ua.andrii.andrushchenko.swapp.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import ua.andrii.andrushchenko.swapp.api.StarWarsApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StarWarsRepository @Inject constructor(private val starWarsApi: StarWarsApi) {

    fun getSearchResults(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 10,
                maxSize = 50,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { StarWarsPagingSource(starWarsApi, query) }
        ).liveData
}