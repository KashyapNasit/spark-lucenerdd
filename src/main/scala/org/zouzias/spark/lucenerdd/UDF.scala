package org.zouzias.spark.lucenerdd

/*

 */
import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.functions.udf

object UDF {

  val unzipData: (String => String) = (unzipped: String) => {
    GZip.decompress(unzipped.getBytes).get
  }
  val updateIdUDF: UserDefinedFunction = udf(unzipData)

}
