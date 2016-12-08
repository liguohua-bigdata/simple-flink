package code.book.batch.dataset.advance.api;

import org.apache.flink.api.common.functions.MapPartitionFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.MapPartitionOperator;
import org.apache.flink.util.Collector;

public class MapPartition001java {
    public static void main(String[] args) throws Exception {

        // 1.设置运行环境
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        // 2.准备运行的数据
        DataSet<String> text = env.fromElements("flink vs spark", "buffer vs  suffer");

        //3.以partition为粒度，进行map操作，计算element个数
        final MapPartitionOperator<String, Long> text2 = text.mapPartition(new MapPartitionFunction<String, Long>() {
            @Override
            public void mapPartition(Iterable<String> iterable, Collector<Long> collector) throws Exception {
                long c = 0;
                for (String s : iterable) {
                    c++;
                }
                collector.collect(c);
            }
        });
        text2.print();

        //4.以partition为粒度，进行map操作，转化element内容
        final MapPartitionOperator<String, String> text3 = text.mapPartition(new MapPartitionFunction<String, String>() {
            @Override
            public void mapPartition(Iterable<String> iterable, Collector<String> collector) throws Exception {
                for (String s : iterable) {
                    s = s.toUpperCase() + "--##bigdata##";
                    collector.collect(s);
                }
            }
        });
        text3.print();

    }
}

