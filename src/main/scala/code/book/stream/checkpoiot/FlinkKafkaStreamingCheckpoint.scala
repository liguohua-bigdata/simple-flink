package code.book.stream.checkpoiot

/**
  * Created by liguohua on 08/12/2016.
  */

import java.util.Properties

import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer09
import org.apache.flink.streaming.util.serialization.SimpleStringSchema
import org.apache.log4j.{Level, Logger}

object FlinkKafkaStreamingCheckpoint {
  def main(args: Array[String]): Unit = {
    //1.关闭日志，可以减少不必要的日志输出
    Logger.getLogger("org").setLevel(Level.OFF)

    //2指定kafka数据流的相关信息
    val zkCluster = "qingcheng11,qingcheng12,qingcheng13:2181"
    val kafkaCluster = "qingcheng11:9092,qingcheng12:9092,qingcheng13:9092"
    val kafkaTopicName = "food"
    //3.创建流处理环境，并开启Checkpoin操作，checkpoint every 3000 msecs
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    //    val env = StreamExecutionEnvironment.createRemoteEnvironment("qingcheng13",60344)
    env.enableCheckpointing(3000)

    //4.创建kafka数据流
    val properties = new Properties()
    properties.setProperty("bootstrap.servers", kafkaCluster)
    properties.setProperty("zookeeper.connect", zkCluster)
    properties.setProperty("group.id", kafkaTopicName)

    val kafka09 = new FlinkKafkaConsumer09[String](kafkaTopicName, new SimpleStringSchema(), properties)
    val text = env.addSource(kafka09).setParallelism(4)

    //5.执行运算
    val counts = text.flatMap(_.toLowerCase.split("\\W+")).map((_, 1)).keyBy(0).sum(1)
    counts.print()

    env.execute(this.getClass.getName)



  }
}
