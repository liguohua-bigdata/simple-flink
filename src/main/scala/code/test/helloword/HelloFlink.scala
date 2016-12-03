package code.test.helloword

//0.引入必要的程序元素
import org.apache.flink.api.scala._

object HelloFlink {
  def main(args: Array[String]): Unit = {
    // 1.设置运行环境
    val env = ExecutionEnvironment.getExecutionEnvironment

    //2.创造测试数据
    val text = env.fromElements(
      "To be, or not to be,--that is the question:--",
      "Whether 'tis nobler in the mind to suffer",
      "The slings and arrows of outrageous fortune",
      "Or to take arms against a sea of troubles,")

    //3.进行wordcount运算
    val counts = text.flatMap(_.toLowerCase.split("\\W+"))
      .map((_, 1)).groupBy(0).sum(1)
    //4.打印测试结构
    counts.print()
  }
}
