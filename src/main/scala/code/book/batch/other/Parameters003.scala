package code.book.batch.other

import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment, _}
import org.apache.flink.configuration.Configuration

object Parameters003 {
  def main(args: Array[String]): Unit = {
    val env = ExecutionEnvironment.getExecutionEnvironment

    //1.准备工人数据
    case class Worker(name: String, salaryPerMonth: Double)
    val workers: DataSet[Worker] = env.fromElements(
      Worker("zhagnsan", 1356.67),
      Worker("lisi", 1476.67)
    )
    //2.准备工作月份数据，作为参数用Configuration传递出去
    val conf = new Configuration()
    conf.setString("month", "4")
    env.getConfig.setGlobalJobParameters(conf)

    //3.接受参数进行计算（如果要用Configuration传参，需要用RichFunction接受）
    workers.map(new RichMapFunction[Worker, Worker] {
      private var m = 0

      override def open(parameters: Configuration): Unit = {
        super.open(parameters)
        //3.1获取Configuration传递过来的参数
        val globalParams = this.getRuntimeContext.getExecutionConfig.getGlobalJobParameters
        m = globalParams.toMap.get("month").trim.toInt
      }

      override def map(w: Worker): Worker = {
        //3.2计算最新工人工资信息
        Worker(w.name, w.salaryPerMonth * m)
      }
    }).print
  }
}
