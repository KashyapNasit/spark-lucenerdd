/*

 */

package org.zouzias.spark.lucenerdd

import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.functions.{col, expr, from_json}
import org.zouzias.spark.lucenerdd.UDF.updateIdUDF

import java.util

object Driver {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder
      .appName("Indexer")
      .config("spark.master", "local")
      .config("spark.driver.userClassPathFirst","true")
      .config("spark.executor.userClassPathFirst","true")
      .config("spark.executor.instances", "1")
      .getOrCreate()

    val df = spark
      .read
      .format("kafka")
      .option("kafka.bootstrap.servers", "localhost:9092")
      .option("subscribe", "callSync")
      .load()

    val interactionDf = df
      .selectExpr("CAST(key AS STRING)", "CAST(value AS STRING) as value")
      .select(from_json( col("value"), Schema.kafkaSchema, new util.HashMap[String, String]()))
      .select("from_json(value).*")
      .withColumn("meeting", updateIdUDF(col("zipped_data")))
      .drop("zipped_data")
      .withColumn("struct_meeting",
        from_json(col("meeting"),
          Schema.meetingSchema,
          new util.HashMap[String, String]()))
      .drop("meeting")
      .selectExpr("*", "struct_meeting.*")
      .drop("struct_meeting")

    interactionDf.printSchema()

    //    interactionDf
    //      .writeStream
    //      .format("parquet")
    //      .option("path", "spark-warehouse/data")
    //      .option("checkpointLocation","spark-warehouse/checkpoint")
    //      .start()
    //      .awaitTermination()

    val transcriptDf = interactionDf
      .selectExpr("account_id",
         "meeting_id",
         "meetingInfo.time",
         "meetingInfo.userId as user_id",
         "explode(meetingInfo.transcript) as transcript")
      .withColumn("start_time", expr("transcript.startTime"))
      .withColumn("end_time", expr("transcript.endTime"))
      .withColumn("phrase", expr("transcript.phrase"))
      .withColumn("speaker", expr("transcript.speaker"))
      .withColumn("convId", expr("transcript.convId"))
      .drop("transcript")

    val luceneRDD: LuceneRDD[Row] = LuceneRDD(transcriptDf)

    luceneRDD.cache()

    luceneRDD.phraseQuery("phrase","hello").foreach(println)

//    val sqlContext= new org.apache.spark.sql.SQLContext(spark.sparkContext)
//    import sqlContext.implicits._
    luceneRDD.saveAsObjectFile("spark-warehouse/lucene_text_"+System.currentTimeMillis())

    luceneRDD.unpersist()

  }
}
