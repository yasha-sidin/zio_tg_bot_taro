package ru.otus
package service

import client.BackendClient
import dto.admin.{AppointmentDate, BookDateRequest, BookingDateBody, Constant, ErrorBody}
import dto.admin.`type`.{AppConstant, BookingStatus}
import util.TimeUtil

import zio.{&, RIO, Scope, ULayer, ZIO, ZLayer}
import zio.http.Client
import zio.macros.accessible

import java.util.UUID
import scala.collection.MapView

@accessible
object BackendService {
  private type BackendService    = Service
  private type BackendServiceEnv = BackendClient.Service with Client & Scope

  trait Service {
    def getUserBookings(chatId: Long): RIO[BackendServiceEnv, Either[ErrorBody, List[BookingDateBody]]]
    def getBookingsToCancel(chatId: Long): RIO[BackendServiceEnv, Either[ErrorBody, List[BookingDateBody]]]
    def getAvailableDatesMap: RIO[BackendServiceEnv, Either[ErrorBody, MapView[String, MapView[String, List[(AppointmentDate, String)]]]]]
    def getConstantByKey(appConstant: AppConstant): RIO[BackendServiceEnv, Either[ErrorBody, Constant]]
    def cancelBooking(id: UUID): RIO[BackendServiceEnv, Either[ErrorBody, BookingDateBody]]
    def book(bookDateRequest: BookDateRequest): RIO[BackendServiceEnv, Either[ErrorBody, BookingDateBody]]
  }

  class ServiceImpl extends Service {

    override def getUserBookings(chatId: Long): RIO[BackendServiceEnv, Either[ErrorBody, List[BookingDateBody]]] =
      BackendClient.getUserBookings(chatId).some.orElseFail(new Throwable("Wrong answer from backend"))

    override def getBookingsToCancel(chatId: Long): RIO[BackendServiceEnv, Either[ErrorBody, List[BookingDateBody]]] =
      BackendClient.getUserBookings(chatId).some.orElseFail(new Throwable("Wrong answer from backend")).flatMap {
        case Right(bookings) =>
          ZIO
            .filterPar(bookings)(booking => ZIO.succeed(List(BookingStatus.Active, BookingStatus.Confirmed).contains(booking.status)))
            .map(Right(_))
        case Left(error)     =>
          ZIO.left(error)
      }

    override def getAvailableDatesMap: RIO[BackendServiceEnv, Either[ErrorBody, MapView[String, MapView[String, List[(AppointmentDate, String)]]]]] =
      BackendClient.getAvailableDates.some.orElseFail(new Throwable("Wrong answer from backend")).flatMap {
        case Right(availableDates) =>
          ZIO.succeed(TimeUtil.groupedDates(availableDates)).map(res => Right(res))
        case Left(error)     =>
          ZIO.left(error)
      }

    override def getConstantByKey(appConstant: AppConstant): RIO[BackendServiceEnv, Either[ErrorBody, Constant]] =
      BackendClient.getConstantByKey(appConstant).some.orElseFail(new Throwable("Wrong answer from backend"))

    override def cancelBooking(id: UUID): RIO[BackendServiceEnv, Either[ErrorBody, BookingDateBody]] =
      BackendClient.cancelBooking(id).some.orElseFail(new Throwable("Wrong answer from backend"))

    override def book(bookDateRequest: BookDateRequest): RIO[BackendServiceEnv, Either[ErrorBody, BookingDateBody]] =
      BackendClient.bookDate(bookDateRequest).some.orElseFail(new Throwable("Wrong answer from backend"))
  }

  val live: ULayer[BackendService] = ZLayer.succeed(new ServiceImpl)
}
