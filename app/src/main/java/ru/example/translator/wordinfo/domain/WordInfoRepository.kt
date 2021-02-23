package ru.example.translator.wordinfo.domain

import ru.example.translator.wordinfo.domain.model.WordInfo

interface WordInfoRepository {
	suspend fun getWordInfo(textId: Long, translationId: Long): WordInfo
}