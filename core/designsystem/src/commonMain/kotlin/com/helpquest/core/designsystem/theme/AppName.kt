package com.helpquest.core.designsystem.theme

import androidx.compose.runtime.Composable
import com.helpquest.core.designsystem.BuildKonfig
import com.helpquest.core.designsystem.Environment
import helpquest.core.designsystem.generated.resources.Res
import helpquest.core.designsystem.generated.resources.app_name
import helpquest.core.designsystem.generated.resources.app_name_dev
import helpquest.core.designsystem.generated.resources.app_name_mock
import helpquest.core.designsystem.generated.resources.app_name_stage
import org.jetbrains.compose.resources.stringResource

@Composable
fun appName(): String = when (BuildKonfig.FLAVOR_ENV) {
    Environment.Mock.name -> stringResource(Res.string.app_name_mock)
    Environment.Dev.name -> stringResource(Res.string.app_name_dev)
    Environment.Stage.name -> stringResource(Res.string.app_name_stage)
    else -> stringResource(Res.string.app_name)
}