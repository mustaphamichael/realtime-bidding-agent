package com.rtb

import com.rtb.protocol._
import com.rtb.utils.Utils

object TestData {
  val validImpression: Impression        = Impression("someId", None, None, Some(300), None, None, Some(250), Some(3.12123))
  val impressionHighBidFloor: Impression = validImpression.copy(bidFloor = Some(999))

  val impressionUnmatchedWidthHeight: Impression = validImpression.copy(w = Some(999), h = Some(999))

  val impressions     = List(validImpression, impressionHighBidFloor)
  val targetedSiteIds = Set(
    "0006a522ce0f4bbbbaa6b3c38cafaa0f", // Id used for test
    "a26acb3c6acc409eb278bd22e4cb53b4",
    "47893305bf4f470f9f1e781f8838eae1",
    "84e22714719a45cc951a93f66097f464"
  )

  val requestPayload: BidRequest = BidRequest(
    id     = "REQ001",
    site   = Site(id = Utils.selectRandom(targetedSiteIds.toSeq), domain = "fake.tld"),
    device = Some(Device(id = "someId", geo = Some(Geo(country = Some("LT"))))),
    user   = Some(User(id = "someId", geo = Some(Geo(country = Some("LT"))))),
    imp    = Some(List(validImpression))
  )

  val campaign: Campaign = Campaign(
    id        = 1,
    country   = "LT",
    targeting = Targeting(targetedSiteIds = targetedSiteIds),
    banners   = List(
      Banner(
        id     = 1,
        src    = "https://business.eskimi.com/wp-content/uploads/2020/06/openGraph.jpeg",
        width  = 300,
        height = 250
      )
    ),
    bid       = 5d
  )

  val campaignWithDifferentCountry: Campaign = campaign.copy(id = 2, country = "US")
  val campaignWithEmptyTarget: Campaign      = campaign.copy(id = 4, targeting = Targeting(Set.empty))

  val activeCampaigns = Seq(campaign, campaignWithDifferentCountry, campaignWithEmptyTarget)
}
