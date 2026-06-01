package com.aureus.budget.`data`.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performInTransactionSuspending
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.aureus.budget.`data`.entity.BudgetGoal
import javax.`annotation`.processing.Generated
import kotlin.Double
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
public class BudgetGoalDao_Impl(
  __db: RoomDatabase,
) : BudgetGoalDao() {
  private val __db: RoomDatabase

  private val __insertAdapterOfBudgetGoal: EntityInsertAdapter<BudgetGoal>

  private val __deleteAdapterOfBudgetGoal: EntityDeleteOrUpdateAdapter<BudgetGoal>

  private val __updateAdapterOfBudgetGoal: EntityDeleteOrUpdateAdapter<BudgetGoal>
  init {
    this.__db = __db
    this.__insertAdapterOfBudgetGoal = object : EntityInsertAdapter<BudgetGoal>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `budget_goals` (`id`,`userId`,`minGoal`,`maxGoal`,`month`,`year`,`updatedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: BudgetGoal) {
        statement.bindLong(1, entity.id)
        statement.bindLong(2, entity.userId)
        statement.bindDouble(3, entity.minGoal)
        statement.bindDouble(4, entity.maxGoal)
        statement.bindLong(5, entity.month.toLong())
        statement.bindLong(6, entity.year.toLong())
        statement.bindLong(7, entity.updatedAt)
      }
    }
    this.__deleteAdapterOfBudgetGoal = object : EntityDeleteOrUpdateAdapter<BudgetGoal>() {
      protected override fun createQuery(): String = "DELETE FROM `budget_goals` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: BudgetGoal) {
        statement.bindLong(1, entity.id)
      }
    }
    this.__updateAdapterOfBudgetGoal = object : EntityDeleteOrUpdateAdapter<BudgetGoal>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `budget_goals` SET `id` = ?,`userId` = ?,`minGoal` = ?,`maxGoal` = ?,`month` = ?,`year` = ?,`updatedAt` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: BudgetGoal) {
        statement.bindLong(1, entity.id)
        statement.bindLong(2, entity.userId)
        statement.bindDouble(3, entity.minGoal)
        statement.bindDouble(4, entity.maxGoal)
        statement.bindLong(5, entity.month.toLong())
        statement.bindLong(6, entity.year.toLong())
        statement.bindLong(7, entity.updatedAt)
        statement.bindLong(8, entity.id)
      }
    }
  }

  public override suspend fun insertGoal(goal: BudgetGoal): Long = performSuspending(__db, false,
      true) { _connection ->
    val _result: Long = __insertAdapterOfBudgetGoal.insertAndReturnId(_connection, goal)
    _result
  }

  public override suspend fun deleteGoal(goal: BudgetGoal): Unit = performSuspending(__db, false,
      true) { _connection ->
    __deleteAdapterOfBudgetGoal.handle(_connection, goal)
  }

  public override suspend fun updateGoal(goal: BudgetGoal): Unit = performSuspending(__db, false,
      true) { _connection ->
    __updateAdapterOfBudgetGoal.handle(_connection, goal)
  }

  public override suspend fun upsertGoal(goal: BudgetGoal): Unit =
      performInTransactionSuspending(__db) {
    super@BudgetGoalDao_Impl.upsertGoal(goal)
  }

  public override suspend fun getGoalForMonth(
    userId: Long,
    month: Int,
    year: Int,
  ): BudgetGoal? {
    val _sql: String = """
        |
        |        SELECT * FROM budget_goals 
        |        WHERE userId = ? AND month = ? AND year = ? 
        |        LIMIT 1
        |    
        """.trimMargin()
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, userId)
        _argIndex = 2
        _stmt.bindLong(_argIndex, month.toLong())
        _argIndex = 3
        _stmt.bindLong(_argIndex, year.toLong())
        val _cursorIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _cursorIndexOfUserId: Int = getColumnIndexOrThrow(_stmt, "userId")
        val _cursorIndexOfMinGoal: Int = getColumnIndexOrThrow(_stmt, "minGoal")
        val _cursorIndexOfMaxGoal: Int = getColumnIndexOrThrow(_stmt, "maxGoal")
        val _cursorIndexOfMonth: Int = getColumnIndexOrThrow(_stmt, "month")
        val _cursorIndexOfYear: Int = getColumnIndexOrThrow(_stmt, "year")
        val _cursorIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _result: BudgetGoal?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_cursorIndexOfId)
          val _tmpUserId: Long
          _tmpUserId = _stmt.getLong(_cursorIndexOfUserId)
          val _tmpMinGoal: Double
          _tmpMinGoal = _stmt.getDouble(_cursorIndexOfMinGoal)
          val _tmpMaxGoal: Double
          _tmpMaxGoal = _stmt.getDouble(_cursorIndexOfMaxGoal)
          val _tmpMonth: Int
          _tmpMonth = _stmt.getLong(_cursorIndexOfMonth).toInt()
          val _tmpYear: Int
          _tmpYear = _stmt.getLong(_cursorIndexOfYear).toInt()
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_cursorIndexOfUpdatedAt)
          _result =
              BudgetGoal(_tmpId,_tmpUserId,_tmpMinGoal,_tmpMaxGoal,_tmpMonth,_tmpYear,_tmpUpdatedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getGoalForMonthLive(
    userId: Long,
    month: Int,
    year: Int,
  ): Flow<BudgetGoal?> {
    val _sql: String = """
        |
        |        SELECT * FROM budget_goals 
        |        WHERE userId = ? AND month = ? AND year = ? 
        |        LIMIT 1
        |    
        """.trimMargin()
    return createFlow(__db, false, arrayOf("budget_goals")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, userId)
        _argIndex = 2
        _stmt.bindLong(_argIndex, month.toLong())
        _argIndex = 3
        _stmt.bindLong(_argIndex, year.toLong())
        val _cursorIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _cursorIndexOfUserId: Int = getColumnIndexOrThrow(_stmt, "userId")
        val _cursorIndexOfMinGoal: Int = getColumnIndexOrThrow(_stmt, "minGoal")
        val _cursorIndexOfMaxGoal: Int = getColumnIndexOrThrow(_stmt, "maxGoal")
        val _cursorIndexOfMonth: Int = getColumnIndexOrThrow(_stmt, "month")
        val _cursorIndexOfYear: Int = getColumnIndexOrThrow(_stmt, "year")
        val _cursorIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _result: BudgetGoal?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_cursorIndexOfId)
          val _tmpUserId: Long
          _tmpUserId = _stmt.getLong(_cursorIndexOfUserId)
          val _tmpMinGoal: Double
          _tmpMinGoal = _stmt.getDouble(_cursorIndexOfMinGoal)
          val _tmpMaxGoal: Double
          _tmpMaxGoal = _stmt.getDouble(_cursorIndexOfMaxGoal)
          val _tmpMonth: Int
          _tmpMonth = _stmt.getLong(_cursorIndexOfMonth).toInt()
          val _tmpYear: Int
          _tmpYear = _stmt.getLong(_cursorIndexOfYear).toInt()
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_cursorIndexOfUpdatedAt)
          _result =
              BudgetGoal(_tmpId,_tmpUserId,_tmpMinGoal,_tmpMaxGoal,_tmpMonth,_tmpYear,_tmpUpdatedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getAllGoalsForUser(userId: Long): Flow<List<BudgetGoal>> {
    val _sql: String = "SELECT * FROM budget_goals WHERE userId = ? ORDER BY year DESC, month DESC"
    return createFlow(__db, false, arrayOf("budget_goals")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, userId)
        val _cursorIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _cursorIndexOfUserId: Int = getColumnIndexOrThrow(_stmt, "userId")
        val _cursorIndexOfMinGoal: Int = getColumnIndexOrThrow(_stmt, "minGoal")
        val _cursorIndexOfMaxGoal: Int = getColumnIndexOrThrow(_stmt, "maxGoal")
        val _cursorIndexOfMonth: Int = getColumnIndexOrThrow(_stmt, "month")
        val _cursorIndexOfYear: Int = getColumnIndexOrThrow(_stmt, "year")
        val _cursorIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _result: MutableList<BudgetGoal> = mutableListOf()
        while (_stmt.step()) {
          val _item: BudgetGoal
          val _tmpId: Long
          _tmpId = _stmt.getLong(_cursorIndexOfId)
          val _tmpUserId: Long
          _tmpUserId = _stmt.getLong(_cursorIndexOfUserId)
          val _tmpMinGoal: Double
          _tmpMinGoal = _stmt.getDouble(_cursorIndexOfMinGoal)
          val _tmpMaxGoal: Double
          _tmpMaxGoal = _stmt.getDouble(_cursorIndexOfMaxGoal)
          val _tmpMonth: Int
          _tmpMonth = _stmt.getLong(_cursorIndexOfMonth).toInt()
          val _tmpYear: Int
          _tmpYear = _stmt.getLong(_cursorIndexOfYear).toInt()
          val _tmpUpdatedAt: Long
          _tmpUpdatedAt = _stmt.getLong(_cursorIndexOfUpdatedAt)
          _item =
              BudgetGoal(_tmpId,_tmpUserId,_tmpMinGoal,_tmpMaxGoal,_tmpMonth,_tmpYear,_tmpUpdatedAt)
          _result.add(_item)
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
