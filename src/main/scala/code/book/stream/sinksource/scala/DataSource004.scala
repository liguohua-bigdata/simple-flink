package code.book.stream.sinksource.scala
import java.util.Properties
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer09
import org.apache.flink.streaming.util.serialization.SimpleStringSchema
object DataSource004 {
  def main(args: Array[String]) {

    //1指定kafka数据流的相关信息
    val zkCluster = "qingcheng11,qingcheng12,qingcheng13:2181"
    val kafkaCluster = "qingcheng11:9092,qingcheng12:9092,qingcheng13:9092"
    val kafkaTopicName = "food"
    //2.创建流处理环境
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    //3.创建kafka数据流
    val properties = new Properties()
    properties.setProperty("bootstrap.servers", kafkaCluster)
    properties.setProperty("zookeeper.connect", zkCluster)
    properties.setProperty("group.id", kafkaTopicName)

    val kafka09 = new FlinkKafkaConsumer09[String](kafkaTopicName, new SimpleStringSchema(), properties)
    //4.添加数据源addSource(kafka09)
    val text = env.addSource(kafka09).setParallelism(4)
    text.print()

    //5.触发运算
    env.execute("flink-kafka-wordcunt")
  }
}