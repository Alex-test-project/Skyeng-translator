package ru.example.translator.wordinfo.domain.interactor

import ru.example.translator.wordinfo.domain.WordInfoRepository
import ru.example.translator.wordinfo.domain.model.WordInfo
import javax.inject.Inject

interface WordInfoInteractor {
	suspend fun getWordInfo(textId: Long, translationId: Long): WordInfo
}

class WordInfoInteractorImpl @Inject constructor(
	private val repository: WordInfoRepository
) : WordInfoInteractor {
	override suspend fun getWordInfo(textId: Long, translationId: Long): WordInfo {
		return repository.getWordInfo(textId, translationId)
	}
}