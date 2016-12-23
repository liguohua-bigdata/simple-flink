package code.book.stream.window.advance

import java.util.Properties

import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer09
import org.apache.flink.streaming.util.serialization.SimpleStringSchema

object EventTimeExample02 {
  def main(args: Array[String]) {

    //1指定kafka数据流的相关信息
    val zkCluster = "qingcheng11,qingcheng12,qingcheng13:2181"
    val kafkaCluster = "qingcheng11:9092,qingcheng12:9092,qingcheng13:9092"
    val kafkaTopicName = "food"
    //2.创建流处理环境
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    //3.创建kafka数据流
    val properties = new Properties()
    properties.setProperty("bootstrap.servers", kafkaCluster)
    properties.setProperty("zookeeper.connect", zkCluster)
    properties.setProperty("group.id", kafkaTopicName)

    val kafka09 = new FlinkKafkaConsumer09[String](kafkaTopicName, new SimpleStringSchema(), properties)
    //4.添加数据源addSource(kafka09)
    val text = env.addSource(kafka09).setParallelism(4)
    text.print()

    case class SalePrice(time: Long, boosName: String, productName: String, price: Double)
    val dst1: DataStream[SalePrice] = text.map(value => {
      val columns = value.split(",")
      SalePrice(columns(0).toLong, columns(1), columns(2), columns(3).toDouble)
    })

    //3.使用EventTime进行求最值操作
    val dst2: DataStream[SalePrice] = dst1
      .assignAscendingTimestamps(_.time)
      .keyBy(_.productName)
      //.timeWindow(Time.seconds(5))//设置window方法一
      .window(TumblingEventTimeWindows.of(Time.seconds(5))) //设置window方法二
      .max("price")

    //4.显示结果
    dst2.print()

    //5.触发流计算
    env.execute()
  }
}