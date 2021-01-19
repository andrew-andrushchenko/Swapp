package ua.andrii.andrushchenko.swapp.source

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import retrofit2.HttpException
import ua.andrii.andrushchenko.swapp.source.remote.StarWarsApi
import ua.andrii.andrushchenko.swapp.source.local.StarWarsDb
import ua.andrii.andrushchenko.swapp.model.Person
import ua.andrii.andrushchenko.swapp.model.RemoteKey
import java.io.IOException
import java.io.InvalidObjectException

@ExperimentalPagingApi
class StarWarsRemoteMediator(
    private val starWarsDb: StarWarsDb,
    private val starWarsApi: StarWarsApi
) : RemoteMediator<Int, Person>() {

    private val peopleDao = starWarsDb.peopleDao()
    private val remoteKeyDao = starWarsDb.remoteKeyDao()

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Person>): MediatorResult {

        val page = when (val pageKeyData = getPageKeyData(loadType, state)) {
            is MediatorResult.Success -> {
                return pageKeyData
            }
            else -> {
                pageKeyData as Int
            }
        }

        return try {
            val response = starWarsApi.getPeopleByPage(page)
            val isEndOfPagination = response.results.isEmpty()

            starWarsDb.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeyDao.clearAll()
                    peopleDao.deleteAll()
                }
                val prevKey = if (page == DEFAULT_PAGE_INDEX) null else page - 1
                val nextKey = if (isEndOfPagination) null else page + 1

                val keys = response.results.map {
                    RemoteKey(label = it.url, prevKey = prevKey, nextKey = nextKey)
                }

                remoteKeyDao.insertAll(keys)
                peopleDao.insertAll(response.results)
            }

            MediatorResult.Success(endOfPaginationReached = isEndOfPagination)

        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getPageKeyData(loadType: LoadType, state: PagingState<Int, Person>): Any? {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKey = getClosestRemoteKey(state)
                remoteKey?.nextKey?.minus(1) ?: DEFAULT_PAGE_INDEX
            }
            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                    ?: throw InvalidObjectException("Remote key should not be null for $loadType")
                remoteKeys.nextKey

            }
            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                    ?: throw InvalidObjectException("Invalid state, key should not be null")
                //end of list condition reached
                remoteKeys.prevKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                remoteKeys.prevKey //TODO: review
            }
        }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, Person>): RemoteKey? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { person -> person.url.let { remoteKeyDao.remoteKeyByLabel(it) } }
    }

    /**
     * get the first remote key inserted which had the data
     */
    private suspend fun getFirstRemoteKey(state: PagingState<Int, Person>): RemoteKey? {
        return state.pages
            .firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { person -> person.url.let { remoteKeyDao.remoteKeyByLabel(it) } }
    }

    /**
     * get the closest remote key inserted which had the data
     */
    private suspend fun getClosestRemoteKey(state: PagingState<Int, Person>): RemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.url?.let { label ->
                remoteKeyDao.remoteKeyByLabel(label)
            }
        }
    }

    companion object {
        const val DEFAULT_PAGE_INDEX = 1
    }
}

/*val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true
                )
                LoadType.APPEND -> {
                    val remoteKey = starWarsDb.withTransaction {
                        remoteKeyDao.remoteKeyByLabel(label)
                    }

                    if (remoteKey.nextKey == null) {
                        return MediatorResult.Success(
                            endOfPaginationReached = true
                        )
                    }

                    remoteKey.nextKey
                }
            }

            val response = starWarsApi.getPeopleByPage(loadKey?.toInt())

            starWarsDb.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeyDao.deleteByLabel(label)
                    peopleDao.deleteByLabel(label)
                    //peopleDao.deleteAll()
                    //peopleDao.deleteByQuery(query)
                }

                remoteKeyDao.insertOrReplace(
                    RemoteKey(response.results.last().personNumber.toString(), response.next)
                )

                peopleDao.insertAll(response.results)
            }

            MediatorResult.Success(
                endOfPaginationReached = response.next == null
            )*/