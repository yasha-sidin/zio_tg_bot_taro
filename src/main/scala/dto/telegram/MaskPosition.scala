package ru.otus
package dto.telegram

import dto.telegram.`type`.MaskPointType

case class MaskPosition(point: MaskPointType, xShift: Double, yShift: Double, scale: Double)
