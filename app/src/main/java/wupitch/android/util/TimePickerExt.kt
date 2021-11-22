package wupitch.android.util

import android.annotation.SuppressLint
import android.widget.NumberPicker
import android.widget.TimePicker

const val DEFAULT_INTERVAL = 15
const val MINUTES_MIN = 0
const val MINUTES_MAX = 60

@SuppressLint("PrivateApi")
fun TimePicker.setTimeInterval(
    timeInterval: Int = DEFAULT_INTERVAL
) {
    try {
        val classForId = Class.forName("com.android.internal.R\$id")
        val fieldId = classForId.getField("minute").getInt(null)

        (this.findViewById(fieldId) as NumberPicker).apply {
            minValue = MINUTES_MIN
            maxValue = MINUTES_MAX / timeInterval - 1
            displayedValues = getDisplayedValue(timeInterval)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

private fun getDisplayedValue(
    timeInterval: Int = DEFAULT_INTERVAL
): Array<String> {
    val minutesArray = ArrayList<String>()
    for (i in 0 until MINUTES_MAX step timeInterval) {
        minutesArray.add(i.toString())
    }

    return minutesArray.toArray(arrayOf(""))
}

fun TimePicker.getDisplayedMinute(
    timeInterval: Int = DEFAULT_INTERVAL
): Int = minute * timeInterval