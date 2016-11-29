package code.book.table

import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment, _}
import org.apache.flink.api.table._

/**
  * Created by liguohua on 29/11/2016.
  */
object TableTest001 {
  def main(args: Array[String]): Unit = {


    val env = ExecutionEnvironment.getExecutionEnvironment
    val tableEnv = TableEnvironment.getTableEnvironment(env)

    // register the DataSet cust as table "Customers" with fields derived from the dataset
    case class Customers(name: String, age: Int)
    val customers: DataSet[Customers] = env.fromElements(new Customers("zhangsan", 18), new Customers("zhangsan", 19))

    tableEnv.registerDataSet("Customers", customers)
    val result = tableEnv.sql("SELECT name FROM Customers")



//    //     register the DataSet ord as table "Orders" with fields user, product, and amount
//    val ds: DataSet[(Long, String, Integer)] = env.fromElements(Tuple3(100, "computer", 3200))
//    // register the DataSet under the name "Orders"
//    tableEnv.registerDataSet("Orders", ds, 'user, 'product, 'amount)
//    val result2 = tableEnv.sql("SELECT SUM(amount) FROM Orders AS o")
  }

}
