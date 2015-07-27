package com.cevaris.dedup.io

import java.io.File
import java.security.MessageDigest

import scala.io.Source


sealed trait Mapper {
  def map(a: Array[Byte]): String
}

object MD5Mapper extends Mapper {
  def map(a: Array[Byte]): String = {
    val digest = MessageDigest.getInstance("MD5")
    digest.digest(a).mkString
  }
}

case class FilesMap(source: File, mapper: Mapper) {

  def walk(f: File): Seq[File] = {
//    if(!f.exists()) return Seq.empty[File]

//    f.listFiles() match {
////      case Seq.empty => Seq.empty[File]
//      case x:File if x.isFile => Seq(f)
//      case (x:File)::xs if x.isDirectory => walk(x)
//      case (x:File)::xs if x.isFile => Seq(f)
//
//    }

    f.listFiles.foldLeft(Seq.empty[File]) {
      case (xs, x:File) if x.isFile && !x.isDirectory => xs ++ Seq(x)
      case (xs, x:File) if x.isDirectory => xs ++ walk(x)
    }
  }


  override def toString: String = {
//    walk(source).map { f: File =>
//      mapper.map(Source.fromFile(f).map(_.toByte).toArray)
//    }.mkString
    ""
  }
}
