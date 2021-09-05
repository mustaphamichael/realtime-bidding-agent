package com.rtb.utils

import scala.util.Random

object Utils {

  def selectRandom[A](list: Seq[A]): A = list(new Random().nextInt(list.length))
}
