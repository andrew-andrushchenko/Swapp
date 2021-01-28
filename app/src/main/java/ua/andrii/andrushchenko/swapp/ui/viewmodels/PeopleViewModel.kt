package ua.andrii.andrushchenko.swapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.cachedIn
import androidx.paging.filter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import ua.andrii.andrushchenko.swapp.source.StarWarsRepository
import java.util.*
import javax.inject.Inject

@ExperimentalPagingApi
@HiltViewModel
class PeopleViewModel @Inject constructor(
    repository: StarWarsRepository/*,
    private val savedStateHandle: SavedStateHandle*/
) : ViewModel() {

    private val queryFlow = MutableStateFlow("")

    val people = repository.getPeople().cachedIn(viewModelScope)
        .combine(queryFlow) { pagingData, query ->
            pagingData.filter {
                it.name.toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT))
            }
        }
        .cachedIn(viewModelScope)

    fun searchPeople(name: String) {
        queryFlow.value = name
    }
}