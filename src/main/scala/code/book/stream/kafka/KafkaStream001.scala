package code.book.stream.kafka

/**
  * Created by liguohua on 28/11/2016.
  */

import java.util.Properties

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment

/**
  * Created by https://www.iteblog.com on 2016/5/3.
  */
object FlinkKafkaStreaming {
  def main(args: Array[String]) {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.enableCheckpointing(5000)
    val properties = new Properties()
    properties.setProperty("bootstrap.servers", "www.iteblog.com:9092")
    // only required for Kafka 0.8
    properties.setProperty("zookeeper.connect", "www.iteblog.com:2181")
    properties.setProperty("group.id", "iteblog")


//    val stream = env.addSource(new FlinkKafkaConsumer09[String]("iteblog",
//      new SimpleStringSchema(), properties))
//    stream.setParallelism(4).writeAsText("hdfs:///tmp/iteblog/data")

    env.execute("IteblogFlinkKafkaStreaming")
  }
}