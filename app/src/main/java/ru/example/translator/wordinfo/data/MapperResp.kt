package ru.example.translator.wordinfo.data

import ru.example.database.models.TextWithTranslations
import ru.example.translator.wordinfo.domain.model.WordInfo
import javax.inject.Inject

class MapperResp @Inject constructor() {
	fun mapToWordInfo(text: TextWithTranslations, translationId: Long): WordInfo {
		return text.translationsDb.find { it.id == translationId }.let { translationDb ->
			WordInfo(
				text = text.textDb.text,
				transcription = translationDb?.transcription.orEmpty(),
				translation = translationDb?.translation.orEmpty(),
				soundUrl = translationDb?.soundUrl.orEmpty(),
				imageUrl = translationDb?.imageUrl.orEmpty()
			)
		}
	}
}