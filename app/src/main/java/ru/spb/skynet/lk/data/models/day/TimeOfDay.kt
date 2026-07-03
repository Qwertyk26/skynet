package ru.spb.skynet.lk.data.models.day

import java.util.Calendar

enum class TimeOfDay {
    MORNING, DAY, EVENING, NIGHT
}

fun getTimeOfDay(): TimeOfDay {
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    return when (hour) {
        in 6..11 -> TimeOfDay.MORNING
        in 12..17 -> TimeOfDay.DAY
        in 18..23 -> TimeOfDay.EVENING
        else -> TimeOfDay.NIGHT
    }
}