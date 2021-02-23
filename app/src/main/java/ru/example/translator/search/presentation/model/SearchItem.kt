package ru.example.translator.search.presentation.model

sealed class SearchItem {

    abstract val id: Long

    data class SearchItemTranslationWithText(
        override val id: Long,
        val textId: Long,
        val text: String,
        val translation: String,
        val previewUrl: String,
    ) : SearchItem()

    data class SearchItemTranslation(
        override val id: Long,
        val textId: Long,
        val translation: String,
        val previewUrl: String
    ) : SearchItem()

    data class SearchItemClosedCombine(
        override val id: Long,
        val text: String,
        val translation: String,
        val wordsNumber: Int,
        val translationItems: List<SearchItemTranslation>
    ) : SearchItem()

    data class SearchItemOpenCombine(
        override val id: Long,
        val text: String,
        val translation: String,
        val wordsNumber: Int,
        val translationItems: List<SearchItemTranslation>
    ): SearchItem()
}