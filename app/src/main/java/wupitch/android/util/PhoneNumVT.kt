package wupitch.android.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class KoreanPhoneNumVT : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= 11) text.text.substring(0..10) else text.text
        var output = ""
        for (i in trimmed.indices) {
            output += trimmed[i]
            if (i==2 || i == 6) output +="-"
        }

        val koreanPhoneNumTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                // [offset [0 - 2] remain the same]
                if (offset <= 2) return offset
                // [3 - 6] transformed to [4 - 7] respectively
                if (offset <= 6) return offset + 1
                // [7 - 10] transformed to [9 - 12] respectively
                if (offset <= 10 ) return offset + 2
                return 13
            }

            override fun transformedToOriginal(offset: Int): Int {

                if (offset <= 2) return offset
                if (offset <= 7) return offset -1
                if (offset <= 12) return offset - 2
                return 11
            }
        }

        return TransformedText(
            AnnotatedString(output),
            koreanPhoneNumTranslator)

    }

}