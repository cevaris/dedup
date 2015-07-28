package com.cevaris.dedup.io

import java.security.MessageDigest

import com.twitter.logging.Logger

sealed trait Mapper {
  def toKey(a: Array[Byte]): String
}

object MD5Mapper extends Mapper {
  private[this] val log = Logger.get(getClass)

  def toKey(a: Array[Byte]): String = {
    log.debug(s"forging key for file ${a}")
    MessageDigest.getInstance("MD5").digest(a).mkString
  }
}