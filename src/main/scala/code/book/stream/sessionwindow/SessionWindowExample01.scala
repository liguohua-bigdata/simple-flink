package code.book.stream.sessionwindow

import java.text.SimpleDateFormat

import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.assigners.ProcessingTimeSessionWindows
import org.apache.flink.streaming.api.windowing.time.Time

object SessionWindowExample01 {


  def main(args: Array[String]) {

    //1.创建执行环境，并设置为使用ProcessingTime
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime)

    //2.创建数据流，并进行数据转化
    val source = env.socketTextStream("qingcheng11", 9999)
    case class SalePrice(time: Long, boosName: String, productName: String, price: Double)
    val dst1: DataStream[SalePrice] = source.filter(_.nonEmpty).map(value => {
      val columns = value.split(",")
      val format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
      SalePrice(format.parse(columns(0).trim).getTime, columns(1).trim, columns(2).trim, columns(3).trim.toDouble)
    })
    //3.使用ProcessingTime进行求最值操作
    val dst2: DataStream[SalePrice] = dst1
      .keyBy(_.productName)
      .window(ProcessingTimeSessionWindows.withGap(Time.seconds(2)))
      .max(3)

    //4.显示结果
    dst2.print()

    //5.触发流计算
    env.execute()
  }
}