package org.zouzias.spark.lucenerdd

import org.apache.spark.sql.types.{IntegerType, StructField, StructType}
import org.apache.spark.sql.{Row, SparkSession}

object ReadSavedFile {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder
      .appName("Indexer")
      .config("spark.master", "local")
      .config("spark.driver.userClassPathFirst","true")
      .config("spark.executor.userClassPathFirst","true")
      .config("spark.executor.instances", "1")
      .getOrCreate()

    // Define the schema
    val schema = new StructType()
      .add(StructField("value", IntegerType, nullable = false))

    // Create a Seq of Rows
    val data = Seq(Row(1), Row(2), Row(3))

    // Create DataFrame
    val df = spark.createDataFrame(
      spark.sparkContext.parallelize(data),
      schema
    )
    println("Starting...")
    val luceneRDD = LuceneRDD(df, true)
//    val rdd: LuceneRDD[Row] = sc.objectFile("spark-warehouse/lucene_text_1715102057572").asInstanceOf[LuceneRDD[Row]]
    luceneRDD.phraseQuery("phrase","hello").foreach(println)

  }

}
