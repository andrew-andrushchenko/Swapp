package ua.andrii.andrushchenko.swapp.source

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import ua.andrii.andrushchenko.swapp.source.local.StarWarsDb
import ua.andrii.andrushchenko.swapp.source.remote.StarWarsApi
import ua.andrii.andrushchenko.swapp.source.remote.StarWarsApi.Companion.DEFAULT_PAGE_SIZE
import javax.inject.Inject
import javax.inject.Singleton

@ExperimentalPagingApi
@Singleton
class StarWarsRepository @Inject constructor(
    private val starWarsApi: StarWarsApi,
    private val starWarsDb: StarWarsDb
) {
    private val peopleDao = starWarsDb.peopleDao()

    fun getPeople() =
        Pager(
            config = PagingConfig(
                pageSize = DEFAULT_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { peopleDao.peoplePagingSource() },
            remoteMediator = StarWarsRemoteMediator(starWarsDb, starWarsApi)
        ).flow
}