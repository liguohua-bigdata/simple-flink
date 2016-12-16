package code.book.Java8;

import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;

import java.util.ArrayList;
import java.util.List;


public class Java800x {
    public static void main(String[] args) throws Exception {
        //1.创建批处理环境
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        //2.准备数据
        List<Tuple2<String, String>> list = new ArrayList<>();
        list.add(new Tuple2<>("zhangsan", "120"));
        list.add(new Tuple2<>("lisi", "123"));
        list.add(new Tuple2<>("zhangsan", "125"));
        list.add(new Tuple2<>("lisi", "207"));
        list.add(new Tuple2<>("wangwu", "315"));
        final DataSource<Tuple2<String, String>> data1 = env.fromCollection(list);

        //5.触发批处理执行
        env.execute();

    }
}
