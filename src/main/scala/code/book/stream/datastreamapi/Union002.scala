package code.book.stream.datastreamapi

//0.引用必要的元素
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time

/**
input
zhangsan,18,1461756862000
lisi,25,1461756868000
zhangsan,18,1461856862000
lisi,25,1461856868000



output
zhangsan,18,1461856862000
lisi,25,1461856868000
  */
object Union002 {
  def main(args: Array[String]): Unit = {
    //0.创建运行环境
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    case class Worker(name: String, salary: Double, time: Long)

    //1.定义worker1数据流
    val text1 = env.socketTextStream("qingcheng11", 9999)
    val ds1: DataStream[Worker] = text1.map(line => {
      val spits = line.split(",")
      Worker(spits(0), spits(1).toDouble, spits(2).toLong)
    }).assignAscendingTimestamps(_.time)
    //2.定义worker2数据流
    val text2 = env.socketTextStream("qingcheng11", 9998)
    val ds2: DataStream[Worker] = text2.map(line => {
      val spits = line.split(",")
      Worker(spits(0), spits(1).toDouble, spits(2).toLong)
    }).assignAscendingTimestamps(_.time)

    //3.定义worker3数据流
    val text3 = env.socketTextStream("qingcheng11", 9997)
    val ds3: DataStream[Worker] = text3.map(line => {
      val spits = line.split(",")
      Worker(spits(0), spits(1).toDouble, spits(2).toLong)
    }).assignAscendingTimestamps(_.time)


    //4.打印结果

    val re = ds1.union(ds2, ds3).windowAll(TumblingEventTimeWindows.of(Time.seconds(3)))

    val r = re.sum(1)
    r.print()

    //5.触发计算
    env.execute("Window Stream WordCount")
  }
}


