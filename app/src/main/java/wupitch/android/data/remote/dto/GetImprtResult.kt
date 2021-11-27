package wupitch.android.data.remote.dto


data class GetImprtResult(
    val content: List<GetImprtContent>,
    val empty: Boolean,
    val first: Boolean,
    val last: Boolean,
    val number: Int,
    val numberOfElements: Int,
    val size: Int,
    val sort: Sort,
    val totalElements: Int,
    val totalPages: Int
)