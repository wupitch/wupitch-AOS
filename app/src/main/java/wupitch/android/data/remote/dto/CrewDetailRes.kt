package wupitch.android.data.remote.dto


data class CrewDetailRes(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: CrewDetailResultDto
)