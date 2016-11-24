package code.book.wordcount.batch

import org.apache.flink.api.scala._

/**
  * Created by liguohua on 25/11/2016.
  */
object WordCountLocalStrings {
  def main(args: Array[String]): Unit = {
    //1.创建批处理环境
    val benv = ExecutionEnvironment.getExecutionEnvironment

    //2.准备数据
    val text = benv.fromElements(
      "To be, or not to be,that is the question",
      "Whether 'tis nobler in the mind to suffer",
      "The slings and arrows of outrageous fortune",
      "Or to take arms against a sea of troubles,")

    //3.执行运算
    val counts = text.flatMap(_.toLowerCase.split("\\W+")).map((_, 1)).groupBy(0).sum(1)

    //4.将结果打印出来
    counts.print()
  }

}
