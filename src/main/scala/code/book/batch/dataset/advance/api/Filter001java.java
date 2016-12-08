package code.book.batch.dataset.advance.api;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;

public class Filter001java {
    public static void main(String[] args) throws Exception {
        // 1.设置运行环境，准备运行的数据
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        DataSet<Integer> text = env.fromElements(2, 4, 7, 8, 9, 6);

        //2.对DataSet的元素进行过滤，筛选出偶数元素
        DataSet<Integer> text2 =text.filter(new FilterFunction<Integer>() {
            @Override
            public boolean filter(Integer e) throws Exception {
                return e%2==0;
            }
        });
        text2.print();

        //3.对DataSet的元素进行过滤，筛选出大于5的元素
        DataSet<Integer> text3 =text.filter(new FilterFunction<Integer>() {
            @Override
            public boolean filter(Integer e) throws Exception {
                return e>5;
            }
        });
        text3.print();
    }
}
