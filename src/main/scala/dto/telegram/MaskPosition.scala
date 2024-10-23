package ru.otus
package dto.telegram

import dto.telegram.`type`.MaskPointType
import zio.json._

@jsonMemberNames(SnakeCase)
@jsonDerive
case class MaskPosition(point: MaskPointType, xShift: Double, yShift: Double, scale: Double)
