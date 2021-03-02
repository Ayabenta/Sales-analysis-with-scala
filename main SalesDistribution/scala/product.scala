import SalesForProducts.SalesData
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.col
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
      var ProductsGrouped =SalesData.groupBy("prodID").sum("amount","quantity")
        ProductsGrouped.show()
      val Products = ProductsGrouped.alias("PG").join(ProductsData.alias("PD"), ProductsGrouped("prodID") === ProductsData("prodID"), "Left")
        .select(col("PG.prodID").as("Product's ID"),
          col("PD.name"),
          col("PD.type"),
          col("PG.sum(quantity)").as("Total quantity of products"),
          col("PG.sum(amount)").as("Total amount of products"))

         Products.show(false)
      println("top 40 products that made the most profit for the company")
      val Amountgrouped = Products.orderBy(col("Total amount of products").desc)
      Amountgrouped.show(40)
      println("top 40 products that were sold the most")
      val Quantitygrouped = Products.orderBy(col("Total Quantity of products").desc)
      Quantitygrouped.show(40)

    }

}
