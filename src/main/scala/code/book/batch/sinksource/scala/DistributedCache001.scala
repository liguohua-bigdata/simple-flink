package code.book.batch.sinksource.scala

import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment, _}
import org.apache.flink.configuration.Configuration

import scala.collection.mutable.ListBuffer
import scala.io.Source

/**
  * 文件内容
  * zhagnsan:4
  * lisi:5
  */
object DistributedCache001 {
  def main(args: Array[String]): Unit = {
    val env = ExecutionEnvironment.getExecutionEnvironment
    //1.准备缓存数据，
    val path = "file:///Users/liguohua/Documents/F/code/idea/git/simple-flink/src/main/scala/code/book/batch/sinksource/scala/test"
    env.registerCachedFile(path, "MyTestFile")

    //2.准备工人数据
    case class Worker(name: String, salaryPerMonth: Double)
    val workers: DataSet[Worker] = env.fromElements(
      Worker("zhagnsan", 1356.67),
      Worker("lisi", 1476.67)
    )

    //3.使用缓存数据和工人数据做计算
    workers.map(new MyMapper()).print()
    class MyMapper() extends RichMapFunction[Worker, Worker] {

      private var lines: ListBuffer[String] = new ListBuffer[String]

      //3.1在open方法中获取缓存文件
      override def open(parameters: Configuration): Unit = {
        super.open(parameters)
        //access cached file via RuntimeContext and DistributedCache
        val myFile = getRuntimeContext.getDistributedCache.getFile("MyTestFile")
        val lines = Source.fromFile(myFile.getAbsolutePath).getLines()
        lines.foreach(f = line => {
          this.lines.append(line)
        })
      }

      //3.2在map方法中使用获取到的缓存文件内容
      override def map(worker: Worker): Worker = {
        var name = ""
        var month = 0
        //分解文件中的内容
        for (s <- this.lines) {
          val tokens = s.split(":")
          if (tokens.length == 2) {
            name = tokens(0).trim
            if (name.equalsIgnoreCase(worker.name)) {
              month = tokens(1).trim.toInt
            }
          }
          //找到满足条件的信息
          if (name.nonEmpty && month > 0.0) {
            return Worker(worker.name, worker.salaryPerMonth * month)
          }
        }
        //没有满足条件的信息
        Worker(worker.name, worker.salaryPerMonth * month)
      }
    }
  }
}
