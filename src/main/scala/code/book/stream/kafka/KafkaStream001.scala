package code.book.stream.kafka

import java.util.Properties

import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.util.serialization.SimpleStringSchema

/**
  * Created by liguohua on 28/11/2016.
  */
object KafkaStream001 {
  def main(args: Array[String]): Unit = {
    val properties = new Properties();
    properties.setProperty("bootstrap.servers", "localhost:9092");
    // only required for Kafka 0.8
    properties.setProperty("zookeeper.connect", "localhost:2181");
    properties.setProperty("group.id", "test");

    //1.创建流处理环境
    val senv = StreamExecutionEnvironment.getExecutionEnvironment
    val stream = senv.addSource(new FlinkKafkaConsumer10[String]("topic", new SimpleStringSchema(), properties))
      .print
  }

}
