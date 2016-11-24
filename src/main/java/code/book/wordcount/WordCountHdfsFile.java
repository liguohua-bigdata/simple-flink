package code.book.wordcount;


import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.aggregation.Aggregations;
import org.apache.flink.api.java.tuple.Tuple2;


/**
 * Created by liguohua on 24/11/2016.
 */
public class WordCountHdfsFile {
    public static void main(String[] args) throws Exception {
        // 1.设置运行环境
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        // 2.准备运行的数据
        DataSet<String> text = env.readTextFile("hdfs:///input/flink/README.txt");

        //3.执行运算操作
        DataSet<Tuple2<String, Integer>> counts = text.flatMap(new LineSplitter())
                .groupBy(0).aggregate(Aggregations.SUM, 1);

        // 4.输出计算结果
        counts.print();
    }
}

