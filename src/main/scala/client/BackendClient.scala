package ru.otus
package client

import dto.admin.{ AppointmentDate, BookDateRequest, BookingDateBody, Constant, ErrorBody }
import util.ClientUtil._
import configuration.{ BackendConfig, Configuration }
import configuration.CirceConfig._

import zio.http.Client
import zio.{ &, RIO, Scope, ZIO, ZLayer }
import zio.macros.accessible
import io.circe.generic.extras.auto._
import dto.admin.`type`.AppConstant

import java.util.UUID

@accessible
object BackendClient {
  type BackendClient = Service

  trait Service {
    def getAvailableDates: RIO[Client & Scope, Option[Either[ErrorBody, List[AppointmentDate]]]]
    def getConstants: RIO[Client & Scope, Option[Either[ErrorBody, List[Constant]]]]
    def getUserBookings(chatId: Long): RIO[Client & Scope, Option[Either[ErrorBody, List[BookingDateBody]]]]
    def bookDate(bookDateRequest: BookDateRequest): RIO[Client & Scope, Option[Either[ErrorBody, BookingDateBody]]]
    def cancelBooking(bookingId: UUID): RIO[Client & Scope, Option[Either[ErrorBody, BookingDateBody]]]
    def getConstantByKey(key: AppConstant): RIO[Client & Scope, Option[Either[ErrorBody, Constant]]]
  }

  class ServiceImpl(val backendConfig: BackendConfig) extends Service {
    override def getAvailableDates: RIO[Client & Scope, Option[Either[ErrorBody, List[AppointmentDate]]]] =
      requestGet[List[AppointmentDate], ErrorBody](s"${backendConfig.url}/api/appointment_date/?status=Available", logResponse = true)

    override def getConstants: RIO[Client & Scope, Option[Either[ErrorBody, List[Constant]]]] =
      requestGet[List[Constant], ErrorBody](s"${backendConfig.url}/api/constant/", logResponse = true)

    override def getUserBookings(chatId: Long): RIO[Client & Scope, Option[Either[ErrorBody, List[BookingDateBody]]]] =
      requestGet[List[BookingDateBody], ErrorBody](s"${backendConfig.url}/api/booking/?chat_id=$chatId", logResponse = true)

    override def bookDate(bookDateRequest: BookDateRequest): RIO[Client & Scope, Option[Either[ErrorBody, BookingDateBody]]] =
      requestPostWithError[BookDateRequest, BookingDateBody, ErrorBody](s"${backendConfig.url}/api/booking/", bookDateRequest, logResponse = true, logPostBody = false)

    override def cancelBooking(bookingId: UUID): RIO[Client & Scope, Option[Either[ErrorBody, BookingDateBody]]] =
      requestDelete[BookingDateBody, ErrorBody](s"${backendConfig.url}/api/booking/$bookingId/", logResponse = true)

    override def getConstantByKey(key: AppConstant): RIO[Client & Scope, Option[Either[ErrorBody, Constant]]] =
      requestGet[Constant, ErrorBody](s"${backendConfig.url}/api/constant/$key/", logResponse = true)
  }

  val live: ZLayer[Configuration, Nothing, BackendClient] = ZLayer {
    for {
      config <- ZIO.service[Configuration]
    } yield new ServiceImpl(config.backend)
  }
}
