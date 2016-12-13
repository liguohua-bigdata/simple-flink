package code.book.batch.sinksource.scala

import org.apache.flink.api.common.functions.MapFunction
import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment, _}

object Parameters002 {
  def main(args: Array[String]): Unit = {
    val env = ExecutionEnvironment.getExecutionEnvironment

    //1.准备工人数据
    case class Worker(name: String, salaryPerMonth: Double)
    val workers: DataSet[Worker] = env.fromElements(
      Worker("zhagnsan", 1356.67),
      Worker("lisi", 1476.67)
    )
    //2.准备工作月份数据，作为参数传递出去
    val month = 4

    //3.接受参数进行计算
    workers.map(new SalarySumMap(month)).print()
    class SalarySumMap(m: Int) extends MapFunction[Worker, Worker] {
      override def map(w: Worker): Worker = {
        //取出参数，取出worker进行计算
        Worker(w.name, w.salaryPerMonth * m)
      }
    }
  }
}
