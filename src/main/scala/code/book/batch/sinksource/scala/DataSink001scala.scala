package code.book.batch.sinksource.scala

import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment, _}
import org.apache.flink.core.fs.FileSystem.WriteMode

/**
  * Created by liguohua on 10/12/2016.
  */
object DataSink001scala {
  def main(args: Array[String]): Unit = {
    //0.主意：不论是本地还是hdfs.若Parallelism>1将把path当成目录名称，若Parallelism=1将把path当成文件名。
    val env = ExecutionEnvironment.getExecutionEnvironment
    val ds1: DataSet[(Int, String)] = env.fromCollection(Map(1 -> "spark", 2 -> "flink"))
    //1.写入到本地，文本文档,NO_OVERWRITE模式下如果文件已经存在，则报错，OVERWRITE模式下如果文件已经存在，则覆盖
    ds1.setParallelism(1).writeAsText("file:///Users/liguohua/Documents/F/code/idea/git/simple-flink/src/main/scala/code/book/batch/other/test", WriteMode.OVERWRITE)
    env.execute()

    //2.写入到hdfs，文本文档,路径不存在则自动创建路径。
    ds1.setParallelism(1).writeAsText("hdfs:///output/flink/datasink/test001.txt", WriteMode.OVERWRITE)
    env.execute()

    //3.写入到hdfs，CSV文档
    //3.1读取csv文件
    val inPath = "hdfs:///input/flink/sales.csv"
    case class Sales(transactionId: String, customerId: Int, itemId: Int, amountPaid: Double)
    val ds2 = env.readCsvFile[Sales](
      filePath = inPath,
      lineDelimiter = "\n",
      fieldDelimiter = ",",
      lenient = false,
      ignoreFirstLine = true,
      includedFields = Array(0, 1, 2, 3),
      pojoFields = Array("transactionId", "customerId", "itemId", "amountPaid")
    )
    //3.2将CSV文档写入到hdfs
    val outPath = "hdfs:///output/flink/datasink/sales.csv"
    ds2.setParallelism(1).writeAsCsv(filePath = outPath, rowDelimiter = "\n", fieldDelimiter = "|", WriteMode.OVERWRITE)
    env.execute()
  }

}
