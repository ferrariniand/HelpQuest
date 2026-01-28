package com.helpquest.core.data.service

import com.helpquest.core.domain.models.Class
import com.helpquest.core.domain.models.Participant
import com.helpquest.core.domain.models.SubClass

object MockCoreResponseElements {

    val participantFull = Participant(
        userId = "id1",
        username = "primo",
        profilePictureUrl = "test",
        showParticipantIdentity = true,
        participantClass = Class.VILLAGER,
    )

    val participantNoClass = Participant(
        userId = "id2",
        username = "secondo",
        profilePictureUrl = "test",
        showParticipantIdentity = true,
    )

    val participantNoImage = Participant(
        userId = "id3",
        username = "terzo",
        profilePictureUrl = null,
        showParticipantIdentity = true,
        participantClass = Class.TECH_WIZARD,
        participantSubClass = SubClass.SOFTWARE_MAGE,
    )

    val participantDontShowID = Participant(
        userId = "id4",
        username = "quarto",
        profilePictureUrl = "test",
        showParticipantIdentity = false,
        participantClass = Class.VILLAGER,
    )

    val participantNoImageDontShowID = Participant(
        userId = "id5",
        username = "quinto",
        profilePictureUrl = null,
        showParticipantIdentity = false,
        participantClass = Class.VILLAGER,
    )

    val participantList = listOf(
        participantFull,
        participantNoClass,
        participantNoImage,
    )

    val allPossibleParticipants = listOf(
        participantFull,
        participantNoClass,
        participantNoImage,
        participantDontShowID,
        participantNoImageDontShowID
    )
}