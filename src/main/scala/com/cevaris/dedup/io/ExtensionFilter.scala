package com.cevaris.dedup.io

sealed trait ExtensionFilter {
  val name: String
}

object MOV extends ExtensionFilter {
  val name = ".mov"
}

object MP4 extends ExtensionFilter {
  val name = ".mp4"
}
