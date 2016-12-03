package kafka;

import org.apache.flink.api.java.ExecutionEnvironment;

import java.util.Properties;

/**
 * Created by liguohua on 03/12/2016.
 */
public class KafkaTest {
    public static void main(String[] args) {
        // 1.设置运行环境
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092");
        // only required for Kafka 0.8
        properties.setProperty("zookeeper.connect", "localhost:2181");
        properties.setProperty("group.id", "test");
//
//        DataStream<String> stream = env
//                .addSource(new FlinkKafkaConsumer09<>("topic", new SimpleStringSchema(), properties))
//                .print();

    }
}
