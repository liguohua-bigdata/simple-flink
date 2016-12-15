package code.book.stream.datastreamapi

import org.apache.flink.streaming.api.scala._

object SplitSelect001 {
  def main(args: Array[String]): Unit = {
    //1.创建流处理环境
    val senv = StreamExecutionEnvironment.getExecutionEnvironment

    //2.准备数据
    val text = senv.fromElements(1, 3, 2, 4, 6, 5)

    //3.执行运算
    val split = text.split(
      (num: Int) =>
        (num % 2) match {
          case 0 => List("even")
          case 1 => List("odd")
        }
    )
    split.print()
    //4.将结果打印出来
    split.select("even").print()
    split.select("odd").print()
    split.select("even", "odd").print()
    //5.触发流计算
    senv.execute(this.getClass.getName)
  }
}
