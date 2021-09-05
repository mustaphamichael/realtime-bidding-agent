package com.rtb.protocol

import com.rtb.UnitSpec

class BidRequestSpec extends UnitSpec {

  "The Bid Request" should {
    val userCountry   = "LT"
    val deviceCountry = "US"
    val user          = User("1", Some(Geo(Some(userCountry))))
    val device        = Device("1", Some(Geo(Some(deviceCountry))))
    val bidRequest    = BidRequest("someId", None, Site("1", "test.com"), Some(user), Some(device))

    "return device country if both user and device exist" in {
      assert(bidRequest.country == deviceCountry)
    }

    "return device country if only device exist" in {
      val request = bidRequest.copy(user = None)
      assert(request.country == deviceCountry)
    }

    "return user country if only user exist" in {
      val request = bidRequest.copy(device = None)
      assert(request.country == userCountry)
    }

    "return blank string if both user and device do not exist" in {
      val request = bidRequest.copy(user = None, device = None)
      assert(request.country == "")
    }
  }

  "Request impression" should {
    val width      = 200
    val minWidth   = 50
    val maxWidth   = 250
    val height     = 300
    val minHeight  = 100
    val maxHeight  = 400
    val bidFloor   = 2.5
    val impression = Impression("someId", None, None, Some(width), None, None, Some(height), Some(bidFloor))

    "validate width" should {
      "with empty min/max" in {
        assert(impression.hasValidWidth(width))
        assert(!impression.hasValidWidth(-width))
      }

      "with min/max" in {
        val imp = impression.copy(wmin = Some(minWidth), wmax = Some(maxWidth))
        assert(imp.hasValidWidth(width))
        assert(!imp.hasValidWidth(minWidth))
        assert(!imp.hasValidWidth(minWidth))
      }

      "if only min width exists" in {
        val imp = impression.copy(wmin = Some(minWidth), h = None)
        assert(imp.hasValidWidth(width))
        assert(!imp.hasValidWidth(minWidth - width))
      }

      "if only max width exists" in {
        val imp = impression.copy(wmax = Some(maxWidth), h = None)
        assert(imp.hasValidWidth(width))
        assert(!imp.hasValidWidth(maxHeight + height))
      }
    }

    "validate height" should {
      "with empty min/max" in {
        assert(impression.hasValidHeight(height))
        assert(!impression.hasValidHeight(-height))
      }

      "with min/max" in {
        val imp = impression.copy(hmin = Some(minHeight), hmax = Some(maxHeight))
        assert(imp.hasValidHeight(height))
        assert(!imp.hasValidHeight(minHeight))
        assert(!imp.hasValidHeight(maxHeight))
      }

      "if only min height exists" in {
        val imp = impression.copy(hmin = Some(minHeight), h = None)
        assert(imp.hasValidHeight(height))
        assert(!imp.hasValidHeight(minHeight - height))
      }

      "if only max height exists" in {
        val imp = impression.copy(hmax = Some(maxHeight), h = None)
        assert(imp.hasValidHeight(height))
        assert(!imp.hasValidHeight(maxHeight + height))
      }
    }

    "validate bid price" should {
      "with equal or higher bid" in {
        assert(impression.hasValidBidPrice(bidFloor))
        assert(impression.hasValidBidPrice(bidFloor * 2))
      }

      "be invalid with lower bid" in {
        assert(!impression.hasValidBidPrice(bidFloor / 2))
      }

      "be invalid with empty bid floor" in {
        val imp = impression.copy(bidFloor = None)
        assert(!imp.hasValidBidPrice(bidFloor))
      }
    }
  }

}
