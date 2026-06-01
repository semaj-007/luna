package com.aureus.budget.data.dao

import androidx.room.*
import com.aureus.budget.data.entity.Category
import kotlinx.coroutines.flow.Flow

@Dao
abstract class CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertCategory(category: Category): Long

    @Update
    abstract suspend fun updateCategory(category: Category)

    @Delete
    abstract suspend fun deleteCategory(category: Category)

    @Query("SELECT * FROM categories WHERE userId = :userId ORDER BY name ASC")
    abstract fun getCategoriesForUser(userId: Long): Flow<List<Category>>

    @Query("SELECT * FROM categories WHERE userId = :userId ORDER BY name ASC")
    abstract suspend fun getCategoriesForUserSync(userId: Long): List<Category>

    @Query("SELECT * FROM categories WHERE id = :id LIMIT 1")
    abstract suspend fun getCategoryById(id: Long): Category?
}
