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
    val digest = MessageDigest.getInstance("MD5").digest(a)
    return new String(java.util.Base64.getEncoder().encode(digest));
  }
}