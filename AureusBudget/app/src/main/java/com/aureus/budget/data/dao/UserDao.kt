package com.aureus.budget.data.dao

import androidx.room.*
import com.aureus.budget.data.entity.User
import kotlinx.coroutines.flow.Flow

@Dao
abstract class UserDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract suspend fun insertUser(user: User): Long

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    abstract suspend fun getUserByUsername(username: String): User?

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    abstract suspend fun getUserById(id: Long): User?

    @Query("SELECT * FROM users WHERE username = :username AND passwordHash = :passwordHash LIMIT 1")
    abstract suspend fun login(username: String, passwordHash: String): User?

    @Update
    abstract suspend fun updateUser(user: User)

    @Delete
    abstract suspend fun deleteUser(user: User)
}
