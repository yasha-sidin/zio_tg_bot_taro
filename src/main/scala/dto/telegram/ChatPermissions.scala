package ru.otus
package dto.telegram

import zio.json._

@jsonMemberNames(SnakeCase)
@jsonDerive
case class ChatPermissions(
    canSendMessages: Option[Boolean],
    canSendMediaMessages: Option[Boolean],
    canSendPolls: Option[Boolean],
    canSendOtherMessages: Option[Boolean],
    canAddWebPagePreviews: Option[Boolean],
    canChangeInfo: Option[Boolean],
    canInviteUsers: Option[Boolean],
    canPinMessages: Option[Boolean],
    canManageTopics: Option[Boolean]
)