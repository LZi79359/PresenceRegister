package com.example.presenceregisterv2

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "staff")
data class Staff(
    @PrimaryKey val pin: Int = 0,
    val name: String,
    val surname: String,
    val mobileNumber: String
)