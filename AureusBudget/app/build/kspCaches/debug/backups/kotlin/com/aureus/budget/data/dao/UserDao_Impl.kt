package com.aureus.budget.`data`.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.aureus.budget.`data`.entity.User
import javax.`annotation`.processing.Generated
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class UserDao_Impl(
  __db: RoomDatabase,
) : UserDao() {
  private val __db: RoomDatabase

  private val __insertAdapterOfUser: EntityInsertAdapter<User>

  private val __deleteAdapterOfUser: EntityDeleteOrUpdateAdapter<User>

  private val __updateAdapterOfUser: EntityDeleteOrUpdateAdapter<User>
  init {
    this.__db = __db
    this.__insertAdapterOfUser = object : EntityInsertAdapter<User>() {
      protected override fun createQuery(): String =
          "INSERT OR ABORT INTO `users` (`id`,`username`,`passwordHash`,`email`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: User) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.username)
        statement.bindText(3, entity.passwordHash)
        statement.bindText(4, entity.email)
        statement.bindLong(5, entity.createdAt)
      }
    }
    this.__deleteAdapterOfUser = object : EntityDeleteOrUpdateAdapter<User>() {
      protected override fun createQuery(): String = "DELETE FROM `users` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: User) {
        statement.bindLong(1, entity.id)
      }
    }
    this.__updateAdapterOfUser = object : EntityDeleteOrUpdateAdapter<User>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `users` SET `id` = ?,`username` = ?,`passwordHash` = ?,`email` = ?,`createdAt` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: User) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.username)
        statement.bindText(3, entity.passwordHash)
        statement.bindText(4, entity.email)
        statement.bindLong(5, entity.createdAt)
        statement.bindLong(6, entity.id)
      }
    }
  }

  public override suspend fun insertUser(user: User): Long = performSuspending(__db, false, true) {
      _connection ->
    val _result: Long = __insertAdapterOfUser.insertAndReturnId(_connection, user)
    _result
  }

  public override suspend fun deleteUser(user: User): Unit = performSuspending(__db, false, true) {
      _connection ->
    __deleteAdapterOfUser.handle(_connection, user)
  }

  public override suspend fun updateUser(user: User): Unit = performSuspending(__db, false, true) {
      _connection ->
    __updateAdapterOfUser.handle(_connection, user)
  }

  public override suspend fun getUserByUsername(username: String): User? {
    val _sql: String = "SELECT * FROM users WHERE username = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, username)
        val _cursorIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _cursorIndexOfUsername: Int = getColumnIndexOrThrow(_stmt, "username")
        val _cursorIndexOfPasswordHash: Int = getColumnIndexOrThrow(_stmt, "passwordHash")
        val _cursorIndexOfEmail: Int = getColumnIndexOrThrow(_stmt, "email")
        val _cursorIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _result: User?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_cursorIndexOfId)
          val _tmpUsername: String
          _tmpUsername = _stmt.getText(_cursorIndexOfUsername)
          val _tmpPasswordHash: String
          _tmpPasswordHash = _stmt.getText(_cursorIndexOfPasswordHash)
          val _tmpEmail: String
          _tmpEmail = _stmt.getText(_cursorIndexOfEmail)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_cursorIndexOfCreatedAt)
          _result = User(_tmpId,_tmpUsername,_tmpPasswordHash,_tmpEmail,_tmpCreatedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getUserById(id: Long): User? {
    val _sql: String = "SELECT * FROM users WHERE id = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        val _cursorIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _cursorIndexOfUsername: Int = getColumnIndexOrThrow(_stmt, "username")
        val _cursorIndexOfPasswordHash: Int = getColumnIndexOrThrow(_stmt, "passwordHash")
        val _cursorIndexOfEmail: Int = getColumnIndexOrThrow(_stmt, "email")
        val _cursorIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _result: User?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_cursorIndexOfId)
          val _tmpUsername: String
          _tmpUsername = _stmt.getText(_cursorIndexOfUsername)
          val _tmpPasswordHash: String
          _tmpPasswordHash = _stmt.getText(_cursorIndexOfPasswordHash)
          val _tmpEmail: String
          _tmpEmail = _stmt.getText(_cursorIndexOfEmail)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_cursorIndexOfCreatedAt)
          _result = User(_tmpId,_tmpUsername,_tmpPasswordHash,_tmpEmail,_tmpCreatedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun login(username: String, passwordHash: String): User? {
    val _sql: String = "SELECT * FROM users WHERE username = ? AND passwordHash = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, username)
        _argIndex = 2
        _stmt.bindText(_argIndex, passwordHash)
        val _cursorIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _cursorIndexOfUsername: Int = getColumnIndexOrThrow(_stmt, "username")
        val _cursorIndexOfPasswordHash: Int = getColumnIndexOrThrow(_stmt, "passwordHash")
        val _cursorIndexOfEmail: Int = getColumnIndexOrThrow(_stmt, "email")
        val _cursorIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _result: User?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_cursorIndexOfId)
          val _tmpUsername: String
          _tmpUsername = _stmt.getText(_cursorIndexOfUsername)
          val _tmpPasswordHash: String
          _tmpPasswordHash = _stmt.getText(_cursorIndexOfPasswordHash)
          val _tmpEmail: String
          _tmpEmail = _stmt.getText(_cursorIndexOfEmail)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_cursorIndexOfCreatedAt)
          _result = User(_tmpId,_tmpUsername,_tmpPasswordHash,_tmpEmail,_tmpCreatedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
