
package com.example.nearbyplaces.ui


import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.nearbyplaces.R
import com.example.nearbyplaces.db.ArticleDatabase
import com.example.nearbyplaces.repository.NewsRepository
import com.google.android.material.bottomnavigation.BottomNavigationView;

import kotlinx.android.synthetic.main.activity_news.*

class NewsActivity : AppCompatActivity() {

    lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)


        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val navController = findNavController(R.id.bottomNavigationView)

        bottomNavigationView.setupWithNavController(navController)
    }
}