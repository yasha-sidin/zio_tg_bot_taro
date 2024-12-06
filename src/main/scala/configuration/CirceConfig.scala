package ru.otus
package configuration

import io.circe.generic.extras.Configuration

object CirceConfig {
  implicit val customConfig: Configuration = Configuration.default.withSnakeCaseMemberNames
}