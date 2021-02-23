package ru.example.database.models

import androidx.room.Embedded
import androidx.room.Relation

data class TextWithTranslations (
	@Embedded
	val textDb: TextDb,
	@Relation(
		parentColumn = "id",
		entityColumn = "text_id"
	)
	val translationsDb: List<TranslationDb>
)