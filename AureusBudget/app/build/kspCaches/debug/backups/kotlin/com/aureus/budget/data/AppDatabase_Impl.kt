package com.aureus.budget.`data`

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import com.aureus.budget.`data`.dao.BudgetGoalDao
import com.aureus.budget.`data`.dao.BudgetGoalDao_Impl
import com.aureus.budget.`data`.dao.CategoryDao
import com.aureus.budget.`data`.dao.CategoryDao_Impl
import com.aureus.budget.`data`.dao.ExpenseDao
import com.aureus.budget.`data`.dao.ExpenseDao_Impl
import com.aureus.budget.`data`.dao.UserDao
import com.aureus.budget.`data`.dao.UserDao_Impl
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class AppDatabase_Impl : AppDatabase() {
  private val _userDao: Lazy<UserDao> = lazy {
    UserDao_Impl(this)
  }


  private val _categoryDao: Lazy<CategoryDao> = lazy {
    CategoryDao_Impl(this)
  }


  private val _expenseDao: Lazy<ExpenseDao> = lazy {
    ExpenseDao_Impl(this)
  }


  private val _budgetGoalDao: Lazy<BudgetGoalDao> = lazy {
    BudgetGoalDao_Impl(this)
  }


  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(1,
        "75686d0cb4101f3b2992ca1a233d30ae", "4c3d0389fff886cfd502e6bab778a7bd") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `users` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `username` TEXT NOT NULL, `passwordHash` TEXT NOT NULL, `email` TEXT NOT NULL, `createdAt` INTEGER NOT NULL)")
        connection.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_users_username` ON `users` (`username`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `categories` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `name` TEXT NOT NULL, `colorHex` TEXT NOT NULL, `iconName` TEXT NOT NULL, `createdAt` INTEGER NOT NULL, FOREIGN KEY(`userId`) REFERENCES `users`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_categories_userId` ON `categories` (`userId`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `expenses` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `categoryId` INTEGER, `amount` REAL NOT NULL, `description` TEXT NOT NULL, `date` TEXT NOT NULL, `startTime` TEXT NOT NULL, `endTime` TEXT NOT NULL, `notes` TEXT NOT NULL, `photoPath` TEXT, `createdAt` INTEGER NOT NULL, FOREIGN KEY(`userId`) REFERENCES `users`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`categoryId`) REFERENCES `categories`(`id`) ON UPDATE NO ACTION ON DELETE SET NULL )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_expenses_userId` ON `expenses` (`userId`)")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_expenses_categoryId` ON `expenses` (`categoryId`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `budget_goals` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` INTEGER NOT NULL, `minGoal` REAL NOT NULL, `maxGoal` REAL NOT NULL, `month` INTEGER NOT NULL, `year` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, FOREIGN KEY(`userId`) REFERENCES `users`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_budget_goals_userId` ON `budget_goals` (`userId`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '75686d0cb4101f3b2992ca1a233d30ae')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `users`")
        connection.execSQL("DROP TABLE IF EXISTS `categories`")
        connection.execSQL("DROP TABLE IF EXISTS `expenses`")
        connection.execSQL("DROP TABLE IF EXISTS `budget_goals`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        connection.execSQL("PRAGMA foreign_keys = ON")
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection):
          RoomOpenDelegate.ValidationResult {
        val _columnsUsers: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsUsers.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUsers.put("username", TableInfo.Column("username", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUsers.put("passwordHash", TableInfo.Column("passwordHash", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUsers.put("email", TableInfo.Column("email", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUsers.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysUsers: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesUsers: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesUsers.add(TableInfo.Index("index_users_username", true, listOf("username"),
            listOf("ASC")))
        val _infoUsers: TableInfo = TableInfo("users", _columnsUsers, _foreignKeysUsers,
            _indicesUsers)
        val _existingUsers: TableInfo = read(connection, "users")
        if (!_infoUsers.equals(_existingUsers)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |users(com.aureus.budget.data.entity.User).
              | Expected:
              |""".trimMargin() + _infoUsers + """
              |
              | Found:
              |""".trimMargin() + _existingUsers)
        }
        val _columnsCategories: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsCategories.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCategories.put("userId", TableInfo.Column("userId", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCategories.put("name", TableInfo.Column("name", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCategories.put("colorHex", TableInfo.Column("colorHex", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCategories.put("iconName", TableInfo.Column("iconName", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsCategories.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysCategories: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysCategories.add(TableInfo.ForeignKey("users", "CASCADE", "NO ACTION",
            listOf("userId"), listOf("id")))
        val _indicesCategories: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesCategories.add(TableInfo.Index("index_categories_userId", false, listOf("userId"),
            listOf("ASC")))
        val _infoCategories: TableInfo = TableInfo("categories", _columnsCategories,
            _foreignKeysCategories, _indicesCategories)
        val _existingCategories: TableInfo = read(connection, "categories")
        if (!_infoCategories.equals(_existingCategories)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |categories(com.aureus.budget.data.entity.Category).
              | Expected:
              |""".trimMargin() + _infoCategories + """
              |
              | Found:
              |""".trimMargin() + _existingCategories)
        }
        val _columnsExpenses: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsExpenses.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsExpenses.put("userId", TableInfo.Column("userId", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsExpenses.put("categoryId", TableInfo.Column("categoryId", "INTEGER", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsExpenses.put("amount", TableInfo.Column("amount", "REAL", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsExpenses.put("description", TableInfo.Column("description", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsExpenses.put("date", TableInfo.Column("date", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsExpenses.put("startTime", TableInfo.Column("startTime", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsExpenses.put("endTime", TableInfo.Column("endTime", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsExpenses.put("notes", TableInfo.Column("notes", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsExpenses.put("photoPath", TableInfo.Column("photoPath", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsExpenses.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysExpenses: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysExpenses.add(TableInfo.ForeignKey("users", "CASCADE", "NO ACTION",
            listOf("userId"), listOf("id")))
        _foreignKeysExpenses.add(TableInfo.ForeignKey("categories", "SET NULL", "NO ACTION",
            listOf("categoryId"), listOf("id")))
        val _indicesExpenses: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesExpenses.add(TableInfo.Index("index_expenses_userId", false, listOf("userId"),
            listOf("ASC")))
        _indicesExpenses.add(TableInfo.Index("index_expenses_categoryId", false,
            listOf("categoryId"), listOf("ASC")))
        val _infoExpenses: TableInfo = TableInfo("expenses", _columnsExpenses, _foreignKeysExpenses,
            _indicesExpenses)
        val _existingExpenses: TableInfo = read(connection, "expenses")
        if (!_infoExpenses.equals(_existingExpenses)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |expenses(com.aureus.budget.data.entity.Expense).
              | Expected:
              |""".trimMargin() + _infoExpenses + """
              |
              | Found:
              |""".trimMargin() + _existingExpenses)
        }
        val _columnsBudgetGoals: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsBudgetGoals.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsBudgetGoals.put("userId", TableInfo.Column("userId", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsBudgetGoals.put("minGoal", TableInfo.Column("minGoal", "REAL", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsBudgetGoals.put("maxGoal", TableInfo.Column("maxGoal", "REAL", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsBudgetGoals.put("month", TableInfo.Column("month", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsBudgetGoals.put("year", TableInfo.Column("year", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsBudgetGoals.put("updatedAt", TableInfo.Column("updatedAt", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysBudgetGoals: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysBudgetGoals.add(TableInfo.ForeignKey("users", "CASCADE", "NO ACTION",
            listOf("userId"), listOf("id")))
        val _indicesBudgetGoals: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesBudgetGoals.add(TableInfo.Index("index_budget_goals_userId", false,
            listOf("userId"), listOf("ASC")))
        val _infoBudgetGoals: TableInfo = TableInfo("budget_goals", _columnsBudgetGoals,
            _foreignKeysBudgetGoals, _indicesBudgetGoals)
        val _existingBudgetGoals: TableInfo = read(connection, "budget_goals")
        if (!_infoBudgetGoals.equals(_existingBudgetGoals)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |budget_goals(com.aureus.budget.data.entity.BudgetGoal).
              | Expected:
              |""".trimMargin() + _infoBudgetGoals + """
              |
              | Found:
              |""".trimMargin() + _existingBudgetGoals)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "users", "categories",
        "expenses", "budget_goals")
  }

  public override fun clearAllTables() {
    super.performClear(true, "users", "categories", "expenses", "budget_goals")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(UserDao::class, UserDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(CategoryDao::class, CategoryDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(ExpenseDao::class, ExpenseDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(BudgetGoalDao::class, BudgetGoalDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override
      fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>):
      List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun userDao(): UserDao = _userDao.value

  public override fun categoryDao(): CategoryDao = _categoryDao.value

  public override fun expenseDao(): ExpenseDao = _expenseDao.value

  public override fun budgetGoalDao(): BudgetGoalDao = _budgetGoalDao.value
}
