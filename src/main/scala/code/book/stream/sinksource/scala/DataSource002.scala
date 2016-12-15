package code.book.stream.sinksource.scala


import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}

import scala.collection.immutable.{Queue, Stack}
import scala.collection.mutable
import scala.collection.mutable.{ArrayBuffer, ListBuffer}

object DataSource002 {
  def main(args: Array[String]): Unit = {

    val env =  StreamExecutionEnvironment.getExecutionEnvironment
    //1.读取本地文本文件,本地文件以file://开头
    val ds1: DataStream[String] = env.readTextFile("file:///Applications/flink-1.1.3/README.txt")
    ds1.print()

    //2.读取hdfs文本文件，hdfs文件以hdfs://开头,不指定master的短URL
    val ds2: DataStream[String] = env.readTextFile("hdfs:///input/flink/README.txt")
    ds2.print()

    //3.读取hdfs CSV文件,转化为tuple
    val path = "hdfs://qingcheng11:9000/input/flink/sales.csv"
    val ds3 = env.readCsvFile[(String, Int, Int, Double)](
      filePath = path,
      lineDelimiter = "\n",
      fieldDelimiter = ",",
      lenient = false,
      ignoreFirstLine = true,
      includedFields = Array(0, 1, 2, 3))
    ds3.print()

    //4.读取hdfs CSV文件,转化为case class
    case class Sales(transactionId: String, customerId: Int, itemId: Int, amountPaid: Double)
    val ds4 = env.readCsvFile[Sales](
      filePath = path,
      lineDelimiter = "\n",
      fieldDelimiter = ",",
      lenient = false,
      ignoreFirstLine = true,
      includedFields = Array(0, 1, 2, 3),
      pojoFields = Array("transactionId", "customerId", "itemId", "amountPaid")
    )
    ds4.print()

  }
}
