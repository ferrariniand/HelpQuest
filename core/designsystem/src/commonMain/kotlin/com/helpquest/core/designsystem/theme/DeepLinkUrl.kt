package com.helpquest.core.designsystem.theme

import com.helpquest.core.designsystem.BuildKonfig
import com.helpquest.core.designsystem.Environment

fun deepLinkUrl(): String {
    val deepLinkPrefix = when (BuildKonfig.FLAVOR_ENV) {
        Environment.Prod.name -> ""
        else -> "${BuildKonfig.FLAVOR_ENV}."
    }
    return "${deepLinkPrefix}hq.com"
}