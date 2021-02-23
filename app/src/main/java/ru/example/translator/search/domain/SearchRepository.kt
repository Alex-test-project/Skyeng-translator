package ru.example.translator.search.domain

import ru.example.translator.search.domain.model.Text

interface SearchRepository {
    suspend fun fetchMeanings(query: String):List<Text>
}