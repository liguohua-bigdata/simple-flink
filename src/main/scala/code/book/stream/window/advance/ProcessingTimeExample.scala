package code.book.stream.window.advance

import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time

object ProcessingTimeExample {
  def main(args: Array[String]) {

    //1.创建执行环境，并设置为使用ProcessingTime
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    //设置为使用ProcessingTime
    env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime)

    //2.创建数据流，并进行数据转化
    val source = env.socketTextStream("qingcheng11", 9999)
    case class SalePrice(time: Long, boosName: String, productName: String, price: Double)
    val dst1: DataStream[SalePrice] = source.map(value => {
      val columns = value.split(",")
      SalePrice(columns(0).toLong, columns(1), columns(2), columns(3).toDouble)
    })

    //3.使用ProcessingTime进行求最值操作,不需要提取消息中的时间属性
    val dst2: DataStream[SalePrice] = dst1
      .keyBy(_.productName)
      .timeWindow(Time.seconds(5))
      .max("price")

    //4.显示结果
    dst2.print()

    //5.触发流计算
    env.execute()
  }
}