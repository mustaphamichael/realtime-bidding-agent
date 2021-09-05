package com.rtb.routes

import akka.actor.testkit.typed.scaladsl.ActorTestKit
import akka.actor.typed.ActorSystem
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.rtb.{ TestData, UnitSpec }
import com.rtb.actors.BiddingAgent
import com.rtb.protocol.{ BidResponse, Site }
import com.typesafe.config.ConfigFactory
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._

class BidRouteSpec extends UnitSpec with ScalatestRouteTest {

  "The BidRoute" should {
    "accept a valid bid and return response" in {
      val request = TestData.requestPayload
      Post("/make-bid", request) ~> Route.seal(routes) ~> check {
        val responsePayload = responseAs[BidResponse]
        status shouldBe StatusCodes.OK
        responsePayload.bidRequestId shouldBe request.id
        Some(responsePayload.price) shouldBe TestData.validImpression.bidFloor
      }
    }

    "accept a bid with unknown location and return no content response" in {
      val payload = TestData.requestPayload.copy(device = None, user = None)
      Post("/make-bid", payload) ~> Route.seal(routes) ~> check {
        status shouldBe StatusCodes.NoContent
      }
    }

    "accept a bid with an unrecognized site and return no content response" in {
      val payload = TestData.requestPayload.copy(site = Site("unknown", ""))
      Post("/make-bid", payload) ~> Route.seal(routes) ~> check {
        status shouldBe StatusCodes.NoContent
      }
    }

    "accept a bid with a high impression price and return no content response" in {
      val payload = TestData.requestPayload.copy(imp = Some(List(TestData.impressionHighBidFloor)))
      Post("/make-bid", payload) ~> Route.seal(routes) ~> check {
        status shouldBe StatusCodes.NoContent
      }
    }

    "accept a bid with an unmatched dimension and return no content response" in {
      val payload = TestData.requestPayload.copy(imp = Some(List(TestData.impressionUnmatchedWidthHeight)))
      Post("/make-bid", payload) ~> Route.seal(routes) ~> check {
        status shouldBe StatusCodes.NoContent
      }
    }
  }

  lazy val testKit: ActorTestKit                 = ActorTestKit()
  implicit def typedSystem: ActorSystem[Nothing] = testKit.system
  private val biddingAgent                       = testKit.spawn(BiddingAgent(TestData.activeCampaigns))
  private val routes                             = new BidRoute(biddingAgent, ConfigFactory.load()).routes

}
