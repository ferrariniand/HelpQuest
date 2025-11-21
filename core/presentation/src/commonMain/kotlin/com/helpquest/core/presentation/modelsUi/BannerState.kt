package com.helpquest.core.presentation.modelsUi

import com.helpquest.core.presentation.util.UiText

data class BannerState(
    val bannerUiText: UiText? = null,
    val isVisible: Boolean = false
)
