package wupitch.android.util

fun isEndTimeFasterThanStart(startTime: String, endTime: String): Boolean {

    val startHourString = startTime.split(":")[0]
    val startMinString = startTime.split(":")[1]

    val startHourInt = startHourString.toInt()
    val startMinInt = startMinString.toInt()

    val endHourString = endTime.split(":")[0]
    val endMinString = endTime.split(":")[1]

    val endHourInt = endHourString.toInt()
    val endMinInt = endMinString.toInt()

    return startHourInt > endHourInt || startHourInt >= endHourInt && startMinInt >= endMinInt

}