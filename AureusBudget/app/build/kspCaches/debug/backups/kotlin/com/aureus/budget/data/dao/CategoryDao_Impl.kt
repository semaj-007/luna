package com.aureus.budget.`data`.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.aureus.budget.`data`.entity.Category
import javax.`annotation`.processing.Generated
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class CategoryDao_Impl(
  __db: RoomDatabase,
) : CategoryDao() {
  private val __db: RoomDatabase

  private val __insertAdapterOfCategory: EntityInsertAdapter<Category>

  private val __deleteAdapterOfCategory: EntityDeleteOrUpdateAdapter<Category>

  private val __updateAdapterOfCategory: EntityDeleteOrUpdateAdapter<Category>
  init {
    this.__db = __db
    this.__insertAdapterOfCategory = object : EntityInsertAdapter<Category>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `categories` (`id`,`userId`,`name`,`colorHex`,`iconName`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: Category) {
        statement.bindLong(1, entity.id)
        statement.bindLong(2, entity.userId)
        statement.bindText(3, entity.name)
        statement.bindText(4, entity.colorHex)
        statement.bindText(5, entity.iconName)
        statement.bindLong(6, entity.createdAt)
      }
    }
    this.__deleteAdapterOfCategory = object : EntityDeleteOrUpdateAdapter<Category>() {
      protected override fun createQuery(): String = "DELETE FROM `categories` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: Category) {
        statement.bindLong(1, entity.id)
      }
    }
    this.__updateAdapterOfCategory = object : EntityDeleteOrUpdateAdapter<Category>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `categories` SET `id` = ?,`userId` = ?,`name` = ?,`colorHex` = ?,`iconName` = ?,`createdAt` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: Category) {
        statement.bindLong(1, entity.id)
        statement.bindLong(2, entity.userId)
        statement.bindText(3, entity.name)
        statement.bindText(4, entity.colorHex)
        statement.bindText(5, entity.iconName)
        statement.bindLong(6, entity.createdAt)
        statement.bindLong(7, entity.id)
      }
    }
  }

  public override suspend fun insertCategory(category: Category): Long = performSuspending(__db,
      false, true) { _connection ->
    val _result: Long = __insertAdapterOfCategory.insertAndReturnId(_connection, category)
    _result
  }

  public override suspend fun deleteCategory(category: Category): Unit = performSuspending(__db,
      false, true) { _connection ->
    __deleteAdapterOfCategory.handle(_connection, category)
  }

  public override suspend fun updateCategory(category: Category): Unit = performSuspending(__db,
      false, true) { _connection ->
    __updateAdapterOfCategory.handle(_connection, category)
  }

  public override fun getCategoriesForUser(userId: Long): Flow<List<Category>> {
    val _sql: String = "SELECT * FROM categories WHERE userId = ? ORDER BY name ASC"
    return createFlow(__db, false, arrayOf("categories")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, userId)
        val _cursorIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _cursorIndexOfUserId: Int = getColumnIndexOrThrow(_stmt, "userId")
        val _cursorIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _cursorIndexOfColorHex: Int = getColumnIndexOrThrow(_stmt, "colorHex")
        val _cursorIndexOfIconName: Int = getColumnIndexOrThrow(_stmt, "iconName")
        val _cursorIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _result: MutableList<Category> = mutableListOf()
        while (_stmt.step()) {
          val _item: Category
          val _tmpId: Long
          _tmpId = _stmt.getLong(_cursorIndexOfId)
          val _tmpUserId: Long
          _tmpUserId = _stmt.getLong(_cursorIndexOfUserId)
          val _tmpName: String
          _tmpName = _stmt.getText(_cursorIndexOfName)
          val _tmpColorHex: String
          _tmpColorHex = _stmt.getText(_cursorIndexOfColorHex)
          val _tmpIconName: String
          _tmpIconName = _stmt.getText(_cursorIndexOfIconName)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_cursorIndexOfCreatedAt)
          _item = Category(_tmpId,_tmpUserId,_tmpName,_tmpColorHex,_tmpIconName,_tmpCreatedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getCategoriesForUserSync(userId: Long): List<Category> {
    val _sql: String = "SELECT * FROM categories WHERE userId = ? ORDER BY name ASC"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, userId)
        val _cursorIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _cursorIndexOfUserId: Int = getColumnIndexOrThrow(_stmt, "userId")
        val _cursorIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _cursorIndexOfColorHex: Int = getColumnIndexOrThrow(_stmt, "colorHex")
        val _cursorIndexOfIconName: Int = getColumnIndexOrThrow(_stmt, "iconName")
        val _cursorIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _result: MutableList<Category> = mutableListOf()
        while (_stmt.step()) {
          val _item: Category
          val _tmpId: Long
          _tmpId = _stmt.getLong(_cursorIndexOfId)
          val _tmpUserId: Long
          _tmpUserId = _stmt.getLong(_cursorIndexOfUserId)
          val _tmpName: String
          _tmpName = _stmt.getText(_cursorIndexOfName)
          val _tmpColorHex: String
          _tmpColorHex = _stmt.getText(_cursorIndexOfColorHex)
          val _tmpIconName: String
          _tmpIconName = _stmt.getText(_cursorIndexOfIconName)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_cursorIndexOfCreatedAt)
          _item = Category(_tmpId,_tmpUserId,_tmpName,_tmpColorHex,_tmpIconName,_tmpCreatedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getCategoryById(id: Long): Category? {
    val _sql: String = "SELECT * FROM categories WHERE id = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        val _cursorIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _cursorIndexOfUserId: Int = getColumnIndexOrThrow(_stmt, "userId")
        val _cursorIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _cursorIndexOfColorHex: Int = getColumnIndexOrThrow(_stmt, "colorHex")
        val _cursorIndexOfIconName: Int = getColumnIndexOrThrow(_stmt, "iconName")
        val _cursorIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _result: Category?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_cursorIndexOfId)
          val _tmpUserId: Long
          _tmpUserId = _stmt.getLong(_cursorIndexOfUserId)
          val _tmpName: String
          _tmpName = _stmt.getText(_cursorIndexOfName)
          val _tmpColorHex: String
          _tmpColorHex = _stmt.getText(_cursorIndexOfColorHex)
          val _tmpIconName: String
          _tmpIconName = _stmt.getText(_cursorIndexOfIconName)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_cursorIndexOfCreatedAt)
          _result = Category(_tmpId,_tmpUserId,_tmpName,_tmpColorHex,_tmpIconName,_tmpCreatedAt)
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
