package code.book.Java8;

import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.util.Collector;


public class Java8001 {
    public static void main(String[] args) throws Exception {
        //1.创建批处理环境
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        //2.准备数据
        DataSet<Integer> text1 = env.fromElements(1, 2, 3);
        //3.在java8中也可以使用Lambda表达式
        text1.map(i -> i * i).print();

        //4. 在java8中，带泛型的写法
        DataSet<String> ds2 = text1.flatMap((Integer number, Collector<String> out) -> {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < number; i++) {
                builder.append("good!");
                out.collect(builder.toString());
            }
        });

        ds2.print();

        //5. 在java8中,简洁写法
        DataSet<String> ds3 = text1.flatMap((number, out) -> {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < number; i++) {
                builder.append("good!");
                out.collect(builder.toString());
            }

        });
        ds3.print();
    }
}
