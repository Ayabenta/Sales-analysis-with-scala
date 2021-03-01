import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types._

object sales{
  SparkSession.clearActiveSession()
  private val SD = SparkSession
    .builder()
    .master("local[1]")
    .appName("SData")
    .config("spark.some.config.option", "some-value")
    .getOrCreate()
  val Sales = "Sales.txt"
  val DataStruct = StructType(Array(
    StructField("txID", IntegerType, false),
    StructField("custID", IntegerType, false),
    StructField("prodID", IntegerType, false),
    StructField("timestamp", StringType, false),
    StructField("amount", IntegerType, false),
    StructField("quantity", IntegerType, false)
  ))

  val SalesData = SD.read
    .option("sep", "|")
    .schema(DataStruct)
    .csv (Sales)

  def main(args:Array[String]) = {
    // val df = spark.createDataFrame(
    //   spark.sparkContext.parallelize(simpleData),DataStruct)
    SalesData.printSchema()
    SalesData.show()
    val Sales2013 = SalesData.filter(SalesData("timestamp").contains("2013"))
    Sales2013.show(false)
    val Amount = Sales2013.select("amount").rdd.map(r=>r(0).asInstanceOf[Int]).collect()
    println(Amount.sum)
    //result is 1637540

  }
}