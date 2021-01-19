package ua.andrii.andrushchenko.swapp.source.remote

import ua.andrii.andrushchenko.swapp.model.Person

data class StarWarsApiResponse(
    val next: String?,
    val previous: String?,
    val results: List<Person>
) {
    fun hasMore() = next?.isNotEmpty() ?: false
}