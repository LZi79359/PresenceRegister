package com.example.presenceregister

import androidx.room.*
import kotlinx.coroutines.flow.Flow

// functions handling the room database
@Dao
interface PersonDao {

    @Query("SELECT * FROM people WHERE date = :date")
    fun getPeopleForDate(date: String): Flow<List<Person>>

    @Insert
    suspend fun insert(person: Person)

    @Query("UPDATE people SET isInside = 0 WHERE idCard = :idCard AND date = :date")
    suspend fun markAsOut(idCard: String, date: String)

    @Query("DELETE FROM people WHERE date != :date")
    suspend fun deletePreviousDays(date: String)
}