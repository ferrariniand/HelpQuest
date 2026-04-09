package com.helpquest.core.data.mappers

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.helpquest.core.data.dto.ParticipantDto
import com.helpquest.core.domain.models.Class
import com.helpquest.core.domain.models.Participant
import com.helpquest.core.domain.models.SubClass
import kotlin.test.Test

class ParticipantMapperTest {

    @Test
    fun `ParticipantDto to Participant`() {
        val input = ParticipantDto(
            userId = "id1",
            username = "primo",
            profilePictureUrl = "test",
            showParticipantIdentity = true,
            participantClassId = Class.TECH_WIZARD.classId,
            participantSubClassId = SubClass.SOFTWARE_MAGE.subClassId,
        )

        val result = Participant(
            userId = "id1",
            username = "primo",
            profilePictureUrl = "test",
            showParticipantIdentity = true,
            participantClass = Class.TECH_WIZARD,
            participantSubClass = SubClass.SOFTWARE_MAGE,
        )

        assertThat(input.toParticipant()).isEqualTo(result)
    }

}
