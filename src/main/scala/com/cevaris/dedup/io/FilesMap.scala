package com.cevaris.dedup.io

import java.io.{File, FilenameFilter}

import com.twitter.logging.Logger

import scala.io.Source

case class FilesMap(source: File, mapper: Mapper, extFilters: Seq[ExtensionFilter]) {
  private[this] val log = Logger.get(getClass)
  private[this] implicit val codec = scala.io.Codec.ISO8859

  private[this] val filter =
    new FilenameFilter {
      override def accept(dir: File, name: String): Boolean = {
        // if result is empty, accept file
        val result = extFilters.filter { ef: ExtensionFilter =>
          name.toLowerCase.endsWith(ef.name)
        }.isEmpty
        if(!result) log.debug(s"skipping file ${dir.getAbsolutePath}/$name")
        result
      }
    }

  val files = walk(source)
  val store = index

  private def walk(f: File): Seq[File] = {

    // Non-directory files return null on `listFiles`
    // this happens when given a single file to browse
    if(!f.isDirectory) return Seq.empty[File]

    val fs = f.listFiles(filter).foldLeft(Seq.empty[File]) {
      case (xs, x: File) if x.isDirectory =>
        log.debug(s"found directory ${x.getAbsolutePath}")
        xs ++ walk(x)
      case (xs, x: File) if x.isFile =>
        log.debug(s"found file ${x.getAbsolutePath}")
        xs ++ Seq(x)
    }

    log.debug(s"found files $fs")
    fs
  }

  private def fileToBytes(f: File): Array[Byte] = {
    log.debug(s"converting to bytes file ${f.toPath}")
    Source.fromFile(f).map(_.toByte).toArray
  }

  private def index = files.par map { f: File =>
    mapper.toKey(fileToBytes(f)) -> f
  }

  override def toString: String = {
    s"#Unique files: ${files.size.toString}\n#Total files: ${index.size}"
  }
}
