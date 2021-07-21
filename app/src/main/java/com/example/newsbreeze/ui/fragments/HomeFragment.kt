package com.example.newsbreeze.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.newsbreeze.R
import com.example.newsbreeze.adapter.HomeFragmentRVAdapter
import com.example.newsbreeze.adapter.HomeRVClickListener
import com.example.newsbreeze.data.NewsArgs
import com.example.newsbreeze.data.db.BreakingNewsEntity
import com.example.newsbreeze.data.db.SavedNewsEntity
import com.example.newsbreeze.databinding.FragmentHomeBinding
import com.example.newsbreeze.ui.MainActivity
import com.example.newsbreeze.ui.viewmodel.HomeViewModel
import com.example.newsbreeze.utils.NetworkResult
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class HomeFragment : Fragment(), HomeRVClickListener {

    var _binding: FragmentHomeBinding? = null
    val binding: FragmentHomeBinding get() = _binding!!

    val homeViewModel: HomeViewModel by viewModels()
    lateinit var homeFragmentRVAdapter: HomeFragmentRVAdapter

    var savedNews = emptyList<SavedNewsEntity>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        homeFragmentRVAdapter = HomeFragmentRVAdapter(this)
        binding.apply {
            homeRv.apply {
                setHasFixedSize(true)
                adapter = homeFragmentRVAdapter
            }
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String?): Boolean {
                    newText?.let {
                        homeViewModel.searchQuery.value = it
                    }
                    return true
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        homeViewModel.searchQuery.value = it
                    }
                    return true
                }
            })
            savedIv.setOnClickListener {
                findNavController().popBackStack(R.id.homeFragment, true)
                findNavController().navigate(R.id.savedNewsFragment)
            }
        }
        subscribeObservers()
        collectFlows()
        if (!MainActivity.isPrimaryAPICallDone) {
            homeViewModel.getBreakingNews()
        }
        return binding.root
    }

    private fun subscribeObservers() {
        homeViewModel.resultBreakingNews.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    println("Rushi : Success")
                    //invisible loading
                    binding.lottieLoading.visibility = View.INVISIBLE
                    //send data to adapter
                    val list = arrayListOf<BreakingNewsEntity>()
                    it.data?.articles?.forEach {
                        val breakingNews =
                            BreakingNewsEntity(
                                title = it.title ?: "Title N/A",
                                description = it.description ?: "Description N/A",
                                urlToImage = it.urlToImage ?: "",
                                content = it.content ?: "Content N/A",
                                publishedAt = it.publishedAt ?: "Date N/A",
                                author = it.author ?: "Author N/A"
                            )
                        list.add(breakingNews)
                        println("Rushi : List Add : ${breakingNews.author}")
                    }
                    if (list.isNotEmpty()) {
                        homeViewModel.clearBreakingNewsTableAndAddThisList(list)
                    }
                }
                is NetworkResult.Loading -> {
                    println("Rushi : Loading")
                    //visible loading
                    binding.lottieLoading.visibility = View.VISIBLE
                }
                is NetworkResult.Error -> {
                    println("Rushi : Error")
                    //invisible loading
                    binding.lottieLoading.visibility = View.INVISIBLE
                    Snackbar.make(
                        requireView(),
                        it.msg ?: "Network Error",
                        Snackbar.LENGTH_INDEFINITE
                    ).setDuration(7000)
                        .setAction("Try again") {
                            homeViewModel.getBreakingNews()
                        }
                        .show()
                }
            }
        }

        homeViewModel.breakingNewsFromDB.observe(viewLifecycleOwner) {
            println("Rushi : ObserveList : ${it.size}")
            homeFragmentRVAdapter.submitList(it)
            if (it.isEmpty()) {
                binding.searchView.visibility = View.GONE
            } else {
                binding.searchView.visibility = View.VISIBLE
            }
        }

        homeViewModel.searchNewsLiveData.observe(viewLifecycleOwner) {
            homeFragmentRVAdapter.submitList(it)
        }

        homeViewModel.savedNewsFromDB.observe(viewLifecycleOwner) {
            savedNews = it
        }
    }

    private fun collectFlows() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            homeViewModel.homeViewModelChannelFlow.collect {
                Snackbar.make(requireView(), it, Snackbar.LENGTH_LONG).setBackgroundTint(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorPrimary
                    )
                ).show()
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onReadClick(entity: BreakingNewsEntity) {
        val newsArgs = NewsArgs(
            entity.title,
            entity.description,
            entity.urlToImage,
            entity.content,
            entity.publishedAt,
            entity.author
        )
        val directions = HomeFragmentDirections.actionHomeFragmentToDetailNewsFragment(newsArgs)
        findNavController().navigate(directions)
    }

    override fun onSaveClick(entity: BreakingNewsEntity) {
        var contains = false
        for (news in savedNews) {
            if (news.title == entity.title) {
                contains = true
            }
        }
        if (contains) {
            Snackbar.make(
                requireView(),
                "Already Saved",
                Snackbar.LENGTH_INDEFINITE
            )
                .setBackgroundTint(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.redColor
                    )
                )
                .setDuration(3000)
                .show()
        } else {
            homeViewModel.addToSavedNewsTable(entity)
        }
    }
}