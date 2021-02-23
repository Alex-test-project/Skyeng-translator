package ru.example.translator.search.domain.model

class Text(
    val id: Long,
    val text: String,
    val translations: List<Translation>
)

class Translation(
    val id: Long,
    val partOfSpeechCode: String,
    val previewUrl: String,
    val imageUrl: String,
    val transcription: String,
    val soundUrl: String,
    val translation: String,
    val note: String
)