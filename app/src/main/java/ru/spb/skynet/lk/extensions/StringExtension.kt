package ru.spb.skynet.lk.extensions

import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

fun Int.toFormattedDate(
    pattern: String = "dd.MM.yyyy \nHH:mm",
    zoneId: ZoneId = ZoneId.systemDefault(),
    locale: Locale = Locale.getDefault()
): String {
    return try {
        val millis = this.toLong() * 1000L

        val instant = Instant.ofEpochMilli(millis)
        val formatter = DateTimeFormatter.ofPattern(pattern, locale)
        instant.atZone(zoneId).format(formatter)
    } catch (e: Exception) {
        ""
    }
}

fun String.parseCustomTimestamp(): String {
    return try {
        val parts = this.split("+")
        val seconds = parts[0].toLong()
        val zoneOffsetString = "+" + parts[1]

        val instant = Instant.ofEpochSecond(seconds)

        val offset = ZoneOffset.of(zoneOffsetString)

        val dateTime = instant.atOffset(offset)

        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")

        dateTime.format(formatter)
    } catch (e: Exception) {
        this
    }
}