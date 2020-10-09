package ua.andrii.andrushchenko.swapp.api

import ua.andrii.andrushchenko.swapp.data.Person

data class StarWarsApiResponse(
    val next: String?,
    val previous: String?,
    val results: List<Person>
) {
    fun hasMore() = next?.isNotEmpty() ?: false
}