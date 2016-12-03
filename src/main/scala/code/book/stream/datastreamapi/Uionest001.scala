package code.book.stream.datastreamapi

import org.apache.flink.streaming.api.scala._

object Uionest001 {
  def main(args: Array[String]): Unit = {
    //1.创建流处理环境
    val senv = StreamExecutionEnvironment.getExecutionEnvironment

    //2.准备数据
    val dataSteam1 = senv.fromElements(1, 3, 2, 4, 6, 5)
    val dataSteam2 = senv.fromElements(4, 7, 2, 4, 6, 5)

    //3.执行运算
    val dataStream3 = dataSteam1.union(dataSteam2)

    //4.将结果打印出来
    dataStream3.print()

    //5.触发流计算
    senv.execute(this.getClass.getName)
  }
}
