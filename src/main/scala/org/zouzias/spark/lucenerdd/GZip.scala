/*

 */
package org.zouzias.spark.lucenerdd

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}
import java.util.Base64
import java.util.zip.{GZIPInputStream, GZIPOutputStream}
import scala.util.Try

object GZip {

  def compress(input: Array[Byte]): Array[Byte] = {
    val bos = new ByteArrayOutputStream(input.length)
    val gzip = new GZIPOutputStream(bos)
    gzip.write(input)
    gzip.close()
    val compressed = bos.toByteArray
    bos.close()
    compressed
  }

  def decompress(compressed: Array[Byte]): Option[String] =
    Try {
      val decodedString = Base64.getDecoder.decode(compressed)
      val inputStream = new GZIPInputStream(new ByteArrayInputStream(decodedString))
      scala.io.Source.fromInputStream(inputStream).mkString
    }.toOption

  def main(args: Array[String]): Unit = {
    val str = ""
//    println(decompress(str.getBytes))
  }
}