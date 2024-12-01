package ru.otus
package dto.telegram

sealed trait ChatMember

case class ChatMemberAdministrator(
    user: User,
    canBeEdited: Boolean,
    isAnonymous: Boolean,
    canManageChat: Boolean,
    canDeleteMessages: Boolean,
    canManageVideoChats: Boolean,
    canRestrictMembers: Boolean,
    canPromoteMembers: Boolean,
    canChangeInfo: Boolean,
    canInviteUsers: Boolean,
    canPostMessages: Option[Boolean],
    canEditMessages: Option[Boolean],
    canPinMessages: Option[Boolean],
    canManageTopics: Option[Boolean],
    customerTitle: Option[String]
) extends ChatMember

case class ChatMemberBanned(user: User, untilDate: Int) extends ChatMember

case class ChatMemberLeft(user: User) extends ChatMember

case class ChatMemberMember(user: User) extends ChatMember

case class ChatMemberOwner(user: User, isAnonymous: Boolean, customTitle: Option[String])
    extends ChatMember

case class ChatMemberRestricted(
    user: User,
    isMember: Boolean,
    canChangeInfo: Boolean,
    canInviteUsers: Boolean,
    canPinMembers: Boolean,
    canManageTopics: Boolean,
    canSendMessages: Boolean,
    canSendMediaMessages: Boolean,
    canSendPolls: Boolean,
    canSendOtherMessages: Boolean,
    canAddWebPagePreviews: Boolean,
    untilDate: Int
) extends ChatMember
