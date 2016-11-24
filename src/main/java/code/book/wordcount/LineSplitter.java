package code.book.wordcount;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

/**
 * Created by liguohua on 24/11/2016.
 */
public class LineSplitter implements FlatMapFunction<String, Tuple2<String, Integer>> {
    public void flatMap(String line, Collector<Tuple2<String, Integer>> out) {
        // 1.以一个或多个空白切割文件中的每一行
        String[] words = line.toLowerCase().split("\\W+");

        // 2.将切割的单词组成（word,1）的形式
        for (String word : words) {
            if (word.length() > 0) {
                out.collect(new Tuple2<String, Integer>(word, 1));
            }
        }
    }
}

