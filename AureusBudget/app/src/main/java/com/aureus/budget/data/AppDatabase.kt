package com.aureus.budget.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.aureus.budget.data.dao.BudgetGoalDao
import com.aureus.budget.data.dao.CategoryDao
import com.aureus.budget.data.dao.ExpenseDao
import com.aureus.budget.data.dao.UserDao
import com.aureus.budget.data.entity.BudgetGoal
import com.aureus.budget.data.entity.Category
import com.aureus.budget.data.entity.Expense
import com.aureus.budget.data.entity.User

@Database(
    entities = [User::class, Category::class, Expense::class, BudgetGoal::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun categoryDao(): CategoryDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun budgetGoalDao(): BudgetGoalDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "aureus_budget_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
