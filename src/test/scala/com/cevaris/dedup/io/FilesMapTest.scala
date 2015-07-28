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
  private final val numOfFiles = 8

  test("fileMap should recurse a directory") {
    val fm = FilesMap(testDir, MD5Mapper)
    val actual = fm.files
    assert(actual.size == numOfFiles)
  }

  test("fileMap should create index form deep directory ") {
    val fm = FilesMap(testDir, MD5Mapper)
    val actual = fm.store
    assert(actual.size == numOfFiles)
  }

}
