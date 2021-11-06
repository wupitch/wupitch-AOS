package wupitch.android.data.remote.dto

import wupitch.android.domain.model.KakaoLoginResult


data class KakaoLoginRes(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: KakaoLoginResult
)