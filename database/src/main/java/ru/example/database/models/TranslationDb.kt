package ru.example.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.example.database.TRANSLATION

@Entity(tableName = TRANSLATION)
data class TranslationDb(
	@PrimaryKey
	val id: Long,
	@ColumnInfo(name = "text_id")
	val textId: Long,
	@ColumnInfo(name = "translation")
	val translation: String,
	@ColumnInfo(name = "image_url")
	val imageUrl: String,
	@ColumnInfo(name = "transcription")
	val transcription: String,
	@ColumnInfo(name = "sound_url")
	val soundUrl: String
)