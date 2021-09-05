package com.rtb.actors

import com.rtb.{ TestData, UnitSpec }
import com.rtb.utils.Utils

class BiddingAgentSpec extends UnitSpec {

  "The Bidding Agent" should {
    "filter campaigns by country and site" in {
      val country = "LT"
      val siteId  = Utils.selectRandom(TestData.targetedSiteIds.toSeq)

      withClue("valid country and site") {
        val result = BiddingAgent.filterCampaignByCountryAndSite(TestData.activeCampaigns, country, siteId)
        result.contains(TestData.campaign) shouldBe true
        result.contains(TestData.campaignWithDifferentCountry) shouldBe false
        result.contains(TestData.campaignWithEmptyTarget) shouldBe false
      }

      withClue("with unknown country") {
        val result = BiddingAgent.filterCampaignByCountryAndSite(TestData.activeCampaigns, "unknown-country", siteId)
        result.size shouldBe 0
      }

      withClue("with unknown site") {
        val result = BiddingAgent.filterCampaignByCountryAndSite(TestData.activeCampaigns, country, "unknown-site")
        result.size shouldBe 0
      }
    }

    "filter request impressions by bid price" in {
      val campaignBid = 5d

      withClue("return impressions that fall below the bid price") {
        val result = BiddingAgent.validBidImpressions(Some(TestData.impressions), campaignBid)
        result.contains(TestData.validImpression) shouldBe true
        result.contains(TestData.impressionHighBidFloor) shouldBe false
      }

      withClue("return an empty list if impressions is not valid") {
        val result = BiddingAgent.validBidImpressions(None, campaignBid)
        result.size shouldBe 0
      }
    }

    "filter campaigns by impressions" in {
      withClue("valid campaigns and impressions") {
        val result = BiddingAgent.filterCampaignByImpressions(TestData.activeCampaigns, Some(TestData.impressions))
        result.size should not be 0
        println(result)
      }

      withClue("return empty list if impressions are empty") {
        val result = BiddingAgent.filterCampaignByImpressions(TestData.activeCampaigns, None)
        result.size shouldBe 0
      }

      withClue("return empty list if campaigns are empty") {
        val result = BiddingAgent.filterCampaignByImpressions(Nil, Some(TestData.impressions))
        result.size shouldBe 0
      }
    }
  }

}
