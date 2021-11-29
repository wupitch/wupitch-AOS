package wupitch.android.util

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class MoneyVT : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {






        val trimmed = if (text.text.length >= 7) text.text.substring(0..6) else text.text
        var output = ""

        if(trimmed.length <=3 ) {
            val noTranslator = object  : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    return offset
                }

                override fun transformedToOriginal(offset: Int): Int {
                    return offset
                }
            }
            return TransformedText(AnnotatedString(trimmed), noTranslator)
        }
        else if (trimmed.length ==4) {
            for (i in trimmed.indices) {
                output += trimmed[i]
                if (i==0) output +=","
            }

            val lenFourTranslator = object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    // [offset [0 - 2] remain the same]
                    if (offset <= 0) return offset
//                else return offset +1
                    // [3 - 6] transformed to [4 - 7] respectively
//                    if (offset <= 3) return offset + 1

                    return 5
                }

                override fun transformedToOriginal(offset: Int): Int {

                    if (offset <= 0) return offset
                    if (offset <= 4) return offset -1
                    return 4
                }
            }
            return TransformedText(AnnotatedString(trimmed), lenFourTranslator)
        }else if(trimmed.length ==5) {
            for (i in trimmed.indices) {
                output += trimmed[i]
                if (i==1) output +=","
            }
            val lenFiveTranslator = object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    // [offset [0 - 2] remain the same]
                    if (offset <= 1) return offset
                    // [3 - 6] transformed to [4 - 7] respectively
                    if (offset <= 4) return offset + 1

                    return 6
                }

                override fun transformedToOriginal(offset: Int): Int {

                    if (offset <= 1) return offset
                    if (offset <= 5) return offset -1
                    return 5
                }
            }
            return TransformedText(AnnotatedString(trimmed), lenFiveTranslator)

        }else {
            for (i in trimmed.indices) {
                output += trimmed[i]
                if (i==2) output +=","
            }
            val lenSixTranslator = object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    // [offset [0 - 2] remain the same]
                    if (offset <= 2) return offset
                    // [3 - 6] transformed to [4 - 7] respectively
                    if (offset <= 5) return offset + 1

                    return 7
                }

                override fun transformedToOriginal(offset: Int): Int {

                    if (offset <= 2) return offset
                    if (offset <= 6) return offset -1
                    return 6
                }
            }
            return TransformedText(AnnotatedString(trimmed), lenSixTranslator)

        }





    }

}