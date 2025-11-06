package com.helpquest.core.designsystem.components.selection_sections

import com.helpquest.core.presentation.modelsUi.ParticipantUi

sealed class ParticipantSearchResult {
    data class Success(val participant: ParticipantUi) : ParticipantSearchResult()
    data object NotFound : ParticipantSearchResult()

    fun getParticipantUiOrNull(): ParticipantUi? = (this as? Success)?.participant

    fun isSuccess(): Boolean = this is Success
    fun isNotFound(): Boolean = this is NotFound

}