package ru.example.translator.search.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ru.example.translator.search.data.model.MeaningsResp

interface Api {

    @GET("/api/public/v1/words/search")
    suspend fun fetchTranslations(
        @Query("search") search: String
    ): Response<List<MeaningsResp>>
}