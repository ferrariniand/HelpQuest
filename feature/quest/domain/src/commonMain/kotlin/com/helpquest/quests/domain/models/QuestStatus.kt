package com.helpquest.quests.domain.models

enum class QuestStatus {
    OPEN,
    READY_FOR_REFINEMENT,
    IN_REFINEMENT,
    IN_PROGRESS,
    ON_HOLD,
    MORE_HELP_REQUIRED,
    READY_FOR_VERIFICATION,
    IN_VERIFICATION,
    COMPLETED,
    REJECTED
}