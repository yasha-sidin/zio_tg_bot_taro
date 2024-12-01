package ru.otus
package configuration

import io.circe
import io.circe.generic.extras.Configuration

object CirceConfig {
  type CirceError = circe.Error
  implicit val customConfig: Configuration = Configuration.default.withSnakeCaseMemberNames
}