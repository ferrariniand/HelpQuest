package com.helpquest.presentation.mappers

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.helpquest.core.domain.models.Participant
import com.helpquest.core.presentation.mappers.toParticipantUi
import com.helpquest.core.presentation.modelsUi.ParticipantUi
import kotlin.test.Test

class ParticipantMapperTest {

    @Test
    fun `Participant to ParticipantUi`() {
        val input = Participant(
            userId = "id1",
            username = "primo",
            profilePictureUrl = "test",
            showParticipantIdentity = true,
            classImageUrl = "test",
        )

        val result = ParticipantUi(
            id = "id1",
            username = "primo",
            initials = "PR",
            imageUrl = "test",
            showParticipantIdentity = true,
            classImageUrl = "test",
        )

        assertThat(input.toParticipantUi()).isEqualTo(result)
    }

}
