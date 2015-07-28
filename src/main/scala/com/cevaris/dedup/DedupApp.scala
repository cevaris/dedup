package com.cevaris.dedup


import java.io.{File, FileNotFoundException}

import com.cevaris.dedup.exceptions.MissingSourceFile
import com.cevaris.dedup.io.{FilesMap, MD5Mapper, MOV, MP4}
import com.twitter.app.App
import com.twitter.logging._

import scala.util.{Failure, Success, Try}


object DedupApp extends App with Logging {

  def file(s: String) = Try({
    val f = new File(s)
    if(f.exists()) f else throw new FileNotFoundException(s)
  })

  def md5Mapper(f: File) = FilesMap(f, MD5Mapper, List(MOV, MP4))
  val sourceFlag = flag("file", "", "Source file/directory")


  def main() {
    val sourceFileStr = sourceFlag.getWithDefault.map(_.trim) match {
      case Some(x) => x
      case None => throw MissingSourceFile
    }

    val fileMap = file(sourceFileStr) match {
      case Success(x) => md5Mapper(x)
      case Failure(e) => {
        log.error(e, s"Failed reading file: ${sourceFileStr}")
        return
      }
    }

    log.info(fileMap.toString)
  }

}
