package com.example.nearbyplaces.repository

import com.example.nearbyplaces.api.RetrofitInstance
import com.example.nearbyplaces.db.ArticleDatabase

class NewsRepository (
    val db:ArticleDatabase
        ){
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQuery, pageNumber)


}