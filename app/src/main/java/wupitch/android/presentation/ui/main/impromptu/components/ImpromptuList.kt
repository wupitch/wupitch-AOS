package wupitch.android.presentation.ui.main.impromptu.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import wupitch.android.domain.model.CrewCardInfo
import wupitch.android.domain.model.ImpromptuCardInfo

@Composable
fun ImpromptuList(
    modifier: Modifier,
    list: List<ImpromptuCardInfo>,
    navigationToCrewDetailScreen: (Int) -> Unit
) {
    Box(modifier = modifier.background(Color.White)) {

        LazyColumn {
            itemsIndexed(
                items = list
            ) { index, item ->
                //todo : get next page.
                ImpromptuCard(cardInfo = item) {
                    navigationToCrewDetailScreen(item.id)
                }
            }
        }
    }
}