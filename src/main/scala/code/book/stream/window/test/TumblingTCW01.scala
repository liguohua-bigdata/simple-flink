package code.book.stream.window.test

import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala.DataStream

//0.引入必要的编程元素
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}
import org.apache.flink.streaming.api.windowing.time.Time

object TumblingTCW01 {


  def main(args: Array[String]): Unit = {

    //1.创建运行环境
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    //2.定义数据流来源
    val text = env.socketTextStream("qingcheng11", 9999)


    //3.转换数据格式，text->CarWc

    val ds1: DataStream[(Long, Int, Int)] = text.map {
      (f) => {
        val tokens = f.split(",")
//        val timeFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS")
//        val eventTime: Long = timeFormat.parse(tokens(0).trim).getTime
        (tokens(1).trim.toLong, tokens(1).trim.toInt, tokens(2).trim.toInt)
      }
    }
    //4.执行统计操作，每个sensorId一个tumbling窗口，窗口的大小为5秒
    val ds2: DataStream[(Long, Int, Int)] = ds1
      .assignAscendingTimestamps(_._1)
      .keyBy(_._2)
      .timeWindow(Time.seconds(10)).max(2).name("timedwindow")

    //5.显示统计结果
    ds2.print()
    //6.触发流计算
    env.execute(this.getClass.getName)
  }

}
