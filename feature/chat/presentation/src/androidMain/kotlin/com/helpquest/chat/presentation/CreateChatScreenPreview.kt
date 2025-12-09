package com.helpquest.chat.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.helpquest.chat.presentation.create_manage_chat.ManageChatScreen
import com.helpquest.chat.presentation.create_manage_chat.ManageChatState
import com.helpquest.core.designsystem.components.selection_sections.SearchResult
import com.helpquest.core.designsystem.theme.HelpQuestTheme
import com.helpquest.core.presentation.modelsUi.ParticipantUi

val participantUiFull = ParticipantUi(
    id = "id1",
    username = "primo",
    initials = "ST",
    imageUrl = "test",
    showParticipantIdentity = true,
    classImageUrl = "test"
)

val participantUiNoClass = ParticipantUi(
    id = "id2",
    username = "secondo",
    initials = "ND",
    imageUrl = "test",
    showParticipantIdentity = true,
)

val participantUiNoImage = ParticipantUi(
    id = "id3",
    username = "terzo",
    initials = "RD",
    showParticipantIdentity = true,
)

val participantUiDontShowID = ParticipantUi(
    id = "id4",
    username = "quarto",
    initials = "TH",
    imageUrl = "test",
)

val participantUiNoImageDontShowID = ParticipantUi(
    id = "id5",
    username = "quinto",
    initials = "FH",
)

val participantList = listOf(
    participantUiFull,
    participantUiNoClass,
    participantUiNoImage,
    participantUiDontShowID,
    participantUiNoImageDontShowID,
)

@Composable
@PreviewLightDark
@PreviewScreenSizes
private fun CreateChatScreenFailurePreview() {
    HelpQuestTheme {
        ManageChatScreen(
            headerText = "Create Chat",
            primaryButtonText = "Create Chat",
            state = ManageChatState(
                selectedChatParticipants = participantList,
                currentSearchResult = SearchResult.Success(
                    listOf(
                        participantUiDontShowID,
                        participantUiNoImage
                    )
                )
            ),
            onAction = {},
        )
    }
}