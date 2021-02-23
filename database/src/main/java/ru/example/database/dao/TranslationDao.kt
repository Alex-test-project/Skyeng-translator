package ru.example.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import ru.example.database.TRANSLATION
import ru.example.database.models.TranslationDb

@Dao
interface TranslationDao : BaseDao<TranslationDb> {

	@Query("DELETE FROM $TRANSLATION")
	fun clearTextCache()

	@Transaction
	fun updateData(data: List<TranslationDb>) {
		clearTextCache()
		insert(data)
	}
}