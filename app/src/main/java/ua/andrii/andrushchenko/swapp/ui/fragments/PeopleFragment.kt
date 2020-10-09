package ua.andrii.andrushchenko.swapp.ui.fragments

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import dagger.hilt.android.AndroidEntryPoint
import ua.andrii.andrushchenko.swapp.R
import ua.andrii.andrushchenko.swapp.data.Person
import ua.andrii.andrushchenko.swapp.databinding.FragmentPeopleBinding
import ua.andrii.andrushchenko.swapp.ui.adapters.StarWarsLoadStateAdapter
import ua.andrii.andrushchenko.swapp.ui.adapters.StarWarsPeopleAdapter
import ua.andrii.andrushchenko.swapp.ui.viewmodels.StarWarsPeopleViewModel

@AndroidEntryPoint
class PeopleFragment : Fragment(R.layout.fragment_people),
    StarWarsPeopleAdapter.OnItemClickListener {

    private val viewModel by viewModels<StarWarsPeopleViewModel>()

    private var _binding: FragmentPeopleBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentPeopleBinding.bind(view)

        val adapter = StarWarsPeopleAdapter(this)

        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.itemAnimator = null
            recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
                header = StarWarsLoadStateAdapter { adapter.retry() },
                footer = StarWarsLoadStateAdapter { adapter.retry() }
            )
            buttonRetry.setOnClickListener { adapter.retry() }
        }

        viewModel.people.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        adapter.addLoadStateListener { loadState ->
            binding.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
                buttonRetry.isVisible = loadState.source.refresh is LoadState.Error
                textViewError.isVisible = loadState.source.refresh is LoadState.Error

                // empty view
                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    adapter.itemCount < 1
                ) {
                    recyclerView.isVisible = false
                    textViewEmpty.isVisible = true
                } else {
                    textViewEmpty.isVisible = false
                }
            }
        }

        setHasOptionsMenu(true)
    }

    override fun onItemClick(person: Person) {

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_sw, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                /*if (query != null) {
                    binding.recyclerView.scrollToPosition(0)
                    viewModel.searchPeople(query)
                    searchView.clearFocus()
                }*/
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    binding.recyclerView.scrollToPosition(0)
                    viewModel.searchPeople(newText)
                }
                return true
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}