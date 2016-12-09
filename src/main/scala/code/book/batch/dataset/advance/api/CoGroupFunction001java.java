package code.book.batch.dataset.advance.api;

import org.apache.flink.api.common.functions.CoGroupFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.util.Collector;

import java.util.Iterator;


public class CoGroupFunction001java {

    public static void main(String[] args) throws Exception {
        // 1.设置运行环境，准备运行的数据
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        // Author (id, name, email)
        DataSet<Tuple3<String, String, String>> authors = env.fromElements(
                new Tuple3<>("A001", "zhangsan", "zhangsan@qq.com"),
                new Tuple3<>("A001", "lisi", "lisi@qq.com"),
                new Tuple3<>("A001", "wangwu", "wangwu@qq.com")
        );
        //Archive (title, author name)
        DataSet<Tuple2<String, String>> posts = env.fromElements(
                new Tuple2<>("P001", "zhangsan"),
                new Tuple2<>("P002", "lisi"),
                new Tuple2<>("P003", "wangwu"),
                new Tuple2<>("P004", "lisi")

        );
        // 2.用自定义的方式进行join操作
        DataSet<Tuple4<String, String, String, String
                >> text2 = authors.coGroup(posts).where(1).equalTo(1).with(new CoGroupFunction<Tuple3<String, String, String>, Tuple2<String, String>, Tuple4<String, String, String, String>>() {

            @Override
            public void coGroup(Iterable<Tuple3<String, String, String>> authors, Iterable<Tuple2<String, String>> posts, Collector<Tuple4<String, String, String, String>> collector) throws Exception {
                //取出Author信息
                Tuple3<String, String, String> at = null;
                Iterator<Tuple3<String, String, String>> aitor = authors.iterator();
                while (aitor.hasNext()) {
                    at = aitor.next();
                }
                //取出Archive信息
                Tuple2<String, String> pt = null;
                Iterator<Tuple2<String, String>> pitor = posts.iterator();
                while (pitor.hasNext()) {
                    pt = pitor.next();
                }
                //重新组装并发送AuthorArchive信息
                collector.collect(new Tuple4<>(pt.f0, at.f0, at.f1, at.f2));
            }
        });

        //3.显示结果
        text2.print();
    }
}
