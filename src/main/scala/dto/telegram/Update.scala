package ru.otus
package dto.telegram

case class Update(
    updateId: Long,
    message: Option[Message],
    editedMessage: Option[Message],
    channelPost: Option[Message],
    editedChannelPost: Option[Message],
    businessConnection: Option[BusinessConnection],
    businessMessage: Option[Message],
    editedBusinessMessage: Option[Message],
    deletedBusinessMessages: Option[BusinessMessagesDeleted],
    messageReaction: Option[MessageReactionUpdated],
    messageReactionCount: Option[MessageReactionCountUpdated],
    inlineQuery: Option[InlineQuery],
    chosenInlineResult: Option[ChosenInlineResult],
    callbackQuery: Option[CallbackQuery],
    shippingQuery: Option[ShippingQuery],
    preCheckoutQuery: Option[PreCheckoutQuery],
    purchasedPaidMedia: Option[PaidMediaPurchased],
    poll: Option[Poll],
    pollAnswer: Option[PollAnswer],
    myChatMember: Option[ChatMemberUpdated],
    chatMember: Option[ChatMemberUpdated],
    chatJoinRequest: Option[ChatJoinRequest],
    chatBoost: Option[ChatBoostUpdated],
    removedChatBoost: Option[ChatBoostRemoved]
)
