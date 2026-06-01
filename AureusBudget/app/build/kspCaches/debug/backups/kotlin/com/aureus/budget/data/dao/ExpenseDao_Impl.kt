package com.aureus.budget.`data`.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.aureus.budget.`data`.entity.Expense
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
public class ExpenseDao_Impl(
  __db: RoomDatabase,
) : ExpenseDao() {
  private val __db: RoomDatabase

  private val __insertAdapterOfExpense: EntityInsertAdapter<Expense>

  private val __deleteAdapterOfExpense: EntityDeleteOrUpdateAdapter<Expense>

  private val __updateAdapterOfExpense: EntityDeleteOrUpdateAdapter<Expense>
  init {
    this.__db = __db
    this.__insertAdapterOfExpense = object : EntityInsertAdapter<Expense>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `expenses` (`id`,`userId`,`categoryId`,`amount`,`description`,`date`,`startTime`,`endTime`,`notes`,`photoPath`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: Expense) {
        statement.bindLong(1, entity.id)
        statement.bindLong(2, entity.userId)
        val _tmpCategoryId: Long? = entity.categoryId
        if (_tmpCategoryId == null) {
          statement.bindNull(3)
        } else {
          statement.bindLong(3, _tmpCategoryId)
        }
        statement.bindDouble(4, entity.amount)
        statement.bindText(5, entity.description)
        statement.bindText(6, entity.date)
        statement.bindText(7, entity.startTime)
        statement.bindText(8, entity.endTime)
        statement.bindText(9, entity.notes)
        val _tmpPhotoPath: String? = entity.photoPath
        if (_tmpPhotoPath == null) {
          statement.bindNull(10)
        } else {
          statement.bindText(10, _tmpPhotoPath)
        }
        statement.bindLong(11, entity.createdAt)
      }
    }
    this.__deleteAdapterOfExpense = object : EntityDeleteOrUpdateAdapter<Expense>() {
      protected override fun createQuery(): String = "DELETE FROM `expenses` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: Expense) {
        statement.bindLong(1, entity.id)
      }
    }
    this.__updateAdapterOfExpense = object : EntityDeleteOrUpdateAdapter<Expense>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `expenses` SET `id` = ?,`userId` = ?,`categoryId` = ?,`amount` = ?,`description` = ?,`date` = ?,`startTime` = ?,`endTime` = ?,`notes` = ?,`photoPath` = ?,`createdAt` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: Expense) {
        statement.bindLong(1, entity.id)
        statement.bindLong(2, entity.userId)
        val _tmpCategoryId: Long? = entity.categoryId
        if (_tmpCategoryId == null) {
          statement.bindNull(3)
        } else {
          statement.bindLong(3, _tmpCategoryId)
        }
        statement.bindDouble(4, entity.amount)
        statement.bindText(5, entity.description)
        statement.bindText(6, entity.date)
        statement.bindText(7, entity.startTime)
        statement.bindText(8, entity.endTime)
        statement.bindText(9, entity.notes)
        val _tmpPhotoPath: String? = entity.photoPath
        if (_tmpPhotoPath == null) {
          statement.bindNull(10)
        } else {
          statement.bindText(10, _tmpPhotoPath)
        }
        statement.bindLong(11, entity.createdAt)
        statement.bindLong(12, entity.id)
      }
    }
  }

  public override suspend fun insertExpense(expense: Expense): Long = performSuspending(__db, false,
      true) { _connection ->
    val _result: Long = __insertAdapterOfExpense.insertAndReturnId(_connection, expense)
    _result
  }

  public override suspend fun deleteExpense(expense: Expense): Unit = performSuspending(__db, false,
      true) { _connection ->
    __deleteAdapterOfExpense.handle(_connection, expense)
  }

  public override suspend fun updateExpense(expense: Expense): Unit = performSuspending(__db, false,
      true) { _connection ->
    __updateAdapterOfExpense.handle(_connection, expense)
  }

  public override suspend fun getExpenseById(id: Long): Expense? {
    val _sql: String = "SELECT * FROM expenses WHERE id = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        val _cursorIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _cursorIndexOfUserId: Int = getColumnIndexOrThrow(_stmt, "userId")
        val _cursorIndexOfCategoryId: Int = getColumnIndexOrThrow(_stmt, "categoryId")
        val _cursorIndexOfAmount: Int = getColumnIndexOrThrow(_stmt, "amount")
        val _cursorIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _cursorIndexOfDate: Int = getColumnIndexOrThrow(_stmt, "date")
        val _cursorIndexOfStartTime: Int = getColumnIndexOrThrow(_stmt, "startTime")
        val _cursorIndexOfEndTime: Int = getColumnIndexOrThrow(_stmt, "endTime")
        val _cursorIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _cursorIndexOfPhotoPath: Int = getColumnIndexOrThrow(_stmt, "photoPath")
        val _cursorIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _result: Expense?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_cursorIndexOfId)
          val _tmpUserId: Long
          _tmpUserId = _stmt.getLong(_cursorIndexOfUserId)
          val _tmpCategoryId: Long?
          if (_stmt.isNull(_cursorIndexOfCategoryId)) {
            _tmpCategoryId = null
          } else {
            _tmpCategoryId = _stmt.getLong(_cursorIndexOfCategoryId)
          }
          val _tmpAmount: Double
          _tmpAmount = _stmt.getDouble(_cursorIndexOfAmount)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_cursorIndexOfDescription)
          val _tmpDate: String
          _tmpDate = _stmt.getText(_cursorIndexOfDate)
          val _tmpStartTime: String
          _tmpStartTime = _stmt.getText(_cursorIndexOfStartTime)
          val _tmpEndTime: String
          _tmpEndTime = _stmt.getText(_cursorIndexOfEndTime)
          val _tmpNotes: String
          _tmpNotes = _stmt.getText(_cursorIndexOfNotes)
          val _tmpPhotoPath: String?
          if (_stmt.isNull(_cursorIndexOfPhotoPath)) {
            _tmpPhotoPath = null
          } else {
            _tmpPhotoPath = _stmt.getText(_cursorIndexOfPhotoPath)
          }
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_cursorIndexOfCreatedAt)
          _result =
              Expense(_tmpId,_tmpUserId,_tmpCategoryId,_tmpAmount,_tmpDescription,_tmpDate,_tmpStartTime,_tmpEndTime,_tmpNotes,_tmpPhotoPath,_tmpCreatedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getAllExpensesForUser(userId: Long): Flow<List<Expense>> {
    val _sql: String = """
        |
        |        SELECT * FROM expenses 
        |        WHERE userId = ? 
        |        ORDER BY date DESC, startTime DESC
        |    
        """.trimMargin()
    return createFlow(__db, false, arrayOf("expenses")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, userId)
        val _cursorIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _cursorIndexOfUserId: Int = getColumnIndexOrThrow(_stmt, "userId")
        val _cursorIndexOfCategoryId: Int = getColumnIndexOrThrow(_stmt, "categoryId")
        val _cursorIndexOfAmount: Int = getColumnIndexOrThrow(_stmt, "amount")
        val _cursorIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _cursorIndexOfDate: Int = getColumnIndexOrThrow(_stmt, "date")
        val _cursorIndexOfStartTime: Int = getColumnIndexOrThrow(_stmt, "startTime")
        val _cursorIndexOfEndTime: Int = getColumnIndexOrThrow(_stmt, "endTime")
        val _cursorIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _cursorIndexOfPhotoPath: Int = getColumnIndexOrThrow(_stmt, "photoPath")
        val _cursorIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _result: MutableList<Expense> = mutableListOf()
        while (_stmt.step()) {
          val _item: Expense
          val _tmpId: Long
          _tmpId = _stmt.getLong(_cursorIndexOfId)
          val _tmpUserId: Long
          _tmpUserId = _stmt.getLong(_cursorIndexOfUserId)
          val _tmpCategoryId: Long?
          if (_stmt.isNull(_cursorIndexOfCategoryId)) {
            _tmpCategoryId = null
          } else {
            _tmpCategoryId = _stmt.getLong(_cursorIndexOfCategoryId)
          }
          val _tmpAmount: Double
          _tmpAmount = _stmt.getDouble(_cursorIndexOfAmount)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_cursorIndexOfDescription)
          val _tmpDate: String
          _tmpDate = _stmt.getText(_cursorIndexOfDate)
          val _tmpStartTime: String
          _tmpStartTime = _stmt.getText(_cursorIndexOfStartTime)
          val _tmpEndTime: String
          _tmpEndTime = _stmt.getText(_cursorIndexOfEndTime)
          val _tmpNotes: String
          _tmpNotes = _stmt.getText(_cursorIndexOfNotes)
          val _tmpPhotoPath: String?
          if (_stmt.isNull(_cursorIndexOfPhotoPath)) {
            _tmpPhotoPath = null
          } else {
            _tmpPhotoPath = _stmt.getText(_cursorIndexOfPhotoPath)
          }
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_cursorIndexOfCreatedAt)
          _item =
              Expense(_tmpId,_tmpUserId,_tmpCategoryId,_tmpAmount,_tmpDescription,_tmpDate,_tmpStartTime,_tmpEndTime,_tmpNotes,_tmpPhotoPath,_tmpCreatedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getExpensesInRange(
    userId: Long,
    startDate: String,
    endDate: String,
  ): Flow<List<Expense>> {
    val _sql: String = """
        |
        |        SELECT * FROM expenses 
        |        WHERE userId = ? AND date >= ? AND date <= ?
        |        ORDER BY date DESC, startTime DESC
        |    
        """.trimMargin()
    return createFlow(__db, false, arrayOf("expenses")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, userId)
        _argIndex = 2
        _stmt.bindText(_argIndex, startDate)
        _argIndex = 3
        _stmt.bindText(_argIndex, endDate)
        val _cursorIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _cursorIndexOfUserId: Int = getColumnIndexOrThrow(_stmt, "userId")
        val _cursorIndexOfCategoryId: Int = getColumnIndexOrThrow(_stmt, "categoryId")
        val _cursorIndexOfAmount: Int = getColumnIndexOrThrow(_stmt, "amount")
        val _cursorIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _cursorIndexOfDate: Int = getColumnIndexOrThrow(_stmt, "date")
        val _cursorIndexOfStartTime: Int = getColumnIndexOrThrow(_stmt, "startTime")
        val _cursorIndexOfEndTime: Int = getColumnIndexOrThrow(_stmt, "endTime")
        val _cursorIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _cursorIndexOfPhotoPath: Int = getColumnIndexOrThrow(_stmt, "photoPath")
        val _cursorIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _result: MutableList<Expense> = mutableListOf()
        while (_stmt.step()) {
          val _item: Expense
          val _tmpId: Long
          _tmpId = _stmt.getLong(_cursorIndexOfId)
          val _tmpUserId: Long
          _tmpUserId = _stmt.getLong(_cursorIndexOfUserId)
          val _tmpCategoryId: Long?
          if (_stmt.isNull(_cursorIndexOfCategoryId)) {
            _tmpCategoryId = null
          } else {
            _tmpCategoryId = _stmt.getLong(_cursorIndexOfCategoryId)
          }
          val _tmpAmount: Double
          _tmpAmount = _stmt.getDouble(_cursorIndexOfAmount)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_cursorIndexOfDescription)
          val _tmpDate: String
          _tmpDate = _stmt.getText(_cursorIndexOfDate)
          val _tmpStartTime: String
          _tmpStartTime = _stmt.getText(_cursorIndexOfStartTime)
          val _tmpEndTime: String
          _tmpEndTime = _stmt.getText(_cursorIndexOfEndTime)
          val _tmpNotes: String
          _tmpNotes = _stmt.getText(_cursorIndexOfNotes)
          val _tmpPhotoPath: String?
          if (_stmt.isNull(_cursorIndexOfPhotoPath)) {
            _tmpPhotoPath = null
          } else {
            _tmpPhotoPath = _stmt.getText(_cursorIndexOfPhotoPath)
          }
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_cursorIndexOfCreatedAt)
          _item =
              Expense(_tmpId,_tmpUserId,_tmpCategoryId,_tmpAmount,_tmpDescription,_tmpDate,_tmpStartTime,_tmpEndTime,_tmpNotes,_tmpPhotoPath,_tmpCreatedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getExpensesInRangeSync(
    userId: Long,
    startDate: String,
    endDate: String,
  ): List<Expense> {
    val _sql: String = """
        |
        |        SELECT * FROM expenses 
        |        WHERE userId = ? AND date >= ? AND date <= ?
        |        ORDER BY date DESC, startTime DESC
        |    
        """.trimMargin()
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, userId)
        _argIndex = 2
        _stmt.bindText(_argIndex, startDate)
        _argIndex = 3
        _stmt.bindText(_argIndex, endDate)
        val _cursorIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _cursorIndexOfUserId: Int = getColumnIndexOrThrow(_stmt, "userId")
        val _cursorIndexOfCategoryId: Int = getColumnIndexOrThrow(_stmt, "categoryId")
        val _cursorIndexOfAmount: Int = getColumnIndexOrThrow(_stmt, "amount")
        val _cursorIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _cursorIndexOfDate: Int = getColumnIndexOrThrow(_stmt, "date")
        val _cursorIndexOfStartTime: Int = getColumnIndexOrThrow(_stmt, "startTime")
        val _cursorIndexOfEndTime: Int = getColumnIndexOrThrow(_stmt, "endTime")
        val _cursorIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _cursorIndexOfPhotoPath: Int = getColumnIndexOrThrow(_stmt, "photoPath")
        val _cursorIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _result: MutableList<Expense> = mutableListOf()
        while (_stmt.step()) {
          val _item: Expense
          val _tmpId: Long
          _tmpId = _stmt.getLong(_cursorIndexOfId)
          val _tmpUserId: Long
          _tmpUserId = _stmt.getLong(_cursorIndexOfUserId)
          val _tmpCategoryId: Long?
          if (_stmt.isNull(_cursorIndexOfCategoryId)) {
            _tmpCategoryId = null
          } else {
            _tmpCategoryId = _stmt.getLong(_cursorIndexOfCategoryId)
          }
          val _tmpAmount: Double
          _tmpAmount = _stmt.getDouble(_cursorIndexOfAmount)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_cursorIndexOfDescription)
          val _tmpDate: String
          _tmpDate = _stmt.getText(_cursorIndexOfDate)
          val _tmpStartTime: String
          _tmpStartTime = _stmt.getText(_cursorIndexOfStartTime)
          val _tmpEndTime: String
          _tmpEndTime = _stmt.getText(_cursorIndexOfEndTime)
          val _tmpNotes: String
          _tmpNotes = _stmt.getText(_cursorIndexOfNotes)
          val _tmpPhotoPath: String?
          if (_stmt.isNull(_cursorIndexOfPhotoPath)) {
            _tmpPhotoPath = null
          } else {
            _tmpPhotoPath = _stmt.getText(_cursorIndexOfPhotoPath)
          }
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_cursorIndexOfCreatedAt)
          _item =
              Expense(_tmpId,_tmpUserId,_tmpCategoryId,_tmpAmount,_tmpDescription,_tmpDate,_tmpStartTime,_tmpEndTime,_tmpNotes,_tmpPhotoPath,_tmpCreatedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getTotalSpentInRange(
    userId: Long,
    startDate: String,
    endDate: String,
  ): Double {
    val _sql: String = """
        |
        |        SELECT COALESCE(SUM(amount), 0) FROM expenses 
        |        WHERE userId = ? AND date >= ? AND date <= ?
        |    
        """.trimMargin()
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, userId)
        _argIndex = 2
        _stmt.bindText(_argIndex, startDate)
        _argIndex = 3
        _stmt.bindText(_argIndex, endDate)
        val _result: Double
        if (_stmt.step()) {
          val _tmp: Double
          _tmp = _stmt.getDouble(0)
          _result = _tmp
        } else {
          _result = 0.0
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getMonthlyTotal(userId: Long, monthPrefix: String): Flow<Double> {
    val _sql: String = """
        |
        |        SELECT COALESCE(SUM(amount), 0) FROM expenses 
        |        WHERE userId = ? AND date LIKE ?
        |    
        """.trimMargin()
    return createFlow(__db, false, arrayOf("expenses")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, userId)
        _argIndex = 2
        _stmt.bindText(_argIndex, monthPrefix)
        val _result: Double
        if (_stmt.step()) {
          val _tmp: Double
          _tmp = _stmt.getDouble(0)
          _result = _tmp
        } else {
          _result = 0.0
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getRecentExpenses(userId: Long, limit: Int): Flow<List<Expense>> {
    val _sql: String = """
        |
        |        SELECT * FROM expenses 
        |        WHERE userId = ? 
        |        ORDER BY date DESC, startTime DESC 
        |        LIMIT ?
        |    
        """.trimMargin()
    return createFlow(__db, false, arrayOf("expenses")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, userId)
        _argIndex = 2
        _stmt.bindLong(_argIndex, limit.toLong())
        val _cursorIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _cursorIndexOfUserId: Int = getColumnIndexOrThrow(_stmt, "userId")
        val _cursorIndexOfCategoryId: Int = getColumnIndexOrThrow(_stmt, "categoryId")
        val _cursorIndexOfAmount: Int = getColumnIndexOrThrow(_stmt, "amount")
        val _cursorIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _cursorIndexOfDate: Int = getColumnIndexOrThrow(_stmt, "date")
        val _cursorIndexOfStartTime: Int = getColumnIndexOrThrow(_stmt, "startTime")
        val _cursorIndexOfEndTime: Int = getColumnIndexOrThrow(_stmt, "endTime")
        val _cursorIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _cursorIndexOfPhotoPath: Int = getColumnIndexOrThrow(_stmt, "photoPath")
        val _cursorIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _result: MutableList<Expense> = mutableListOf()
        while (_stmt.step()) {
          val _item: Expense
          val _tmpId: Long
          _tmpId = _stmt.getLong(_cursorIndexOfId)
          val _tmpUserId: Long
          _tmpUserId = _stmt.getLong(_cursorIndexOfUserId)
          val _tmpCategoryId: Long?
          if (_stmt.isNull(_cursorIndexOfCategoryId)) {
            _tmpCategoryId = null
          } else {
            _tmpCategoryId = _stmt.getLong(_cursorIndexOfCategoryId)
          }
          val _tmpAmount: Double
          _tmpAmount = _stmt.getDouble(_cursorIndexOfAmount)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_cursorIndexOfDescription)
          val _tmpDate: String
          _tmpDate = _stmt.getText(_cursorIndexOfDate)
          val _tmpStartTime: String
          _tmpStartTime = _stmt.getText(_cursorIndexOfStartTime)
          val _tmpEndTime: String
          _tmpEndTime = _stmt.getText(_cursorIndexOfEndTime)
          val _tmpNotes: String
          _tmpNotes = _stmt.getText(_cursorIndexOfNotes)
          val _tmpPhotoPath: String?
          if (_stmt.isNull(_cursorIndexOfPhotoPath)) {
            _tmpPhotoPath = null
          } else {
            _tmpPhotoPath = _stmt.getText(_cursorIndexOfPhotoPath)
          }
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_cursorIndexOfCreatedAt)
          _item =
              Expense(_tmpId,_tmpUserId,_tmpCategoryId,_tmpAmount,_tmpDescription,_tmpDate,_tmpStartTime,_tmpEndTime,_tmpNotes,_tmpPhotoPath,_tmpCreatedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getCategorySpendingInRange(
    userId: Long,
    startDate: String,
    endDate: String,
  ): List<CategorySpending> {
    val _sql: String = """
        |
        |        SELECT 
        |            e.categoryId,
        |            c.name AS categoryName,
        |            SUM(e.amount) AS totalAmount,
        |            COUNT(e.id) AS expenseCount
        |        FROM expenses e
        |        LEFT JOIN categories c ON e.categoryId = c.id
        |        WHERE e.userId = ? AND e.date >= ? AND e.date <= ?
        |        GROUP BY e.categoryId
        |        ORDER BY totalAmount DESC
        |    
        """.trimMargin()
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, userId)
        _argIndex = 2
        _stmt.bindText(_argIndex, startDate)
        _argIndex = 3
        _stmt.bindText(_argIndex, endDate)
        val _cursorIndexOfCategoryId: Int = 0
        val _cursorIndexOfCategoryName: Int = 1
        val _cursorIndexOfTotalAmount: Int = 2
        val _cursorIndexOfExpenseCount: Int = 3
        val _result: MutableList<CategorySpending> = mutableListOf()
        while (_stmt.step()) {
          val _item: CategorySpending
          val _tmpCategoryId: Long?
          if (_stmt.isNull(_cursorIndexOfCategoryId)) {
            _tmpCategoryId = null
          } else {
            _tmpCategoryId = _stmt.getLong(_cursorIndexOfCategoryId)
          }
          val _tmpCategoryName: String?
          if (_stmt.isNull(_cursorIndexOfCategoryName)) {
            _tmpCategoryName = null
          } else {
            _tmpCategoryName = _stmt.getText(_cursorIndexOfCategoryName)
          }
          val _tmpTotalAmount: Double
          _tmpTotalAmount = _stmt.getDouble(_cursorIndexOfTotalAmount)
          val _tmpExpenseCount: Int
          _tmpExpenseCount = _stmt.getLong(_cursorIndexOfExpenseCount).toInt()
          _item = CategorySpending(_tmpCategoryId,_tmpCategoryName,_tmpTotalAmount,_tmpExpenseCount)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getTodaySpending(userId: Long, today: String): Double {
    val _sql: String = """
        |
        |        SELECT COALESCE(SUM(amount), 0) FROM expenses 
        |        WHERE userId = ? AND date = ?
        |    
        """.trimMargin()
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, userId)
        _argIndex = 2
        _stmt.bindText(_argIndex, today)
        val _result: Double
        if (_stmt.step()) {
          val _tmp: Double
          _tmp = _stmt.getDouble(0)
          _result = _tmp
        } else {
          _result = 0.0
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
