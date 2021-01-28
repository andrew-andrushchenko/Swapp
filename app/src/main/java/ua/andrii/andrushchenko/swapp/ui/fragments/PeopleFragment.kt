package ua.andrii.andrushchenko.swapp.ui.fragments

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import retrofit2.HttpException
import ua.andrii.andrushchenko.swapp.R
import ua.andrii.andrushchenko.swapp.databinding.FragmentPeopleBinding
import ua.andrii.andrushchenko.swapp.model.Person
import ua.andrii.andrushchenko.swapp.ui.adapters.StarWarsLoadStateAdapter
import ua.andrii.andrushchenko.swapp.ui.adapters.StarWarsPeopleAdapter
import ua.andrii.andrushchenko.swapp.ui.viewmodels.PeopleViewModel
import java.io.IOException

@ExperimentalPagingApi
@AndroidEntryPoint
class PeopleFragment : Fragment(R.layout.fragment_people) {

    private val viewModel by viewModels<PeopleViewModel>()

    private var _binding: FragmentPeopleBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentPeopleBinding.bind(view)

        val adapter = StarWarsPeopleAdapter(object : StarWarsPeopleAdapter.OnItemClickListener {
            override fun onItemClick(person: Person) {
                val action = PeopleFragmentDirections.actionPeopleFragmentToDetailsFragment(person)
                findNavController().navigate(action)
            }
        })

        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
                header = StarWarsLoadStateAdapter { adapter.retry() },
                footer = StarWarsLoadStateAdapter { adapter.retry() }
            )

            //Add refresh listener
            swipeRefreshLayout.setOnRefreshListener {
                adapter.refresh()
            }
        }

        adapter.addLoadStateListener {
            binding.apply {
                swipeRefreshLayout.isRefreshing = it.refresh is LoadState.Loading
                if (it.source.refresh is LoadState.NotLoading &&
                    it.source.append.endOfPaginationReached &&
                    adapter.itemCount < 1
                ) {
                    textViewEmpty.isVisible = true
                    recyclerView.isVisible = false
                } else {
                    textViewEmpty.isVisible = false
                    recyclerView.isVisible = true
                }

                if (it.refresh is LoadState.Error) {
                    //Get error type
                    val message = when ((it.refresh as LoadState.Error).error) {
                        is IOException ->
                            resources.getString(R.string.error_no_internet)
                        is HttpException ->
                            resources.getString(R.string.error_server_not_respond)
                        else ->
                            resources.getString(R.string.error_unknown)
                    }
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        //Fetch data from viewModel to populate recyclerView
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.people.distinctUntilChanged().collectLatest {
                adapter.submitData(it)
            }
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_sw, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    binding.recyclerView.scrollToPosition(0)
                    viewModel.searchPeople(query)
                    searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    if (newText.isEmpty()) {
                        viewModel.searchPeople(newText)
                    }
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