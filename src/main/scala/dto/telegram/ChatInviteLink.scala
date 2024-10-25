package ru.otus
package dto.telegram

import zio.json._

@jsonMemberNames(SnakeCase)
@jsonDerive
case class ChatInviteLink(
    inviteLink: String,
    creator: User,
    createsJoinRequest: Boolean,
    isPrimary: Boolean,
    isRevoked: Boolean,
    name: Option[String],
    expirationDate: Option[Long],
    memberLimit: Option[Int],
    pendingJoinRequestCount: Option[Long]
)
