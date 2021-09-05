package com.rtb.protocol

final case class Campaign(
  id: Int,
  country: String,
  targeting: Targeting,
  banners: List[Banner],
  bid: Double
)

final case class Targeting(targetedSiteIds: Set[String])

final case class Banner(id: Int, src: String, width: Int, height: Int)
