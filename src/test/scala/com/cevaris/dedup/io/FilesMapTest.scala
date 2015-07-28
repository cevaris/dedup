package com.cevaris.dedup.io

import java.io.File

import com.twitter.io.TempDirectory
import scalax.file.Path

class FilesMapTest extends org.scalatest.FunSuite {

  /*
  testdir (master|+2?) ~࿔ tree
  .
  ├── 0
  │   ├── 0
  │   │   └── 0
  │   ├── 0.txt
  │   ├── 1
  │   │   ├── 0.txt
  │   │   └── 1.txt
  │   └── 1.txt
  ├── a
  │   └── b
  │       └── c
  │           └── d
  │               └── abcd.txt
  ├── e
  │   └── f
  │       └── g
  │           └── efg.txt
  ├── h
  │   └── i
  │       └── hi.txt
  └── j.txt
   */

  private final val testDir = new File(getClass.getResource("/testDir").getPath)
  private final val testFilter = new File(getClass.getResource("/testFilters").getPath)
  private final val numOfFiles = 8
  private def fileMap = FilesMap(testDir, MD5Mapper, Seq.empty[String])

  test("fileMap should recurse a directory") {
    val fm = fileMap
    val actual = fm.files
    assert(actual.size == numOfFiles)
  }

  test("fileMap should create index form deep directory ") {
    val fm = fileMap
    val actual = fm.store
    assert(actual.size == numOfFiles)
  }

  test("fileMap should ") {
    val fm = fileMap.copy(source = testFilter, extFilters = List(".mp4"))
    val testFile = new File(getClass.getResource("/testFilters/test.mp4").getPath)
    val actual = fm.store.map(_.map(_.values))
    println(actual)
    assert(actual.filter(_ == testFile).isEmpty)
  }

}
