package main.scala

import org.apache.flink.api.scala._
/**
  * Created by liguohua on 21/11/2016.
  */
object WordCountJob {
  def main(args: Array[String]) {
    // 1.设置运行环境
    val env = ExecutionEnvironment.getExecutionEnvironment

    //2.创造测试数据
    val text = env.fromElements("To be", "Whether", "The slings ande", "Or to take arm")

    //3.进行测试运算
    val counts = text.flatMap { _.toLowerCase.split("\\W+") }
      .map { (_, 1) }
      .groupBy(0)
      .sum(1)

    //4.打印测试结构
    counts.print()

  }
}
