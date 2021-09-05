package com.rtb.config

import com.typesafe.config.Config

final case class HttpConfig(
  host: String,
  port: Int
)

object HttpConfig {
  def fromConfig(config: Config): HttpConfig = HttpConfig(
    host = config.getString("http.host"),
    port = config.getInt("http.port")
  )
}
