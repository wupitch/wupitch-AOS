package wupitch.android.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class NumberTransformation() : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return formatToWon(text)
    }
}

fun String.wonToNum() : Int {
    return this.replace(",", "").toInt()
}

fun String.formatToWon() : String {
    val num = this.replace(",", "").toInt()
    val formatter: DecimalFormat =
        DecimalFormat("#,###")
    return formatter.format(num)
}

fun formatToWon(text: AnnotatedString): TransformedText {
//    text = value.replace("[\$&+,:;=\\\\\\\\?@#|/'<>.^*()%!-]".toRegex(), "")
//    text = value.replace("\\s".toRegex(), "")
    val num = text.replace("[\$&+,:;=\\\\\\\\?@#|/'<>.^*()%!-]".toRegex(), "")
    val formatter: DecimalFormat =
        DecimalFormat("#,###") //NumberFormat.getInstance(Locale.US) as DecimalFormat


    val numberOffsetTranslator = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            if (offset <= 0) return offset
            if (offset <= 2) return offset +1
            if (offset <= 5) return offset +2
            if (offset <= 8) return offset +3
            if (offset <= 12) return offset +4
            return 16
        }

        override fun transformedToOriginal(offset: Int): Int {
            if (offset <=0) return offset
            if (offset <=3) return offset -1
            if (offset <=7) return offset -2
            if (offset <=11) return offset -3
            if (offset <=15) return offset -4
            return 11
        }
    }

    return TransformedText(AnnotatedString(formatter.format(num)), numberOffsetTranslator)
}