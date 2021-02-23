package ru.example.database.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

interface BaseDao<T> {

    @Insert
    fun insert(obj: T): Long

    @Insert
    fun insert(vararg obj: T): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(obj: List<T>): List<Long>

    @Update
    fun update(obj: T)

    @Delete
    fun delete(obj: T)


}