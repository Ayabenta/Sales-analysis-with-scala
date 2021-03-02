import SalesforCostumers.SalesData
import org.apache.spark.SparkException
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.col
import org.apache.spark.sql.types._
object Customers {
   SparkSession.clearActiveSession()
    private val SD = SparkSession
      .builder()
      .master("local[1]")
      .appName("SData")
      .config("spark.some.config.option", "some-value")
      .getOrCreate()
    val Customer = "Customer.txt"
    val DataStruct = StructType(Array(
      StructField("CustID", IntegerType, false),
      StructField("Firstname", StringType, false),
      StructField("Lastname", StringType, false),
      StructField("Phone", StringType, true)
    ))

    val CustomersData = SD.read
      .option("sep", "|")
      .schema(DataStruct)
      .csv (Customer)

    def main(args:Array[String]) = {
      // val df = spark.createDataFrame(
      //   spark.sparkContext.parallelize(simpleData),DataStruct)
      try {
        CustomersData.printSchema()

        CustomersData.show()
        val SalesperCustID = SalesData.groupBy("CustID").sum("quantity","amount")
        SalesperCustID.show(false)
        val SalesperCustomer = CustomersData.alias("CD").join(SalesperCustID.alias("SPC"), CustomersData("CustID") === SalesperCustID("CustID"), "Left")
          .select(col("CD.CustID").as("Costumer's ID"),
            col("CD.Firstname"),
            col("CD.Lastname"),
            col("SPC.sum(amount)").as("Total amount "),
            col("SPC.sum(quantity)").as("Number of products"))
        SalesperCustomer.show(false)
        println(" Top 10 active customers  ")
        val Top10Customers = SalesperCustomer.orderBy(col("Number of products").desc)
        Top10Customers.show(10)
        println(" Top 10 profitable customers  ")
       // val Top10profitableCustomers = SalesperCustomer.orderBy(col("Total amount").desc)
       // Top10profitableCustomers.show(10)
      }

      catch{
        case e: InterruptedException  => println("Couldn't find that file.")
        case e: SparkException => println("Had an IOException trying to read that file")
      }
//for the errors showing I created a breakpoint for each one of them, and added the try and catch but none of them worked.

    }

}
