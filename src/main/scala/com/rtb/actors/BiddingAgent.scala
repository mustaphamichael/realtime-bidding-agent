package com.rtb.actors

import akka.actor.typed.{ ActorRef, Behavior }
import akka.actor.typed.scaladsl.Behaviors
import com.rtb.protocol.{ Banner, BidRequest, Campaign, Impression }
import com.rtb.utils.Utils

import scala.util.Try

object BiddingAgent {
  type CampaignId = Int
  type Price      = Double
  type BidData    = Option[(CampaignId, Banner, Price)]

  sealed trait Command
  final case class MakeBid(request: BidRequest, replyTo: ActorRef[BidMade]) extends Command
  final case class BidMade(data: BidData)

  def apply(activeCampaigns: Seq[Campaign]): Behavior[Command] =
    Behaviors.receiveMessage { case MakeBid(request, replyTo) =>
      val bidData = matchBid(request, activeCampaigns)
      replyTo ! BidMade(bidData)
      Behaviors.same
    }

  def matchBid(request: BidRequest, campaigns: Seq[Campaign]): BidData = {
    val campaignsByCountryAndSite = filterCampaignByCountryAndSite(campaigns, request.country, request.site.id)
    val matchedCampaigns          = filterCampaignByImpressions(campaignsByCountryAndSite, request.imp)

    if (matchedCampaigns.isEmpty) None
    else
      Try {
        val randomCampaign  = Utils.selectRandom(matchedCampaigns)
        val id              = randomCampaign.keys.head
        val bannerList      = randomCampaign.get(id).head
        val (banner, price) = Utils.selectRandom(bannerList)

        (id, banner, price)
      }.toOption
  }

  /** Filter campaigns by the country and site */
  def filterCampaignByCountryAndSite(campaigns: Seq[Campaign], country: String, siteId: String): Seq[Campaign] =
    campaigns
      .filter(campaign =>
        campaign.country == country &&
          campaign.targeting.targetedSiteIds.contains(siteId)
      )

  /** Find campaigns and their corresponding banners that fit the impressions */
  def filterCampaignByImpressions(
    campaigns: Seq[Campaign],
    impressions: Option[List[Impression]]
  ): Seq[Map[CampaignId, List[(Banner, Price)]]] = {
    campaigns
      .map(campaign => campaign -> validBidImpressions(impressions, campaign.bid))
      .withFilter { case (_, impressions) => impressions.nonEmpty }
      .map { case (campaign, impressions) =>
        (for {
          banner <- campaign.banners
          impression <- impressions
          if impression.hasValidWidth(banner.width) && impression.hasValidHeight(banner.height)
        } yield (campaign.id, banner, impression.bidFloor.getOrElse(campaign.bid)))
          .groupMap(_._1) { case (_, banner, price) => (banner, price) }
      }
      .filter(_.nonEmpty)
  }

  /** Filter impressions that falls within the campaign bid price */
  def validBidImpressions(impressions: Option[List[Impression]], bid: Double): List[Impression] =
    impressions.getOrElse(List()).filter(imp => imp.hasValidBidPrice(bid))
}
