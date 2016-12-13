##1.flink中的广播变量
```
flink支持将变量广播到worker上，以供程序运算使用。
```
###执行程序
```scala
package code.book.batch.sinksource.scala
import java.util
import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment, _}
import org.apache.flink.configuration.Configuration

object BroadcastVariables001 {
  def main(args: Array[String]): Unit = {
    val env = ExecutionEnvironment.getExecutionEnvironment
    //1.准备工人数据（用于map）
    case class Worker(name: String, salaryPerMonth: Double)
    val workers: DataSet[Worker] = env.fromElements(
      Worker("zhagnsan", 1356.67),
      Worker("lisi", 1476.67)
    )
    //2准备统计数据(用于广播，通过withBroadcastSet进行广播)
    case class Count(name: String, month: Int)
    val counts: DataSet[Count] = env.fromElements(
      Count("zhagnsan", 4),
      Count("lisi", 5)
    )

    //3.使用map数据和广播数据进行计算
    workers.map(new RichMapFunction[Worker, Worker] {
      private var cwork: util.List[Count] = null

      override def open(parameters: Configuration): Unit = {
        super.open(parameters)
        // 3.1 访问广播数据
        cwork = getRuntimeContext.getBroadcastVariable[Count]("countWorkInfo")
      }

      override def map(w: Worker): Worker = {
        //3.2解析广播数据
        var i = 0
        while (i < cwork.size()) {
          val c = cwork.get(i)
          i += 1
          if (c.name.equalsIgnoreCase(w.name)) {
            //有相应的信息的返回值
            return Worker(w.name, w.salaryPerMonth * c.month)
          }
        }
        //无相应的信息的返回值
        Worker("###", 0)
      }
    }).withBroadcastSet(counts, "countWorkInfo").print()
  }
}
```
###执行效果
```
Worker(zhagnsan,5426.68)
Worker(lisi,7383.35)
```
##2.flink中的分布式缓存
```
flink支持将文件，分布式缓存到worker节点，以便程序计算使用。
```

###执行程序
```scala
package code.book.batch.sinksource.scala

import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment, _}
import org.apache.flink.configuration.Configuration

import scala.collection.mutable.ListBuffer
import scala.io.Source

/**
  * hdfs:///input/flink/workcount.txt文件内容如下：
  * zhagnsan:4
  * lisi:5
  */
object DistributedCache001 {
  def main(args: Array[String]): Unit = {
    val env = ExecutionEnvironment.getExecutionEnvironment
    //1.准备缓存数据，
    val path = "hdfs:///input/flink/workcount.txt"
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
```
###执行效果
```
Worker(zhagnsan,5426.68)
Worker(lisi,7383.35)
```
