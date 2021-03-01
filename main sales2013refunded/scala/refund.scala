import SalesRefunded.Sales2013
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types._
  object refund {
    SparkSession.clearActiveSession()
    private val SD = SparkSession
      .builder()
      .master("local[1]")
      .appName("SData")
      .config("spark.some.config.option", "some-value")
      .getOrCreate()
    val Refund = "Refund.txt"
    val DataStruct = StructType(Array(
      StructField("refID", IntegerType, false),
      StructField("txID", IntegerType, false),
      StructField("custID", IntegerType, false),
      StructField("prodID", IntegerType, false),
      StructField("timestamp", StringType, false),
      StructField("amount", IntegerType, false),
      StructField("quantity", IntegerType, false)
    ))

    val RefundData = SD.read
      .option("sep", "|")
      .schema(DataStruct)
      .csv (Refund)

    def main(args:Array[String]) = {
      RefundData.printSchema()
      RefundData.show()
      val refunded = RefundData.groupBy("txID").sum("amount")
        refunded.show(false)
      val amount = RefundData.select("amount").rdd.map(r=>r(0).asInstanceOf[Int]).collect()
      val Amountrefunded = amount.sum
      println(Amountrefunded) //result is 189790
      val AmountSales = Sales2013.select("amount").rdd.map(r=>r(0).asInstanceOf[Int]).collect().sum
      println(AmountSales)
      val SalesExceptRefund = AmountSales - Amountrefunded
      println(SalesExceptRefund) // result is 1447750.



    }
  }