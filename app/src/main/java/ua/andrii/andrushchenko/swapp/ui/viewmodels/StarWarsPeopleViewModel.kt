package ua.andrii.andrushchenko.swapp.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import ua.andrii.andrushchenko.swapp.data.StarWarsRepository

class StarWarsPeopleViewModel @ViewModelInject constructor(
    private val repository: StarWarsRepository
) : ViewModel() {

    private val currentQuery = MutableLiveData(DEFAULT_QUERY)

    val people = currentQuery.switchMap { queryString ->
        repository.getSearchResults(queryString).cachedIn(viewModelScope)
    }

    fun searchPeople(query: String) {
        currentQuery.value = query
    }

    companion object {
        private const val DEFAULT_QUERY = ""
    }

}