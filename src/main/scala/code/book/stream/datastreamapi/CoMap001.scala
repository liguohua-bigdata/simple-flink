package code.book.stream.datastreamapi

import org.apache.flink.streaming.api.scala._

object CoMap001 {
  def main(args: Array[String]): Unit = {
    //1.创建流处理环境
    val senv = StreamExecutionEnvironment.getExecutionEnvironment

    //2.准备数据
    val dataSteam1 = senv.fromElements(1, 3, 2, 4, 6, 5)
    val dataSteam2 = senv.fromElements(40, 17, 12, 41, 16, 25)

    //3.执行运算
    val connectedStreams = dataSteam1.connect(dataSteam2)
    val dataSteam3 = connectedStreams
    //4.将结果打印出来
//    dataSteam3.print()

    //5.触发流计算
    senv.execute(this.getClass.getName)
  }
}
