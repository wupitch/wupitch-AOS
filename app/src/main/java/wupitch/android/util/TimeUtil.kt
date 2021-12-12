package wupitch.android.util

fun String.stringToDouble() : Double {
    val hour = this.split(":")[0].toDouble()
    val min = this.split(":")[1].toDouble() / 100
    return hour + min
}

fun doubleToTime(time : Double) : String {
    var hour = time.toInt()
    var min = ((time -hour)*100).toInt().toString()
    if(hour==0) hour = 12
    if(min == "0") min = "00"
    return "$hour:$min"
}

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