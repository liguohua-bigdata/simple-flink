package code.book.batch.outputformat.java;

import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.hadoop.mapred.HadoopOutputFormat;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobConf;

import java.util.ArrayList;
import java.util.List;

/**
 * hadoop fs -text /output/flink/MultipleTextOutputFormat/java/001/lisi
 * Created by liguohua on 11/12/2016.
 */
public class MultipleTextOutputFormat001Test {
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


        //3.多路径输出的HadoopOutputFormat
        MultipleTextOutputFormat001<String, String> multipleTextOutputFormat = new MultipleTextOutputFormat001<>();
        JobConf jobConf = new JobConf();
        String filePath = "hdfs://qingcheng12:9000/output/flink/MultipleTextOutputFormat/java/001";
        FileOutputFormat.setOutputPath(jobConf, new Path(filePath));
        HadoopOutputFormat<String, String> format = new HadoopOutputFormat<>(multipleTextOutputFormat, jobConf);

        //4.将数据输出出去
        data1.output(format);

        //5.触发批处理执行
        env.execute();

    }
}
