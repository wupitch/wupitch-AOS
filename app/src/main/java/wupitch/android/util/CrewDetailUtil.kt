package wupitch.android.util

import wupitch.android.data.remote.dto.Schedule
import java.text.DecimalFormat

fun convertedSchedule(schedules: List<Schedule>): List<String> {
    val schedule = arrayListOf<String>()
    schedules.forEach {
        schedule.add("${it.day} ${doubleToTime(it.startTime)} - ${doubleToTime(it.endTime)}")
    }
    return schedule.toList()
}

fun convertedCrewFee(dues: Int?, guestDues: Int?): List<String> {
    val list = arrayListOf<String>()
    val formatter: DecimalFormat =
        DecimalFormat("#,###")

    if (dues != null) {
        val formattedMoney = formatter.format(dues)
        list.add("정회원비 $formattedMoney 원")
    }
    if (guestDues != null) {
        val formattedMoney = formatter.format(guestDues)
        list.add("손님비 $formattedMoney 원")
    }

    return list.toList()
}

fun convertedAge(ageTable: List<String>): String {

    val stringBuilder = StringBuilder()
    ageTable.forEachIndexed { index, s ->
        if (index != ageTable.size - 1) {
            stringBuilder.append("$s, ")
        } else {
            stringBuilder.append(s)
        }
    }
    return stringBuilder.toString()
}