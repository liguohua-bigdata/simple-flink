package code.book.batch.dataset.advance.api;

import org.apache.flink.api.common.functions.MapPartitionFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.MapPartitionOperator;
import org.apache.flink.util.Collector;

import java.util.Iterator;

public class MapPartitionFunction001java {
    public static void main(String[] args) throws Exception {

        // 1.设置运行环境,准备运行的数据
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        DataSet<String> text = env.fromElements("flink vs spark", "buffer vs  shuffer");

        //2.以partition为粒度，进行map操作，计算element个数
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

        //3.以partition为粒度，进行map操作，转化element内容
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

        //4.以partition为粒度，进行map操作，转化为大写并,并计算line的长度。
        //4.1定义class
        class Wc{
            private String line;
            private int lineLength;
            public Wc(String line, int lineLength) {
                this.line = line;
                this.lineLength = lineLength;
            }

            @Override
            public String toString() {
                return "Wc{" + "line='" + line + '\'' + ", lineLength='" + lineLength + '\'' + '}';
            }
        }
        //4.2转化成class类型
        final MapPartitionOperator<String, Wc> text4 = text.mapPartition(new MapPartitionFunction<String, Wc>() {
            @Override
            public void mapPartition(Iterable<String> iterable, Collector<Wc> collector) throws Exception {
                Iterator<String> itor = iterable.iterator();
                while (itor.hasNext()) {
                    String  s = itor.next();
                    collector.collect(new Wc(s.toUpperCase(), s.length()));
                }

            }
        });
        text4.print();

    }
}

