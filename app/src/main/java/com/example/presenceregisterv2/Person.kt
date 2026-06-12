package com.example.presenceregisterv2

import androidx.room.Entity
import androidx.room.PrimaryKey

// person class used across the system
@Entity(tableName = "people")
data class Person(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val surname: String,
    val idCard: String,
    val isInside: Boolean = true,
    val mobileNumber: String,
    val isStaff: Boolean,
    val date: String  // stored as "YYYY-MM-DD"
)