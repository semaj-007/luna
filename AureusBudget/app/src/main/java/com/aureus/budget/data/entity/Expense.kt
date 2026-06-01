package com.aureus.budget.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "expenses",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("userId"), Index("categoryId")]
)
data class Expense(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val categoryId: Long?,
    val amount: Double,
    val description: String,
    val date: String,          // yyyy-MM-dd
    val startTime: String,     // HH:mm
    val endTime: String,       // HH:mm
    val notes: String = "",
    val photoPath: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
