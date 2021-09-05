package com.rtb.routes

import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.typed.{ ActorRef, ActorSystem }
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import com.rtb.actors.BiddingAgent
import com.rtb.protocol.{ BidRequest, BidResponse }
import com.typesafe.config.Config
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._
import org.slf4j.LoggerFactory

import java.util.UUID
import java.util.concurrent.TimeUnit
import scala.util.{ Failure, Success }

class BidRoute(
  biddingAgent: ActorRef[BiddingAgent.Command],
  config: Config
)(implicit system: ActorSystem[Nothing]) {

  private val log = LoggerFactory.getLogger(getClass.getName)

  implicit val timeout: Timeout = Timeout(config.getDuration("akka.ask-execution-timeout").toSeconds, TimeUnit.SECONDS)

  val routes: Route = makeBidRoute

  private def makeBidRoute: Route =
    path("make-bid") {
      post {
        entity(as[BidRequest]) { req =>
          log.info("Received request to make a bid [{}] from site [{}]", req.id, req.site.domain)

          onComplete(
            biddingAgent.ask(ref => BiddingAgent.MakeBid(req, ref))
          ) {
            case Failure(exception)                      =>
              log.warn("Making bid failed with exception [{}]", exception)
              complete(StatusCodes.InternalServerError)
            case Success(BiddingAgent.BidMade(campaign)) =>
              campaign match {
                case None                              =>
                  log.info("No campaign found for bid [{}]", req.id)
                  complete(StatusCodes.NoContent)
                case Some((campaignId, banner, price)) =>
                  val response = BidResponse(
                    id           = UUID.randomUUID().toString.replace("-", ""),
                    bidRequestId = req.id,
                    price        = price,
                    adid         = Some(campaignId.toString),
                    banner       = Some(banner)
                  )
                  complete(response)
              }
          }
        }
      }
    }
}
