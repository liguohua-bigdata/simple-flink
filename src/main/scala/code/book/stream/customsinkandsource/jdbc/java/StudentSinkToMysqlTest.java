package code.book.stream.customsinkandsource.jdbc.java;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class StudentSinkToMysqlTest {
    public static void main(String[] args) throws Exception {
        //1.创建流执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //2.准备数据
        DataStream<Student> students = env.fromElements(
                new Student(11, "zhangsan", "beijing biejing", "female"),
                new Student(12, "lisi", "tainjing tianjin", "male ")
        );

        //3.将数据写入到自定义的sink中（这里是mysql）
        students.addSink(new StudentSinkToMysql());

        //4.触发流执行
        env.execute();
    }
}
