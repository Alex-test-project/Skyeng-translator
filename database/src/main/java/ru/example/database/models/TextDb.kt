package ru.example.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.example.database.TEXT

@Entity(tableName = TEXT)
data class TextDb(
	@PrimaryKey
	val id: Long,
	@ColumnInfo(name = "text")
	val text: String
)