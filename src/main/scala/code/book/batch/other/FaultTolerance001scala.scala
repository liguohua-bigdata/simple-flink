package code.book.batch.other

import org.apache.flink.api.scala.{ExecutionEnvironment, _}

/**
  * flink-conf.yaml:
  * execution-retries.default: 3
  * execution-retries.delay: 5 s
  */
object FaultTolerance001scala {
  def main(args: Array[String]): Unit = {

    val env = ExecutionEnvironment.createRemoteEnvironment("qingcheng13", 6123)

//    val env = ExecutionEnvironment.getExecutionEnvironment
    //失败重试3次
    env.setNumberOfExecutionRetries(3)
    //重试时延 5000 milliseconds
    env.getConfig.setExecutionRetryDelay(5000)
    val ds1 = env.fromElements(2, 5, 3, 7, 9)
    ds1.map(_ * 2).print()
  }

}
