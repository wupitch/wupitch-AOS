package wupitch.android.util

import java.text.DecimalFormat

fun convertedFee(guestDues: Int?):String{
    val formatter: DecimalFormat =
        DecimalFormat("#,###")
    return "참여비 ${formatter.format(guestDues)}원"
}