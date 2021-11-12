package wupitch.android.presentation.ui.main.home.create_crew

data class DistrictState(
    val isLoading : Boolean = false,
    val data : Array<String> = emptyArray(),
    val error : String = ""
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DistrictState

        if (isLoading != other.isLoading) return false
        if (!data.contentEquals(other.data)) return false
        if (error != other.error) return false

        return true
    }

    override fun hashCode(): Int {
        var result = isLoading.hashCode()
        result = 31 * result + data.contentHashCode()
        result = 31 * result + error.hashCode()
        return result
    }
}
