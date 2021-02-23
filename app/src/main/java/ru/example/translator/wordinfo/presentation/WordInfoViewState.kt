package ru.example.translator.wordinfo.presentation

import ru.example.translator.wordinfo.domain.model.WordInfo

sealed class WordInfoViewState {
	object ErrorState : WordInfoViewState()
	data class DataState(val info: WordInfo): WordInfoViewState()
}