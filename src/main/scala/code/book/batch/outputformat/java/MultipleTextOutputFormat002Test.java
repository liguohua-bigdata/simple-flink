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
 * Created by liguohua on 11/12/2016.
 */
public class MultipleTextOutputFormat002Test {
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
        MultipleTextOutputFormat003<String, String> multipleTextOutputFormat = new MultipleTextOutputFormat003<>();
        JobConf jobConf = new JobConf();
        String filePath = "hdfs://qingcheng12:9000/output/flink/MultipleTextOutputFormat/java/003";
        FileOutputFormat.setOutputPath(jobConf, new Path(filePath));
        HadoopOutputFormat<String, String> format = new HadoopOutputFormat<>(multipleTextOutputFormat, jobConf);

        //4.将数据输出出去
        data1.setParallelism(4).output(format);

        //5.触发批处理执行
        env.execute();

    }
}
