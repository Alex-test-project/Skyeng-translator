package ru.example.translator.wordinfo.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.example.database.databaseworker.DataBaseWorker
import ru.example.translator.wordinfo.domain.WordInfoRepository
import ru.example.translator.wordinfo.domain.model.WordInfo
import javax.inject.Inject

class WordInfoRepositoryImpl @Inject constructor(
	private val dataBaseWorker: DataBaseWorker,
	private val mapperResp: MapperResp
) : WordInfoRepository {
	override suspend fun getWordInfo(textId: Long, translationId: Long): WordInfo {
		return mapperResp.mapToWordInfo(
			withContext(Dispatchers.IO) {
				dataBaseWorker.getTextWithTranslations(textId)
			}, translationId
		)
	}
}