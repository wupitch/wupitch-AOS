package wupitch.android.presentation.ui.main.home.create_crew.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.flowlayout.FlowRow
import wupitch.android.domain.model.FilterItem
import wupitch.android.presentation.theme.Roboto
import wupitch.android.presentation.ui.components.ToggleBtn

@Composable
fun CreateCrewRepetitionLayout(
    filterItemList: List<FilterItem>,
    extraInfoListState : SnapshotStateList<Int>,
) {

    Column(Modifier.fillMaxWidth()) {

        FlowRow(
            modifier = Modifier
                .padding(top = 12.dp),
            mainAxisSpacing = 16.dp,
            crossAxisSpacing = 16.dp
        ) {
            filterItemList.forEachIndexed { index, item ->
                if(index < 3 || index > 4) {
                    ToggleBtn(
                        toggleState = item.state,
                        modifier = Modifier
                            .width(96.dp)
                            .height(48.dp),
                        textString = item.name
                    ) {
                        if (it) {
                            if(!extraInfoListState.contains(index)) {
                                extraInfoListState.add(index)
                            }
                        }else {
                            if(extraInfoListState.contains(index)) {
                                extraInfoListState.remove(index)
                            }
                        }
                    }
                }else {
                    ToggleBtn(
                        toggleState = item.state,
                        modifier = Modifier
                            .width(152.dp)
                            .height(48.dp),
                        textString = item.name
                    ) {
                        if (it) {
                            if(!extraInfoListState.contains(index)) {
                                extraInfoListState.add(index)
                            }
                        }else {
                            if(extraInfoListState.contains(index)) {
                                extraInfoListState.remove(index)
                            }
                        }
                    }
                }
            }
        }
    }
}