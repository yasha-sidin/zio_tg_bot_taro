package ru.otus
package dto.telegram

import scala.concurrent.duration.Duration

object Poll {}

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
    openPeriod: Option[Duration],
    closeDate: Option[Long]
)
