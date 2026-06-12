package com.example.presenceregisterv2

import android.content.Context
import androidx.room.*

// initialisation of database
@Database(entities = [Person::class, Staff::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun personDao(): PersonDao
    abstract fun staffDao(): StaffDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        // builds the database for the first time, or returns the existing one
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "presence_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}