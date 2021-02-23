package ru.example.database.databaseworker

import ru.example.database.dao.TextDao
import ru.example.database.dao.TranslationDao
import ru.example.database.models.TextDb
import ru.example.database.models.TextWithTranslations
import ru.example.database.models.TranslationDb
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

interface DataBaseWorker {
	suspend fun saveTexts(data: List<TextDb>)
	suspend fun saveTranslations(data: List<TranslationDb>)
	suspend fun getTextWithTranslations(textId: Long): TextWithTranslations
}

class DataBaseWorkerImpl @Inject constructor(
	private val textDao: TextDao,
	private val translationDao: TranslationDao
) : DataBaseWorker {

	override suspend fun saveTexts(data: List<TextDb>) {
		textDao.updateData(data)
	}

	override suspend fun saveTranslations(data: List<TranslationDb>) {
		translationDao.updateData(data)
	}

	override suspend fun getTextWithTranslations(textId: Long): TextWithTranslations {
		return textDao.getTextWithTranslations(textId)
	}

}