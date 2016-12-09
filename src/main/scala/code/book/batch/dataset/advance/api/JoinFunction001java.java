package code.book.batch.dataset.advance.api;

import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.Tuple4;

public class JoinFunction001java {

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
        DataSet<Tuple4<String, String, String, String>> text2 = authors.join(posts).where(1).equalTo(1).with(new JoinFunction<Tuple3<String, String, String>, Tuple2<String, String>, Tuple4<String, String, String, String>>() {
            @Override
            public Tuple4<String, String, String, String> join(Tuple3<String, String, String> author, Tuple2<String, String> post) throws Exception {
                //AuthorArchive (title, id, name, email)
                return new Tuple4<>(post.f0, author.f0, author.f1, author.f2);
            }
        });

        //3.显示结果
        text2.print();
    }
}
