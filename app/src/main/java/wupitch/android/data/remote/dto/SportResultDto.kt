package wupitch.android.data.remote.dto

import wupitch.android.domain.model.FilterItem
import wupitch.android.domain.model.SportResult


data class SportResultDto(
    val name: String,
    val sportsId: Int
)

fun SportResultDto.toSportResult() : SportResult {
    return SportResult(
        name = name,
        sportsId = sportsId
    )
}

fun SportResultDto.toFilterItem() : FilterItem {
    return FilterItem(
        name = name
    )
}