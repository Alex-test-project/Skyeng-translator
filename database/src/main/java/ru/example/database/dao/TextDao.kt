package ru.example.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import ru.example.database.TEXT
import ru.example.database.models.TextDb
import ru.example.database.models.TextWithTranslations

@Dao
interface TextDao : BaseDao<TextDb> {

	@Query("DELETE FROM $TEXT")
	fun clearTextCache()

	@Transaction
	fun updateData(data: List<TextDb>) {
		clearTextCache()
		insert(data)
	}

	@Transaction
	@Query("SELECT * FROM $TEXT where id=:id")
	fun getTextWithTranslations(id: Long): TextWithTranslations
}