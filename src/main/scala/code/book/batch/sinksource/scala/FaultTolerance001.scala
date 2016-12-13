package code.book.batch.sinksource.scala

import org.apache.flink.api.scala.{ExecutionEnvironment, _}

object FaultTolerance001 {
  def main(args: Array[String]): Unit = {
    val env = ExecutionEnvironment.getExecutionEnvironment
    //失败重试3次
    env.setNumberOfExecutionRetries(3)
    //重试时延 5000 milliseconds
    env.getConfig.setExecutionRetryDelay(5000)
    val ds1 = env.fromElements(2, 5, 3, 7, 9)
    ds1.map(_ * 2).print()
  }
}
