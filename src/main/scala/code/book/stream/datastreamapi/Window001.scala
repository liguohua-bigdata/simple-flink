package code.book.stream.datastreamapi


import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time

object Window001 {
  def main(args: Array[String]): Unit = {
    //1.创建流处理环境
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    //置为使用EventTime
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    //2.创建数据流，并进行数据转化
    val source = env.fromElements("zhangsan,18,1461756862000","lisi,18,1461756868000")
    case class Stu( name: String,age :Int,time: Long)
    val dst1: DataStream[Stu] = source.map(value => {
      val columns = value.split(",")
       Stu(columns(0), columns(1).trim.toInt, columns(2).trim.toLong)
    })

    //3.使用EventTime进行求最值操作
    val dst2: DataStream[ Stu] = dst1
      //提取消息中的时间戳属性
      .assignAscendingTimestamps(_.time)
      .keyBy(_.name)
      .window(TumblingEventTimeWindows.of(Time.seconds(5)))
      .max("age")

    //4.显示结果
    dst2.printToErr()

    //5.触发流计算
    env.execute(this.getClass.getName)
  }
}
