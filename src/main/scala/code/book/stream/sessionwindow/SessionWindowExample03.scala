package code.book.stream.sessionwindow

import java.text.SimpleDateFormat

import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.watermark.Watermark
import org.apache.flink.streaming.api.windowing.assigners.EventTimeSessionWindows
import org.apache.flink.streaming.api.windowing.time.Time

object SessionWindowExample03 {


  def main(args: Array[String]) {

    //1.创建执行环境，并设置为使用EventTime
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    //2.创建数据流，并进行数据转化
    val source = env.socketTextStream("qingcheng11", 9999)
    case class SalePrice(time: Long, boosName: String, productName: String, price: Double)
    val dst1: DataStream[SalePrice] = source.filter(_.nonEmpty).map(value => {
      val columns = value.split(",")
      val format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
      SalePrice(format.parse(columns(0).trim).getTime, columns(1).trim, columns(2).trim, columns(3).trim.toDouble)
    })


    val assigner = new AssignerWithPeriodicWatermarks[SalePrice] {
      var currentMaxTimestamp = 0L
      val maxOutOfOrderness = 3L

      override def getCurrentWatermark: Watermark = {
        new Watermark(currentMaxTimestamp - maxOutOfOrderness)
      }

      override def extractTimestamp(t: SalePrice, l: Long): Long = {
        val timestamp = t.time
        currentMaxTimestamp = Math.max(timestamp, currentMaxTimestamp)
        timestamp
      }
    }
    //3.使用EventTime进行求最值操作
    val dst2: DataStream[SalePrice] = dst1
      .assignTimestampsAndWatermarks(assigner)
      .keyBy(_.productName)
      .window(EventTimeSessionWindows.withGap(Time.seconds(2)))
      .max(3)

    //4.显示结果
    dst2.print()

    //5.触发流计算
    env.execute()
  }
}