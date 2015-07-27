package com.cevaris.dedup.io

import java.io.File

import com.twitter.io.TempDirectory
import scalax.file.Path

class FilesMapTest extends org.scalatest.FunSuite {

  val txt = ".txt"
  val dir = ".dir"

  val testDir = {
    val root = TempDirectory.create()
    File.createTempFile("000",txt,root)
    File.createTempFile("001",txt,root)
    File.createTempFile("002",txt,root)
    root
  }

  val testDeepDir = {
    val root = TempDirectory.create()
    Path.fromString(s"${root.getAbsolutePath}/000/000/000") createDirectory ()
    Path.fromString(s"${root.getAbsolutePath}/000/000/000.txt") createFile ()

    Path.fromString(s"${root.getAbsolutePath}/001/000/000") createDirectory ()
    Path.fromString(s"${root.getAbsolutePath}/001/001.txt") createFile ()

    Path.fromString(s"${root.getAbsolutePath}/002/000/000") createDirectory ()
    Path.fromString(s"${root.getAbsolutePath}/002/001/000") createDirectory ()
    Path.fromString(s"${root.getAbsolutePath}/002/001/002.txt") createFile ()

    Path.fromString(s"${root.getAbsolutePath}/003.txt") createDirectory ()

    root
  }

  test("fileMap should be able to walk a directory"){
    val fm = FilesMap(testDir, MD5Mapper)
    val actual = fm.walk(testDir)
    assert(actual.size == 3)
  }

  test("fileMap should be able to recurse a directory"){
    val fm = FilesMap(testDeepDir, MD5Mapper)
    val actual = fm.walk(testDeepDir)
    assert(actual.size == 4)
  }

}
