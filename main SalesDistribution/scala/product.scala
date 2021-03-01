import SalesForProducts.SalesData
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types._

  object product {
    SparkSession.clearActiveSession()
    private val SD = SparkSession
      .builder()
      .master("local[1]")
      .appName("SData")
      .config("spark.some.config.option", "some-value")
      .getOrCreate()
    val Product = "Product.txt"
    val DataStruct = StructType(Array(
      StructField("prodID", IntegerType, false),
      StructField("name", StringType, false),
      StructField("type", StringType, false),
      StructField("version", StringType, true),
      StructField("price", IntegerType, false)
    ))

    val ProductsData = SD.read
      .option("sep", "|")
      .schema(DataStruct)
      .csv (Product)

    def main(args:Array[String]) = {
      ProductsData.printSchema()
      ProductsData.show()
      // val df = spark.createDataFrame(
      //   spark.sparkContext.parallelize(simpleData),DataStruct)
      var Productgrouped =SalesData.groupBy("prodID", "name").sum("amount","quantity").show()



    }


}
