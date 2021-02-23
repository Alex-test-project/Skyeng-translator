package ru.example.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.example.database.dao.TextDao
import ru.example.database.dao.TranslationDao
import ru.example.database.models.TextDb
import ru.example.database.models.TranslationDb
import javax.inject.Inject
import javax.inject.Singleton

private const val DATABASE_NAME = "translation-db"

@Singleton
@Database(
	entities = [
		TextDb::class,
		TranslationDb::class
	], version = 1
)
abstract class AppDataBase @Inject constructor() : RoomDatabase() {

	abstract fun textDao(): TextDao
	abstract fun translationDao(): TranslationDao

	companion object {
		private var instance: AppDataBase? = null
		fun getInstance(context: Context): AppDataBase {
			return instance ?: synchronized(this) {
				instance ?: Room.databaseBuilder(context, AppDataBase::class.java, DATABASE_NAME)
					.build()
					.also { instance = it }
			}
		}
	}
}