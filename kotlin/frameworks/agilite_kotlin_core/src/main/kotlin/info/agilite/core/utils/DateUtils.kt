package info.agilite.core.utils

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

object DateUtils {
    fun nowUTC(): LocalDateTime {
        return LocalDateTime.now(ZoneOffset.UTC)
    }

    fun millisUTC2TimeUTC(millisUTC: Long): LocalDateTime {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(millisUTC), ZoneOffset.UTC)
    }

    fun timeUTC(): Long {
        return LocalDateTime.now().atZone(ZoneOffset.UTC).toInstant().toEpochMilli()
    }

    fun timeUTC2millisUTC(timeUTC: LocalDateTime): Long {
        return timeUTC.atZone(ZoneOffset.UTC).toInstant().toEpochMilli()
    }

    fun convertToUTC(localDateTime: LocalDateTime): LocalDateTime {
        return localDateTime.atZone(ZoneOffset.systemDefault()).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime()
    }

    fun convertToLocalDefaultZone(localDateTime: LocalDateTime): LocalDateTime {
        return localDateTime.atZone(ZoneOffset.UTC).withZoneSameInstant(ZoneOffset.systemDefault()).toLocalDateTime()
    }

    fun dateDiff(ini: LocalDateTime, fim: LocalDateTime, unit: ChronoUnit): Long {
        return when (unit) {
            ChronoUnit.SECONDS -> unit.between(
                LocalDateTime.of(ini.year, ini.month, ini.dayOfMonth, ini.hour, ini.minute, ini.second, 0),
                LocalDateTime.of(fim.year, fim.month, fim.dayOfMonth, fim.hour, fim.minute, fim.second, 0)
            )

            ChronoUnit.MINUTES -> unit.between(
                LocalDateTime.of(ini.year, ini.month, ini.dayOfMonth, ini.hour, ini.minute, 0, 0),
                LocalDateTime.of(fim.year, fim.month, fim.dayOfMonth, fim.hour, fim.minute, 0, 0)
            )

            ChronoUnit.HOURS -> unit.between(
                LocalDateTime.of(ini.year, ini.month, ini.dayOfMonth, ini.hour, 0, 0, 0),
                LocalDateTime.of(fim.year, fim.month, fim.dayOfMonth, fim.hour, 0, 0, 0)
            )

            ChronoUnit.DAYS -> unit.between(
                LocalDateTime.of(ini.year, ini.month, ini.dayOfMonth, 1, 0, 0, 0),
                LocalDateTime.of(fim.year, fim.month, fim.dayOfMonth, 1, 0, 0, 0)
            )

            ChronoUnit.MONTHS -> unit.between(
                LocalDateTime.of(ini.year, ini.month, 1, 1, 0, 0, 0),
                LocalDateTime.of(fim.year, fim.month, 1, 1, 0, 0, 0)
            )

            ChronoUnit.YEARS -> unit.between(
                LocalDateTime.of(ini.year, 1, 1, 1, 0, 0, 0),
                LocalDateTime.of(fim.year, 1, 1, 1, 0, 0, 0)
            )

            else -> unit.between(ini, fim)
        }
    }

    fun formatNowToIso(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")
        return OffsetDateTime.now().format(formatter)
    }

    fun lastDayOfMonth(month: Int = LocalDate.now().monthValue, year: Int = LocalDate.now().year): LocalDate {
        return LocalDate.of(year, month, 1).plusMonths(1).minusDays(1)
    }

    fun lastUtilDayOfMonth(month: Int = LocalDate.now().monthValue, year: Int = LocalDate.now().year): LocalDate {
        var lastDay = lastDayOfMonth(month, year)
        while (lastDay.dayOfWeek.value > 5) {
            lastDay = lastDay.minusDays(1)
        }
        return lastDay
    }


}
