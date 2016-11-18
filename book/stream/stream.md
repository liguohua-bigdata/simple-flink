
import java.util.Properties
import org.apache.flink.streaming.api.{CheckpointingMode, TimeCharacteristic}
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.flink.streaming.util.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala._

val ZOOKEEPER_HOST = "qingcheng11:2181,qingcheng12:2181,qingcheng13:2181"
val KAFKA_BROKER = "qingcheng11:9092,qingcheng12:9092,qingcheng13:9092"
val TRANSACTION_GROUP = "transaction"

senv.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
senv.enableCheckpointing(1000)
senv.getCheckpointConfig.setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE)

val kafkaProps = new Properties()
kafkaProps.setProperty("zookeeper.connect", ZOOKEEPER_HOST)
kafkaProps.setProperty("bootstrap.servers", KAFKA_BROKER)
kafkaProps.setProperty("group.id", TRANSACTION_GROUP)

//topicd的名字是new，schema默认使用SimpleStringSchema()即可
val transaction = senv .addSource(new FlinkKafkaConsumer08[String]("fold", new SimpleStringSchema(), kafkaProps))
transaction.print()
senv.execute()



http://blog.csdn.net/lmalds/article/details/51780950