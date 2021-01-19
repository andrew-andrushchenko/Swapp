package ua.andrii.andrushchenko.swapp.source

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import ua.andrii.andrushchenko.swapp.source.remote.StarWarsApi
import ua.andrii.andrushchenko.swapp.source.local.StarWarsDb
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
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { peopleDao.getPeople() },
            remoteMediator = StarWarsRemoteMediator(starWarsDb, starWarsApi)
        ).liveData
}