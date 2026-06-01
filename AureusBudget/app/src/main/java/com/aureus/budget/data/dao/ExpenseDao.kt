package com.aureus.budget.data.dao

import androidx.room.*
import com.aureus.budget.data.entity.Expense
import kotlinx.coroutines.flow.Flow

data class CategorySpending(
    val categoryId: Long?,
    val categoryName: String?,
    val totalAmount: Double,
    val expenseCount: Int
)

@Dao
abstract class ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertExpense(expense: Expense): Long

    @Update
    abstract suspend fun updateExpense(expense: Expense)

    @Delete
    abstract suspend fun deleteExpense(expense: Expense)

    @Query("SELECT * FROM expenses WHERE id = :id LIMIT 1")
    abstract suspend fun getExpenseById(id: Long): Expense?

    // All expenses for a user ordered by date desc
    @Query("""
        SELECT * FROM expenses 
        WHERE userId = :userId 
        ORDER BY date DESC, startTime DESC
    """)
    abstract fun getAllExpensesForUser(userId: Long): Flow<List<Expense>>

    // Expenses in a date range
    @Query("""
        SELECT * FROM expenses 
        WHERE userId = :userId AND date >= :startDate AND date <= :endDate
        ORDER BY date DESC, startTime DESC
    """)
    abstract fun getExpensesInRange(userId: Long, startDate: String, endDate: String): Flow<List<Expense>>

    // Expenses in a date range (sync)
    @Query("""
        SELECT * FROM expenses 
        WHERE userId = :userId AND date >= :startDate AND date <= :endDate
        ORDER BY date DESC, startTime DESC
    """)
    abstract suspend fun getExpensesInRangeSync(userId: Long, startDate: String, endDate: String): List<Expense>

    // Total spent in a date range
    @Query("""
        SELECT COALESCE(SUM(amount), 0) FROM expenses 
        WHERE userId = :userId AND date >= :startDate AND date <= :endDate
    """)
    abstract suspend fun getTotalSpentInRange(userId: Long, startDate: String, endDate: String): Double

    // Total for current month
    @Query("""
        SELECT COALESCE(SUM(amount), 0) FROM expenses 
        WHERE userId = :userId AND date LIKE :monthPrefix
    """)
    abstract fun getMonthlyTotal(userId: Long, monthPrefix: String): Flow<Double>

    // Recent expenses (last N)
    @Query("""
        SELECT * FROM expenses 
        WHERE userId = :userId 
        ORDER BY date DESC, startTime DESC 
        LIMIT :limit
    """)
    abstract fun getRecentExpenses(userId: Long, limit: Int): Flow<List<Expense>>

    // Category spending for period
    @Query("""
        SELECT 
            e.categoryId,
            c.name AS categoryName,
            SUM(e.amount) AS totalAmount,
            COUNT(e.id) AS expenseCount
        FROM expenses e
        LEFT JOIN categories c ON e.categoryId = c.id
        WHERE e.userId = :userId AND e.date >= :startDate AND e.date <= :endDate
        GROUP BY e.categoryId
        ORDER BY totalAmount DESC
    """)
    abstract suspend fun getCategorySpendingInRange(
        userId: Long,
        startDate: String,
        endDate: String
    ): List<CategorySpending>

    // Today's spending
    @Query("""
        SELECT COALESCE(SUM(amount), 0) FROM expenses 
        WHERE userId = :userId AND date = :today
    """)
    abstract suspend fun getTodaySpending(userId: Long, today: String): Double
}
