package com.example.newsbreeze.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.newsbreeze.R
import com.example.newsbreeze.data.db.SavedNewsEntity
import com.example.newsbreeze.databinding.FragmentDetailBinding
import com.example.newsbreeze.ui.viewmodel.DetailNewsViewModel
import com.example.newsbreeze.utils.Utils
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class DetailNewsFragment : Fragment() {

    val detailNewsViewModel: DetailNewsViewModel by viewModels()

    var _binding: FragmentDetailBinding? = null
    val binding: FragmentDetailBinding get() = _binding!!

    var savedNews = emptyList<SavedNewsEntity>()

    val args: DetailNewsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        binding.apply {
            titleTv.text = args.newsArgs.title
            var dateToShow = args.newsArgs.date
            if (dateToShow != "Date N/A") {
                dateToShow =
                    Utils.convertISOTimeToDate(dateToShow).toString()
            }
            dateTv.text = dateToShow
            contentTv.text = args.newsArgs.content
            authorTv.text = args.newsArgs.author
            println("Rushi : URL : ${args.newsArgs.imgURL}")
            Glide.with(binding.root.context).load(args.newsArgs.imgURL)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(coverIv)

            backIv.setOnClickListener {
                findNavController().popBackStack(R.id.detailNewsFragment,true)
                findNavController().navigate(R.id.homeFragment)
            }

            saveBt.setOnClickListener {
                val entity = SavedNewsEntity(args.newsArgs.title,args.newsArgs.description,args.newsArgs.imgURL,args.newsArgs.content,args.newsArgs.date,args.newsArgs.author)
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
                    detailNewsViewModel.addToSavedNewsTable(entity)
                }
            }
        }
        subscribeObservers()
        collectFlow()
        return binding.root
    }

    fun subscribeObservers() {
        detailNewsViewModel.savedNewsLiveData.observe(viewLifecycleOwner) {
            savedNews = it
        }
    }

    fun collectFlow() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            detailNewsViewModel.detailNewsViewModelChannelFlow.collect {
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
}