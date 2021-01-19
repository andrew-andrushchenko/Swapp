package ua.andrii.andrushchenko.swapp.source

import androidx.paging.PagingSource
import retrofit2.HttpException
import ua.andrii.andrushchenko.swapp.source.remote.StarWarsApi
import ua.andrii.andrushchenko.swapp.source.remote.StarWarsApiResponse
import ua.andrii.andrushchenko.swapp.model.Person
import java.io.IOException

private const val SW_STARTING_PAGE_INDEX = 1

class StarWarsPagingSource(
    private val starWarsApi: StarWarsApi,
    private val query: String
) : PagingSource<Int, Person>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Person> {
        val position = params.key ?: SW_STARTING_PAGE_INDEX

        return try {
            val response: StarWarsApiResponse
            val people: List<Person>
            if (query.isEmpty()) {
                response = starWarsApi.getPeopleByPage(position)
                people = response.results

                LoadResult.Page(
                    data = people,
                    prevKey = if (position == SW_STARTING_PAGE_INDEX) null else position - 1,
                    nextKey = if (response.hasMore()) position + 1 else null
                )
            } else {
                response = starWarsApi.searchPeopleByName(query)
                people = response.results

                LoadResult.Page(
                    data = people,
                    prevKey = null,
                    nextKey = null
                )
            }
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }
}