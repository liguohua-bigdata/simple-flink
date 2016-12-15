package code.book.stream.datastreamapi

import org.apache.flink.streaming.api.scala._

/**
Rolling aggregations on a keyed data stream.
The difference between min and minBy is that min returns the minimun value, whereas minBy returns the element that has the minimum value in this field (same for max and maxBy).
  */
object Aggregations001 {
  def main(args: Array[String]): Unit = {
    //1.创建流处理环境
    val senv = StreamExecutionEnvironment.getExecutionEnvironment

    //2.准备数据
    val text = senv.fromElements(
      ("zhangsan", 240),
      ("lisi", 840),
      ("zhangsan", 240),
      ("wangwu", 1240),
      ("zhangsan", 400))

    //3.执行运算
    val keyByStream = text.keyBy(0)

    //4.将结果打印出来
    keyByStream.min(1).print()
    keyByStream.minBy(1).print()

    keyByStream.max(1).print()
    keyByStream.maxBy(1).print()

    keyByStream.sum(1).print()




    //5.触发流计算
    senv.execute(this.getClass.getName)
  }
}
