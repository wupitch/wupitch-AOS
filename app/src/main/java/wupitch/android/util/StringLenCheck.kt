package wupitch.android.util

fun checkKeywordLen(searchKeyword: String): String {
    return if(searchKeyword.length > 7) {
        searchKeyword.substring(0, 7) + "..."
    }else searchKeyword
}