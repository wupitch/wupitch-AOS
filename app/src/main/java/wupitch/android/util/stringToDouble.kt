package wupitch.android.util

fun String.stringToDouble() : Double {
    val hour = this.split(":")[0].toDouble()
    val min = this.split(":")[1].toDouble() / 100
    return hour + min
}