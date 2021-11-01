package wupitch.android.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import wupitch.android.R

@Composable
fun GrayDivider () {
    Spacer(modifier = Modifier
        .background(colorResource(id = R.color.gray04))
        .height(8.dp)
        .fillMaxWidth()
    )
}