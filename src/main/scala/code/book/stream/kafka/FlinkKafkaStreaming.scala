package code.book.stream.kafka

import java.util.Properties

import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer09
import org.apache.flink.streaming.util.serialization.SimpleStringSchema
import org.apache.log4j.{Level, Logger}



object FlinkKafkaStreaming {
  def main(args: Array[String]) {
    Logger.getLogger("org").setLevel(Level.TRACE)
    //1.创建流处理环境
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val properties = new Properties()
    properties.setProperty("bootstrap.servers", "qingcheng11:9092,qingcheng12:9092,qingcheng13:9092")
    // only required for Kafka 0.9
    val topicName = "food"
    properties.setProperty("zookeeper.connect", "qingcheng11,qingcheng12,qingcheng13:2181")
    properties.setProperty("group.id", topicName)

    val kafka09 = new FlinkKafkaConsumer09[String](topicName, new SimpleStringSchema(), properties)
    val stream = env.addSource(kafka09)
    stream.setParallelism(4).print()

    env.execute("flink-kafka-test")
//    org.apache.flink.runtime.net.ConnectionUtils

  }
}