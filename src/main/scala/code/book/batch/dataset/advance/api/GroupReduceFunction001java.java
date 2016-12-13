package code.book.batch.dataset.advance.api;

import org.apache.flink.api.common.functions.GroupReduceFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;
import java.util.Iterator;

import static org.apache.flink.api.java.ExecutionEnvironment.getExecutionEnvironment;

public class GroupReduceFunction001java {
    public static void main(String[] args) throws Exception {
        // 1.设置运行环境，准备运行的数据
        final ExecutionEnvironment env = getExecutionEnvironment();
        DataSet<Integer> text = env.fromElements(1, 2, 3, 4, 5, 6, 7);

        //2.对DataSet的元素进行合并，这里是计算累加和
        DataSet<Integer> text2 = text.reduceGroup(new GroupReduceFunction<Integer, Integer>() {
            @Override
            public void reduce(Iterable<Integer> iterable, Collector<Integer> collector) throws Exception {
                int sum = 0;
                Iterator<Integer> itor = iterable.iterator();
                while (itor.hasNext()) {
                    sum += itor.next();
                }
                collector.collect(sum);
            }
        });
        text2.print();

        //3.对DataSet的元素进行分组合并，这里是分别计算偶数和奇数的累加和
        DataSet<Tuple2<Integer, Integer>> text3 = text.reduceGroup(new GroupReduceFunction<Integer, Tuple2<Integer, Integer>>() {
            @Override
            public void reduce(Iterable<Integer> iterable, Collector<Tuple2<Integer, Integer>> collector) throws Exception {
                int sum0 = 0;
                int sum1 = 0;
                Iterator<Integer> itor = iterable.iterator();
                while (itor.hasNext()) {
                    int v = itor.next();
                    if (v % 2 == 0) {
                        sum0 += v;
                    } else {
                        sum1 += v;
                    }
                }
                collector.collect(new Tuple2<Integer, Integer>(sum0, sum1));
            }
        });
        text3.print();

        //4.对DataSet的元素进行分组合并，这里是对分组后的数据进行合并操作，统计每个人的工资总和（每个分组会合并出一个结果）
        DataSet<Tuple2<String, Integer>> data = env.fromElements(new Tuple2("zhangsan", 1000), new Tuple2("lisi", 1001), new Tuple2("zhangsan", 3000), new Tuple2("lisi", 1002));
        //4.1根据name进行分组
        DataSet<Tuple2<String, Integer>> data2 = data.groupBy(0).reduceGroup(new GroupReduceFunction<Tuple2<String, Integer>, Tuple2<String, Integer>>() {
            @Override
            public void reduce(Iterable<Tuple2<String, Integer>> iterable, Collector<Tuple2<String, Integer>> collector) throws Exception {
                int salary = 0;
                String name = "";
                Iterator<Tuple2<String, Integer>> itor = iterable.iterator();
                //4.2统计每个人的工资总和
                while (itor.hasNext()) {
                    Tuple2<String, Integer> t = itor.next();
                    name = t.f0;
                    salary += t.f1;
                }
                collector.collect(new Tuple2(name, salary));
            }
        });
        data2.print();
    }
}
