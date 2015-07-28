package com.cevaris.dedup.io

import java.io.{FilenameFilter, File}
import java.nio.charset.MalformedInputException
import java.security.MessageDigest

import com.twitter.logging.Logger

import scala.io.Source

sealed trait Mapper {
  def toKey(a: Option[Array[Byte]]): Option[String]
}

object MD5Mapper extends Mapper {
  def toKey(a: Option[Array[Byte]]): Option[String] = {
    val digest = MessageDigest.getInstance("MD5")
    a.map(digest.digest(_).mkString)
  }
}

case class FilesMap(source: File, mapper: Mapper, extFilters: Seq[String]) {
  private[this] val log = Logger.get(getClass)
  private[this] implicit val codec = scala.io.Codec.ISO8859

  private[this] val filter =
    new FilenameFilter {
      override def accept(dir: File, name: String): Boolean =
        // if empty list, accept file
        extFilters.filter(name.endsWith(_)).isEmpty
    }

  val files = walk(source)
  val store = index

  private def walk(f: File): Seq[File] = {

    // Non-directory files return null on `listFiles`
    // this happens when given a single file to browse
    if (f.isFile && f.listFiles == null) return Seq(f)

    f.listFiles(filter).foldLeft(Seq.empty[File]) {
      case (xs, x: File) if x.isDirectory =>
        log.info(s"found directory ${x.getAbsolutePath}")
        xs ++ walk(x)
      case (xs, x: File) if x.isFile =>
        log.info(s"found file ${x.getAbsolutePath}")
        xs ++ Seq(x)
    }
  }

  private def fileToBytes(f: File): Option[Array[Byte]] = try {
    Some(Source.fromFile(f).map(_.toByte).toArray)
  } catch {
    case e: MalformedInputException =>
      log.error(e, s"error while reading ${f.getAbsolutePath}")
      None
  }

  private def index = files.par.map { f: File =>
    fileToBytes(f).map { bytes: Array[Byte] =>
      mapper.toKey(Some(bytes)).map(_ -> f)
    }
  }.map(_.map(_.toMap))

  override def toString: String = {
    s"Files Found: ${files.size.toString}"
  }
}
