package wupitch.android.presentation.ui.main.my_activity.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberImagePainter
import wupitch.android.R

@Composable
fun GalleryImage(
    imageUrl: String,
    isLiked: Boolean,
    onClick: () -> Unit
) {
    ConstraintLayout(modifier = Modifier.height(112.dp).fillMaxWidth().padding(end = 6.dp, bottom = 6.dp)) {
        val (image, like) = createRefs()

        Image(
            modifier = Modifier
                .constrainAs(image) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }.fillMaxSize().clickable {
                    onClick()       //todo pass id
                },
            painter =
            rememberImagePainter(
                imageUrl,
                builder = {
                    placeholder(R.color.gray09)
                    build()
                }
            ),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

        if (isLiked) {
            Image(
                modifier = Modifier
                    .constrainAs(like) {
                        end.linkTo(parent.end, margin = 6.dp)
                        bottom.linkTo(parent.bottom, margin = 6.dp)
                    }
                    .size(24.dp),
                painter = painterResource(id = R.drawable.react_heart),
                contentDescription = null
            )
        }

    }

}