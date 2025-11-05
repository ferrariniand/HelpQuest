package com.helpquest.core.data.mappers

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.helpquest.core.data.dto.ParticipantDto
import com.helpquest.core.domain.models.Participant
import kotlin.test.Test

class ParticipantMapperTest {

    @Test
    fun `ParticipantDto to Participant`() {
        val input = ParticipantDto(
            userId = "id1",
            username = "primo",
            profilePictureUrl = "test",
            showParticipantIdentity = true,
            classImageUrl = "test",
        )

        val result = Participant(
            userId = "id1",
            username = "primo",
            profilePictureUrl = "test",
            showParticipantIdentity = true,
            classImageUrl = "test",
        )

        assertThat(input.toParticipant()).isEqualTo(result)
    }

}
