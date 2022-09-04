package com.example.nearbyplaces.models

import com.example.nearbyplaces.models.Article

data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)