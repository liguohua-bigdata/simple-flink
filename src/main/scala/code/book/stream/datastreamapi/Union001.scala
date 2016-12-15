package code.book.stream.datastreamapi

//0.引用必要的元素
import java.util.Date

import org.apache.flink.streaming.api.scala._

object Union001 {
  def main(args: Array[String]): Unit = {
    //0.创建运行环境
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    case class Worker(name: String, age: Int)

    //1.定义worker1数据流
    val text1 = env.socketTextStream("qingcheng11", 9999)
    val ds1: DataStream[Worker] = text1.map(line => {
      val spits = line.split(",")
      Worker(spits(0), spits(1).toInt)
    }).assignAscendingTimestamps(w => new Date().getTime)
    //2.定义worker2数据流
    val text2 = env.socketTextStream("qingcheng11", 9998)
    val ds2: DataStream[Worker] = text2.map(line => {
      val spits = line.split(",")
      Worker(spits(0), spits(1).toInt)
    }).assignAscendingTimestamps(s => new Date().getTime)

    //3.定义worker3数据流
    val text3 = env.socketTextStream("qingcheng11", 9997)
    val ds3: DataStream[Worker] = text3.map(line => {
      val spits = line.split(",")
      Worker(spits(0), spits(1).toInt)
    }).assignAscendingTimestamps(s => new Date().getTime)

    //4.打印结果
    val re = ds1.union(ds2, ds3)
    re.print()
    //5.触发计算
    env.execute("Window Stream WordCount")
  }
}
