package com.aureus.budget.data.dao

import androidx.room.*
import com.aureus.budget.data.entity.BudgetGoal
import kotlinx.coroutines.flow.Flow

@Dao
abstract class BudgetGoalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertGoal(goal: BudgetGoal): Long

    @Update
    abstract suspend fun updateGoal(goal: BudgetGoal)

    @Delete
    abstract suspend fun deleteGoal(goal: BudgetGoal)

    @Query("""
        SELECT * FROM budget_goals 
        WHERE userId = :userId AND month = :month AND year = :year 
        LIMIT 1
    """)
    abstract suspend fun getGoalForMonth(userId: Long, month: Int, year: Int): BudgetGoal?

    @Query("""
        SELECT * FROM budget_goals 
        WHERE userId = :userId AND month = :month AND year = :year 
        LIMIT 1
    """)
    abstract fun getGoalForMonthLive(userId: Long, month: Int, year: Int): Flow<BudgetGoal?>

    @Query("SELECT * FROM budget_goals WHERE userId = :userId ORDER BY year DESC, month DESC")
    abstract fun getAllGoalsForUser(userId: Long): Flow<List<BudgetGoal>>

    // Upsert: insert or update goal for a specific month/year
    @Transaction
    open suspend fun upsertGoal(goal: BudgetGoal) {
        val existing = getGoalForMonth(goal.userId, goal.month, goal.year)
        if (existing == null) {
            insertGoal(goal)
        } else {
            updateGoal(goal.copy(id = existing.id))
        }
    }
}
