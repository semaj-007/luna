package com.aureus.budget.utils

import java.security.MessageDigest
import java.text.NumberFormat
import java.util.Locale

object CurrencyFormatter {
    private val format: NumberFormat = NumberFormat.getNumberInstance(Locale("en", "ZA")).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }

    fun format(amount: Double): String = "R ${format.format(amount)}"

    fun formatCompact(amount: Double): String {
        return when {
            amount >= 1_000_000 -> "R${format.format(amount / 1_000_000)}M"
            amount >= 1_000 -> "R${format.format(amount / 1_000)}K"
            else -> format(amount)
        }
    }

    fun parse(text: String): Double? {
        return try {
            text.replace(Regex("[^0-9.,]"), "")
                .replace(",", ".")
                .toDoubleOrNull()
        } catch (e: Exception) {
            null
        }
    }
}

object PasswordHasher {
    fun hash(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    fun verify(password: String, hash: String): Boolean = hash(password) == hash
}

object DateUtils {
    fun getCurrentDate(): String {
        val cal = java.util.Calendar.getInstance()
        return "%04d-%02d-%02d".format(
            cal.get(java.util.Calendar.YEAR),
            cal.get(java.util.Calendar.MONTH) + 1,
            cal.get(java.util.Calendar.DAY_OF_MONTH)
        )
    }

    fun getCurrentTime(): String {
        val cal = java.util.Calendar.getInstance()
        return "%02d:%02d".format(
            cal.get(java.util.Calendar.HOUR_OF_DAY),
            cal.get(java.util.Calendar.MINUTE)
        )
    }

    fun getCurrentMonth(): Int = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH) + 1
    fun getCurrentYear(): Int = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)

    fun getMonthStartDate(month: Int, year: Int): String =
        "%04d-%02d-01".format(year, month)

    fun getMonthEndDate(month: Int, year: Int): String {
        val cal = java.util.Calendar.getInstance()
        cal.set(year, month - 1, 1)
        val lastDay = cal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH)
        return "%04d-%02d-%02d".format(year, month, lastDay)
    }

    fun getMonthYearLabel(month: Int, year: Int): String {
        val monthNames = arrayOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )
        return "${monthNames[month - 1]} $year"
    }

    fun formatDisplayDate(date: String): String {
        return try {
            val parts = date.split("-")
            val monthNames = arrayOf(
                "Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
            )
            val month = parts[1].toInt() - 1
            "${parts[2]} ${monthNames[month]} ${parts[0]}"
        } catch (e: Exception) {
            date
        }
    }

    fun isValidDateRange(startDate: String, endDate: String): Boolean =
        startDate <= endDate

    fun isValidTimeRange(startTime: String, endTime: String): Boolean =
        startTime < endTime
}

object ColorUtils {
    fun parseColor(hex: String): Int {
        return try {
            android.graphics.Color.parseColor(hex)
        } catch (e: Exception) {
            android.graphics.Color.parseColor("#1B3A73")
        }
    }

    // Predefined colors for categories
    val categoryColors = listOf(
        "#1B3A73" to "Navy Blue",
        "#2E7D32" to "Forest Green",
        "#E65100" to "Deep Orange",
        "#6A1B9A" to "Purple",
        "#B71C1C" to "Red",
        "#00695C" to "Teal",
        "#AD1457" to "Pink",
        "#4E342E" to "Brown",
        "#37474F" to "Blue Grey",
        "#F57F17" to "Amber",
        "#1565C0" to "Blue",
        "#00838F" to "Cyan"
    )
}
