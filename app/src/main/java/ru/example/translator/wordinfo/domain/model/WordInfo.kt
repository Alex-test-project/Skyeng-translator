package ru.example.translator.wordinfo.domain.model

data class WordInfo(
	val text: String,
	val transcription: String,
	val translation: String,
	val imageUrl: String,
	val soundUrl: String
)