package org.zouzias.spark.lucenerdd

import org.apache.spark.sql.{DataFrame, Encoder, Encoders, ForeachWriter, Row, SparkSession}
import org.apache.spark.sql.functions.{col, expr, from_json}
import org.zouzias.spark.lucenerdd.UDF.updateIdUDF

object KafkaStreamingQuery {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder
      .appName("Indexer")
      .config("spark.master", "local")
      .config("spark.driver.userClassPathFirst","true")
      .config("spark.executor.userClassPathFirst","true")
      .config("spark.executor.instances", "1")
      .getOrCreate()

    val df: DataFrame = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "localhost:9092")
      .option("subscribe", "query")
      .option("startingOffsets", "earliest")
      .load()

    implicit val enc: Encoder[(String, String)] = Encoders.product[(String, String)]
    val modified_df = df.selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)")
      .as[(String, String)]
      .select("value")
    val luceneRDD = LuceneRDD(df, true)

    modified_df
      .writeStream
      .foreach(new ForeachWriter[Row] {

        def open(partitionId: Long, version: Long): Boolean = {
          // Open connection
          true
        }

        def process(record: Row): Unit = {
          //    val rdd: LuceneRDD[Row] = sc.objectFile("spark-warehouse/lucene_text_1715102057572").asInstanceOf[LuceneRDD[Row]]
          luceneRDD.phraseQuery("phrase",record.getString(0)).foreach(println)
        }

        def close(errorOrNull: Throwable): Unit = {
          // Close the connection
        }
      })
      .start()
      .awaitTermination()

//    val writing_df = modified_df.writeStream
//      .format("console")
//      .option("checkpointLocation","checkpoint_dir"+System.currentTimeMillis())
//      .outputMode("append")
//      .start()
//
//
//    writing_df.awaitTermination()
  }

}
