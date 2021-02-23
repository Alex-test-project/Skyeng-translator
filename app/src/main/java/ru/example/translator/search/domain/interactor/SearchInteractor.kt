package ru.example.translator.search.domain.interactor

import ru.example.translator.search.domain.SearchRepository
import ru.example.translator.search.domain.model.Text
import javax.inject.Inject

interface SearchInteractor {
    var lastQuery: String
    suspend fun fetchMeanings(query: String): List<Text>
}

class SearchInteractorImpl @Inject constructor(
    private val searchRepository: SearchRepository
) : SearchInteractor {

    override var lastQuery = ""

    override suspend fun fetchMeanings(query: String): List<Text> {
        lastQuery = query
        return searchRepository.fetchMeanings(query)
    }
}