package wupitch.android.util

import java.text.SimpleDateFormat
import java.util.*

fun dateFormatter(milliseconds : Long?) : String? {
    milliseconds?.let{
        return if(!checkIfDateValid(milliseconds)) null
        else {
            val formatter = SimpleDateFormat("yyyy.MM.dd", Locale.KOREAN)
            val calendar: Calendar = Calendar.getInstance()
            calendar.timeInMillis = it
            val day = koreanDay(calendar.get(Calendar.DAY_OF_WEEK))

            formatter.format(calendar.time) +" ($day)"
        }
    }
    return null
}

fun koreanDay (day : Int) : String {
    return when(day) {
        1 -> "일"
        2-> "월"
        3-> "화"
        4-> "수"
        5->"목"
        6->"금"
        else -> "토"
    }
}

fun checkIfDateValid(milliseconds: Long) : Boolean {
    val chosenCalendar : Calendar = Calendar.getInstance()
    chosenCalendar.timeInMillis = milliseconds

    val futureCalendar: Calendar = Calendar.getInstance()
    futureCalendar.add(Calendar.WEEK_OF_YEAR, +4)
    val maxDate = futureCalendar.timeInMillis

    val currentCalendar: Calendar = Calendar.getInstance()

    return milliseconds < maxDate && chosenCalendar.timeInMillis >= currentCalendar.timeInMillis
}

fun dateDashToCol(date : String) : String {
    val splitted = date.split("-")
    val year = splitted[0].toInt() - 2000
    val month = splitted[1]
    val day= splitted[2]
    return "$year.$month.$day"
}