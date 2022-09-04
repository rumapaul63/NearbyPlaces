package com.example.nearbyplaces.ui.fragments


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

import com.example.nearbyplaces.R
import com.example.nearbyplaces.ui.NewsActivity
import com.example.nearbyplaces.ui.NewsViewModel

class SavedNewsFragment : Fragment(R.layout.fragment_saved_news) {

    lateinit var viewModel: NewsViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel

    }
}