package com.rtb.protocol

final case class Campaign(
  id: Int,
  country: String,
  targeting: Targeting,
  banners: List[Banner],
  bid: Double
)

final case class Targeting(
  targetedSiteIds: Set[String],
  demographics: Set[Demography] = Set.empty,
  technologies: Set[Technology] = Set.empty,
  interestIds: Set[String]      = Set.empty
)

/* Targeting through age range, gender, parental status or household income */
final case class Demography(
  age: Option[AgeRange],
  gender: Option[String],
  parentalStatus: Option[String],
  income: Option[Double]
)
final case class AgeRange(min: Option[Int], max: Option[Int])

/* Targeting through device type, OS operators, set of browsers, and 3G/4G/Wifi connections */
final case class Technology(device: TargetDevice, browsers: Set[String], connectionTypes: Set[String])
final case class TargetDevice(deviceType: Option[String], manufacturer: Option[String], model: Option[String], os: Option[String])

final case class Banner(id: Int, src: String, width: Int, height: Int)
