package com.cevaris.dedup.io

import java.io.File
import java.security.MessageDigest

import scala.io.Source

sealed trait Mapper {
  def toKey(a: Array[Byte]): String
}

object MD5Mapper extends Mapper {
  def toKey(a: Array[Byte]): String = {
    val digest = MessageDigest.getInstance("MD5")
    digest.digest(a).mkString
  }
}

case class FilesMap(source: File, mapper: Mapper) {

  val files = walk(source)
  val store = index

  private def walk(f: File): Seq[File] = {
    f.listFiles.foldLeft(Seq.empty[File]) {
      case (xs, x: File) if x.isDirectory => xs ++ walk(x)
      case (xs, x: File) if x.isFile => xs ++ Seq(x)
    }
  }

  private def index = (files.par.map { f: File =>
    mapper.toKey(Source.fromFile(f).map(_.toByte).toArray) -> f
  }).toMap

  override def toString: String = {
    store.toString
  }
}
