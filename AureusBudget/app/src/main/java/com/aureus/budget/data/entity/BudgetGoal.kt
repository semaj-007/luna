package com.aureus.budget.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "budget_goals",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("userId")]
)
data class BudgetGoal(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val minGoal: Double,    // minimum monthly spending goal
    val maxGoal: Double,    // maximum monthly spending goal
    val month: Int,         // 1-12
    val year: Int,
    val updatedAt: Long = System.currentTimeMillis()
)
