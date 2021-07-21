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
import com.example.newsbreeze.adapter.OnSavedNewsClickListener
import com.example.newsbreeze.adapter.SavedFragmentRVAdapter
import com.example.newsbreeze.data.NewsArgs
import com.example.newsbreeze.data.db.SavedNewsEntity
import com.example.newsbreeze.databinding.FragmentSavedNewsBinding
import com.example.newsbreeze.ui.viewmodel.SavedNewsViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SavedNewsFragment : Fragment(), OnSavedNewsClickListener {

    var _binding: FragmentSavedNewsBinding? = null
    val binding get() = _binding!!

    val savedNewsViewModel: SavedNewsViewModel by viewModels()

    val savedNewsRVAdapter by lazy {
        SavedFragmentRVAdapter(savedNewsViewModel,this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedNewsBinding.inflate(inflater, container, false)
        binding.apply {
            backIv.setOnClickListener {
                findNavController().popBackStack(R.id.savedNewsFragment,true)
                findNavController().navigate(R.id.homeFragment)
            }
            savedRv.apply {
                setHasFixedSize(true)
                adapter = savedNewsRVAdapter
            }
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String?): Boolean {
                    newText?.let {
                        savedNewsViewModel.searchQuery.value = it
                    }
                    return true
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        savedNewsViewModel.searchQuery.value = it
                    }
                    return true
                }
            })
        }
        subscribeObservers()
        collectFlows()
        return binding.root
    }

    private fun collectFlows() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            savedNewsViewModel.savedNewsViewModelChannelFlow.collect {
                Snackbar.make(requireView(),it, Snackbar.LENGTH_LONG).setBackgroundTint(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorPrimary
                    )
                ).show()
            }
        }
    }

    fun subscribeObservers() {
        savedNewsViewModel.savedNews.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                println("Rushi : Empty")
                binding.notavailableIv.visibility = View.VISIBLE
                binding.notavailableTv.visibility = View.VISIBLE
                binding.searchView.visibility = View.INVISIBLE
                binding.savedCard.visibility = View.INVISIBLE
            } else {
                println("Rushi : Not Empty")
                binding.notavailableIv.visibility = View.INVISIBLE
                binding.notavailableTv.visibility = View.INVISIBLE
                binding.searchView.visibility = View.VISIBLE
                binding.savedCard.visibility = View.VISIBLE
                savedNewsRVAdapter.submitList(it)
            }
        }

        savedNewsViewModel.searchNewsLivedata.observe(viewLifecycleOwner){
            savedNewsRVAdapter.submitList(it)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onSavedNewsClick(savedNewsEntity: SavedNewsEntity) {
        //navigate
        val newsArgs = NewsArgs(savedNewsEntity.title,savedNewsEntity.description,savedNewsEntity.urlToImage,savedNewsEntity.content,savedNewsEntity.publishedAt,savedNewsEntity.author)
        val directions = SavedNewsFragmentDirections.actionSavedNewsFragmentToDetailNewsFragment(newsArgs)
        findNavController().navigate(directions)
    }
}