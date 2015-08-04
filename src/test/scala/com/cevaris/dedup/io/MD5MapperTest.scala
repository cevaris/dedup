package com.cevaris.dedup.io

import java.io.File

import org.scalatest.FunSuite

import scala.io.Source

/**
 * Created by acardenas on 8/4/15.
 */
class MD5MapperTest extends FunSuite {

  private final val testDir = new File(getClass.getResource("/testDir").getPath)
  private final val testFileA = new File(testDir + File.separator + "j.txt")
  private final val testFileB = new File(testDir + File.separator + "k.txt")

  test("MD5Mapper should return empty value for null bytes") {
    val testBytes = Array.empty[Byte]
    val actual = MD5Mapper.toKey(testBytes)
    val expected = "1B2M2Y8AsgTpgAmY7PhCfg=="
    assert(actual == expected)
  }

  test("MD5Mapper should be idempotent") {
    val testBytes = "test-bytes-data".getBytes()
    val first = MD5Mapper.toKey(testBytes)
    val second = MD5Mapper.toKey(testBytes)
    assert(first == second)
  }

  test("MD5Mapper should return different values for two different byte arrays") {
    val first = MD5Mapper.toKey("test-input-1".getBytes())
    val second = MD5Mapper.toKey("test-input-2".getBytes())
    assert(first != second)
  }

  test("MD5Mapper should return different values for two different files") {
    val fileAData = Source.fromFile(testFileA).map(_.toByte).toArray
    val fileBData = Source.fromFile(testFileB).map(_.toByte).toArray

    val first = MD5Mapper.toKey(fileAData)
    val second = MD5Mapper.toKey(fileBData)
    assert(first != second)
  }



}
