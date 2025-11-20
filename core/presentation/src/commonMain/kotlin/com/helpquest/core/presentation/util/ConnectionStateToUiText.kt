package com.helpquest.core.presentation.util

import com.helpquest.core.domain.util.ConnectionState
import helpquest.core.presentation.generated.resources.Res
import helpquest.core.presentation.generated.resources.error_network
import helpquest.core.presentation.generated.resources.error_unknown_short
import helpquest.core.presentation.generated.resources.offline
import helpquest.core.presentation.generated.resources.online
import helpquest.core.presentation.generated.resources.reconnecting


fun ConnectionState.toUiText(): UiText {
    val resource = when (this) {
        ConnectionState.DISCONNECTED -> Res.string.offline
        ConnectionState.CONNECTING -> Res.string.reconnecting
        ConnectionState.CONNECTED -> Res.string.online
        ConnectionState.ERROR_NETWORK -> Res.string.error_network
        ConnectionState.ERROR_UNKNOWN -> Res.string.error_unknown_short
    }
    return UiText.Resource(resource)
}