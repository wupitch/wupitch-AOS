package wupitch.android.domain.model

import androidx.annotation.DrawableRes


data class OnboardingContent(
    val title : String,
    val subtitle : String,
    @DrawableRes val imgDrawable : Int
)
