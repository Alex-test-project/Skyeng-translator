package ru.example.translator.search.presentation

import ru.example.translator.search.presentation.model.SearchItem

sealed class SearchViewState {
	data class DataState(val items: List<SearchItem>) : SearchViewState()
	data class DialogState(
		val textId: Long,
		val translationId: Long,
		val items: List<SearchItem>
	) : SearchViewState()
	object LoadingState : SearchViewState()
	object ErrorState : SearchViewState()
	object EmptyDataState : SearchViewState()
	object StartScreenState : SearchViewState()
}