package wupitch.android.util

fun String.stringToDouble() : Double {
    val hour = this.split(":")[0].toDouble()
    val min = this.split(":")[1].toDouble() / 100
    return hour + min
}

fun doubleToTime(time : Double) : String {
    val hour = time.toInt()
    val min = (time -hour)*100
    return "$hour:$min"
}