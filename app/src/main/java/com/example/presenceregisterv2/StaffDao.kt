package com.example.presenceregisterv2

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface StaffDao {
    @Query("SELECT * FROM staff")
    fun getAllStaff(): Flow<List<Staff>>

    @Query("SELECT * FROM staff WHERE pin = :pin")
    suspend fun getByPin(pin: Int): Staff?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(staff: Staff)

    @Delete
    suspend fun delete(staff: Staff)
}