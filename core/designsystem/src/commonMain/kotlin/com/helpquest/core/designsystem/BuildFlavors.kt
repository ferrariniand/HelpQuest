package com.helpquest.core.designsystem

sealed class Environment(val name: String) {
    object Mock : Environment("mock")
    object Dev : Environment("dev")
    object Stage : Environment("stage")
    object Prod : Environment("prod")
}