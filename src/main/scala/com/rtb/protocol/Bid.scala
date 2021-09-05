package com.rtb.protocol

//# Request protocol
final case class BidRequest(
  id: String,
  imp: Option[List[Impression]],
  site: Site,
  user: Option[User],
  device: Option[Device]
) {
  val country: String = {
    val deviceCountry =
      try { device.get.geo.get.country.get }
      catch { case _: Exception => "" }
    val userCountry   =
      try { user.get.geo.get.country.get }
      catch { case _: Exception => "" }

    if (deviceCountry.isEmpty) userCountry else deviceCountry
  }
}

final case class Impression(
  id: String,
  wmin: Option[Int],
  wmax: Option[Int],
  w: Option[Int],
  hmin: Option[Int],
  hmax: Option[Int],
  h: Option[Int],
  bidFloor: Option[Double]
) {
  /* Check that the campaign bid is more than the minimum impression price*/
  def hasValidBidPrice(bid: Double): Boolean =
    bid >= bidFloor.getOrElse(Double.MaxValue)

  def hasValidWidth(width: Int): Boolean = this.w match {
    case Some(w) => width == w
    case None    => width >= min(wmin) && width <= max(wmax)
  }

  def hasValidHeight(height: Int): Boolean = this.h match {
    case Some(h) => height == h
    case None    => height >= min(hmin) && height <= max(hmax)
  }

  private def min(value: Option[Int]) = value.getOrElse(0)
  private def max(value: Option[Int]) = value.getOrElse(Int.MaxValue)

}

final case class Site(id: String, domain: String)
final case class User(id: String, geo: Option[Geo])
final case class Device(id: String, geo: Option[Geo])
final case class Geo(country: Option[String])

//# Response protocol
final case class BidResponse(
  id: String,
  bidRequestId: String,
  price: Double,
  adid: Option[String],
  banner: Option[Banner]
)
