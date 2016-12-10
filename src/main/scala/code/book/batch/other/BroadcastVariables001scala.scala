package code.book.batch.other

import java.util

import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment, _}
import org.apache.flink.configuration.Configuration


/**
  * Created by liguohua on 10/12/2016.
  */
object BroadcastVariables001scala {


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
