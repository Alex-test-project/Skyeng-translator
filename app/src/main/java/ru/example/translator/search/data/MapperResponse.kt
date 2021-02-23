package ru.example.translator.search.data

import ru.example.database.models.TextDb
import ru.example.database.models.TranslationDb
import ru.example.translator.search.data.model.MeaningResp
import ru.example.translator.search.data.model.MeaningsResp
import ru.example.translator.search.domain.model.Translation
import ru.example.translator.search.domain.model.Text
import javax.inject.Inject

class MapperResponse @Inject constructor() {
	fun mapToDomainModel(meaningsResp: List<MeaningsResp>?): List<Text> {
		return meaningsResp?.map {
			Text(
				id = it.id ?: 0,
				text = it.text.orEmpty(),
				translations = getTranslations(it.meanings)
			)
		} ?: emptyList()
	}

	fun mapMeaningsTextToDbModel(meanings: List<Text>): List<TextDb> {
		return meanings.map {
			TextDb(
				id = it.id,
				text = it.text
			)
		}
	}

	fun mapMeaningToTranslationDbModel(meanings: List<Text>): List<TranslationDb> {
		return mutableListOf<TranslationDb>()
			.also { translations ->
				meanings.forEach { text ->
					text.translations.forEach { translation ->
						translations.add(
							TranslationDb(
								id = translation.id,
								textId = text.id,
								translation = translation.translation,
								transcription = translation.transcription,
								soundUrl = "https:${translation.soundUrl}",
								imageUrl = translation.imageUrl
							)
						)
					}
				}
			}
	}

	private fun getTranslations(meanings: List<MeaningResp>?): List<Translation> {
		return meanings?.map {
			Translation(
				id = it.id ?: 0,
				partOfSpeechCode = it.partOfSpeechCode.orEmpty(),
				previewUrl = "https:${it.previewUrl.orEmpty()}",
				imageUrl = "https:${it.imageUrl.orEmpty()}",
				translation = it.translation?.text.orEmpty(),
				note = it.translation?.note.orEmpty(),
				transcription = it.transcription.orEmpty(),
				soundUrl = it.soundUrl.orEmpty()
			)
		} ?: emptyList()
	}
}