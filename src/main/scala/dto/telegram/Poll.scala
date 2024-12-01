package ru.otus
package dto.telegram

import dto.telegram.`type`.PollType

case class Poll(
    id: String,
    question: String,
    options: Seq[PollOption],
    totalVoterCount: Long,
    isClosed: Boolean,
    isAnonymous: Boolean,
    `type`: PollType,
    allowsMultipleAnswers: Boolean,
    correctOptionId: Option[Long],
    explanation: Option[String],
    explanationEntities: Option[Seq[MessageEntity]],
    openPeriod: Option[Long],
    closeDate: Option[Long]
)
