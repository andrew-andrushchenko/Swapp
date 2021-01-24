package ua.andrii.andrushchenko.swapp.source

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import retrofit2.HttpException
import ua.andrii.andrushchenko.swapp.model.Person
import ua.andrii.andrushchenko.swapp.model.RemoteKey
import ua.andrii.andrushchenko.swapp.source.local.StarWarsDb
import ua.andrii.andrushchenko.swapp.source.remote.StarWarsApi
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
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getClosestRemoteKey(state)
                    remoteKeys?.prevKey ?: DEFAULT_PAGE_INDEX
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getFirstRemoteKey(state)
                        ?: throw InvalidObjectException("Invalid state, key should not be null")

                    if (remoteKeys.prevKey == null) {
                        //end of list condition reached
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }

                    remoteKeys.prevKey
                }
                LoadType.APPEND -> {
                    val remoteKeys = getLastRemoteKey(state)
                        ?: throw InvalidObjectException("Invalid state, key should not be null")

                    if (remoteKeys.nextKey == null) {
                        //end of list condition reached
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }

                    remoteKeys.nextKey
                }
            }

            val response = starWarsApi.getPeopleByPage(loadKey)

            starWarsDb.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeyDao.clearAll()
                    peopleDao.deleteAll()
                }

                /*Since 'next' and 'previous' properties type is String?
                * and information about next and previous pages placed at the
                * end of the corresponding strings
                * we should take last chars until they are digits*/
                val prevKey = response.previous?.takeLastWhile { it.isDigit() }?.toInt()
                val nextKey = response.next?.takeLastWhile { it.isDigit() }?.toInt()

                val keys = response.results.map {
                    RemoteKey(label = it.url, prevKey = prevKey, nextKey = nextKey)
                }

                remoteKeyDao.insertAll(keys)
                peopleDao.insertAll(response.results)
            }

            //If there are no more results then end of list condition reached
            //if there are some items then end of list is not reached
            MediatorResult.Success(endOfPaginationReached = response.next.isNullOrBlank())

        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, Person>): RemoteKey? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { person -> remoteKeyDao.remoteKeyByLabel(person.url) }
        //return state.lastItemOrNull()?.let { person -> remoteKeyDao.remoteKeyByLabel(person.url) }
    }

    /**
     * get the first remote key inserted which had the data
     */
    private suspend fun getFirstRemoteKey(state: PagingState<Int, Person>): RemoteKey? {
        return state.pages
            .firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { person -> remoteKeyDao.remoteKeyByLabel(person.url) }
        //return state.firstItemOrNull()?.let { person -> remoteKeyDao.remoteKeyByLabel(person.url) }
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