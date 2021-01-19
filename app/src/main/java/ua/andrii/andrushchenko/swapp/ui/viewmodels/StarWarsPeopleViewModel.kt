package ua.andrii.andrushchenko.swapp.ui.viewmodels

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.cachedIn
import ua.andrii.andrushchenko.swapp.source.StarWarsRepository

@ExperimentalPagingApi
class StarWarsPeopleViewModel @ViewModelInject constructor(
    private val repository: StarWarsRepository,
    @Assisted savedStateHandle: SavedStateHandle
) : ViewModel() {
    val people = repository.getPeople().cachedIn(viewModelScope)
}