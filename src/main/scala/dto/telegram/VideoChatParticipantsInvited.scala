package ru.otus
package dto.telegram

import zio.json._

@jsonMemberNames(SnakeCase)
@jsonDerive
case class VideoChatParticipantsInvited(users: Seq[User])
