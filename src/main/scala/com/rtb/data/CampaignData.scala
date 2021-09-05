package com.rtb.data

import com.rtb.protocol.{ Banner, Campaign, Targeting }

/** This is a set of data used only for the purpose of running the application.
  *
  * Update the [[CampaignData.activeCampaigns]] variable to run the solution against a different set of data.
  */
object CampaignData {
  val activeCampaigns = Seq(
    Campaign(
      id        = 1,
      country   = "LT",
      targeting = Targeting(targetedSiteIds = Set("0006a522ce0f4bbbbaa6b3c38cafaa0f")),
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
  )

}
