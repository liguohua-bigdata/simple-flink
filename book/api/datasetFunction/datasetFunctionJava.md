#一、Flink DateSet定制API详解(JAVA版)
##Map
```
以element为粒度，对element进行1：1的转化
```

####执行程序：
```java
package code.book.batch.dataset.advance.api;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;

public class MapFunction001java {
    public static void main(String[] args) throws Exception {
        // 1.设置运行环境，准备运行的数据
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        DataSet<String> text = env.fromElements("flink vs spark", "buffer vs  shuffle");

        // 2.以element为粒度，将element进行map操作，转化为大写并添加后缀字符串"--##bigdata##"
        DataSet<String> text2 = text.map(new MapFunction<String, String>() {
            @Override
            public String map(String s) throws Exception {
                return s.toUpperCase() + "--##bigdata##";
            }
        });
        text2.print();

        // 4.以element为粒度，将element进行map操作，转化为大写并,并计算line的长度。
        DataSet< Tuple2<String, Integer>> text3= text.map(
        new MapFunction<String, Tuple2<String,Integer> >() {
            @Override
            public Tuple2<String, Integer> map(String s) throws Exception {
                //转化为大写并,并计算矩阵的长度。
                return new Tuple2<String, Integer>(s.toUpperCase(),s.length());
            }
        });
        text3.print();

        // 4.以element为粒度，将element进行map操作，转化为大写并,并计算line的长度。
        //4.1定义class
        class Wc{
            private String line;
            private int lineLength;
            public Wc(String line, int lineLength) {
                this.line = line;
                this.lineLength = lineLength;
            }

            @Override
            public String toString() {
                return "Wc{" + "line='" + line + '\'' + ", lineLength='" + lineLength + '\'' + '}';
            }
        }
         //4.2转化成class类型
        DataSet<Wc> text4= text.map(new MapFunction<String, Wc>() {
            @Override
            public Wc map(String s) throws Exception {
                return new Wc(s.toUpperCase(),s.length());
            }
        });
        text4.print();
    }
}

```
####执行结果：
```java
text2.print();
FLINK VS SPARK--##bigdata##
BUFFER VS  SHUFFLE--##bigdata##

text3.print();
(FLINK VS SPARK,14)
(BUFFER VS  SHUFFLE,18)

text4.print();
Wc{line='FLINK VS SPARK', lineLength='14'}
Wc{line='BUFFER VS  SHUFFLE', lineLength='18'}
```

##MapPartition
```
以element为粒度，对element进行1：n的转化。
```

####执行程序：
```java
package code.book.batch.dataset.advance.api;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.MapPartitionFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.MapPartitionOperator;
import org.apache.flink.util.Collector;

public class MapPartitionFunction001java {
    public static void main(String[] args) throws Exception {

        // 1.设置运行环境,准备运行的数据
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        DataSet<String> text = env.fromElements("flink vs spark", "buffer vs  shuffer");

        //2.以partition为粒度，进行map操作，计算element个数
        final MapPartitionOperator<String, Long> text2 = text.mapPartition(
        new MapPartitionFunction<String, Long>() {
            @Override
            public void mapPartition(Iterable<String> iterable, Collector<Long> collector)
            throws Exception {
                long c = 0;
                for (String s : iterable) {
                    c++;
                }
                collector.collect(c);
            }
        });
        text2.print();

        //3.以partition为粒度，进行map操作，转化element内容
        final MapPartitionOperator<String, String> text3 = text.mapPartition(
        new MapPartitionFunction<String, String>() {
            @Override
            public void mapPartition(Iterable<String> iterable, Collector<String> collector)
            throws Exception {
                for (String s : iterable) {
                    s = s.toUpperCase() + "--##bigdata##";
                    collector.collect(s);
                }
            }
        });
        text3.print();

        //4.以partition为粒度，进行map操作，转化为大写并,并计算line的长度。
        //4.1定义class
        class Wc{
            private String line;
            private int lineLength;
            public Wc(String line, int lineLength) {
                this.line = line;
                this.lineLength = lineLength;
            }

            @Override
            public String toString() {
                return "Wc{" + "line='" + line + '\'' + ", lineLength='" + lineLength + '\'' + '}';
            }
        }
        //4.2转化成class类型
        final MapPartitionOperator<String, Wc> text4 = text.mapPartition(
        new MapPartitionFunction<String, Wc>() {
            @Override
            public void mapPartition(Iterable<String> iterable, Collector<Wc> collector) throws Exception {
                Iterator<String> itor = iterable.iterator();
                while (itor.hasNext()) {
                    String  s = itor.next();
                    collector.collect(new Wc(s.toUpperCase(), s.length()));
                }

            }
        });
        text4.print();
    }
}

```
####执行结果：
```java
text2.print();
2

text3.print();
FLINK VS SPARK--##bigdata##
BUFFER VS  SHUFFER--##bigdata##

text4.print();
Wc{line='FLINK VS SPARK', lineLength='14'}
Wc{line='BUFFER VS  SHUFFER', lineLength='18'}
```


##flatMap
```
以element为粒度，对element进行1：n的转化。
```
####执行程序：
```java
package code.book.batch.dataset.advance.api;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.util.Collector;

import java.util.List;
public class FlatMapFunction001java {
    public static void main(String[] args) throws Exception {
        // 1.设置运行环境，准备运行的数据
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        DataSet<String> text = env.fromElements("flink vs spark", "buffer vs  shuffle");

        // 2.以element为粒度，将element进行map操作，转化为大写并添加后缀字符串"--##bigdata##"
        DataSet<String> text2 = text.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public void flatMap(String s, Collector<String> collector) throws Exception {
                collector.collect(s.toUpperCase() + "--##bigdata##");
            }
        });
        text2.print();

        // 3.以element为粒度，将element进行map操作，转化为大写并添加后缀字符串"--##bigdata##"
        DataSet<String[]> text3 = text.flatMap(new FlatMapFunction<String, String[]>() {
            @Override
            public void flatMap(String s, Collector<String[]> collector) throws Exception {
                collector.collect(s.toUpperCase().split("\\s+"));
            }
        });
        final List<String[]> collect = text3.collect();
        //显示结果，使用Lambda表达式的写法
        collect.forEach(arr -> {
            for (String token : arr) {
                System.out.println(token);
            }
        });
        //显示结果，不使用Lambda表达式的写法
        for (String[] arr : collect) {
            for (String token : arr) {
                System.out.println(token);
            }
        }
    }
}
```
####执行结果：
```java
text2.print();
FLINK VS SPARK--##bigdata##
BUFFER VS  SHUFFLE--##bigdata##

collect.forEach(arr -> {
for (String token : arr) {System.out.println(token);}});
FLINK
VS
SPARK
BUFFER
VS
SHUFFLE
```


##filter
```
以element为粒度，对element进行过滤操作。将满足过滤条件的element组成新的DataSet
```
####执行程序：
```java
package code.book.batch.dataset.advance.api;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;

public class FilterFunction001java {
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
```
####执行结果：
```java
text2.print()
2
4
8
6

text3.print()
7
8
9
6
```

##Reduce
```
以element为粒度，对element进行合并操作。最后只能形成一个结果。
```
####执行程序：
```java
package code.book.batch.dataset.advance.api;

import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;

public class ReduceFunction001java {
    public static void main(String[] args) throws Exception {
        // 1.设置运行环境，准备运行的数据
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        DataSet<Integer> text = env.fromElements(1, 2, 3, 4, 5, 6,7);

        //2.对DataSet的元素进行合并，这里是计算累加和
        DataSet<Integer> text2 = text.reduce(new ReduceFunction<Integer>() {
            @Override
            public Integer reduce(Integer intermediateResult, Integer next) throws Exception {
                return intermediateResult + next;
            }
        });
        text2.print();

        //3.对DataSet的元素进行合并，这里是计算累乘积
        DataSet<Integer> text3 = text.reduce(new ReduceFunction<Integer>() {
            @Override
            public Integer reduce(Integer intermediateResult, Integer next) throws Exception {
                return intermediateResult * next;
            }
        });
        text3.print();

        //4.对DataSet的元素进行合并，逻辑可以写的很复杂
        DataSet<Integer> text4 = text.reduce(new ReduceFunction<Integer>() {
            @Override
            public Integer reduce(Integer intermediateResult, Integer next) throws Exception {
                if (intermediateResult % 2 == 0) {
                    return intermediateResult + next;
                } else {
                    return intermediateResult * next;
                }
            }
        });
        text4.print();

        //5.对DataSet的元素进行合并，可以看出intermediateResult是临时合并结果，next是下一个元素
        DataSet<Integer> text5 = text.reduce(new ReduceFunction<Integer>() {
            @Override
            public Integer reduce(Integer intermediateResult, Integer next) throws Exception {
                System.out.println("intermediateResult=" + intermediateResult + " ,next=" + next);
                return intermediateResult + next;
            }
        });
        text5.collect();
    }
}
```
####执行结果：
```java
text2.print()
28

text3.print()
5040

text4.print()
157

text5.print()
intermediateResult=1 ,next=2
intermediateResult=3 ,next=3
intermediateResult=6 ,next=4
intermediateResult=10 ,next=5
intermediateResult=15 ,next=6
intermediateResult=21 ,next=7
```


##reduceGroup
```
对每一组的元素分别进行合并操作。与reduce类似，不过它能为每一组产生一个结果。
如果没有分组，就当作一个分组，此时和reduce一样，只会产生一个结果。
```
####执行程序：
```java
package code.book.batch.dataset.advance.api;

import org.apache.flink.api.common.functions.GroupReduceFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;
import java.util.Iterator;

public class GroupReduceFunction001java {
    public static void main(String[] args) throws Exception {
        // 1.设置运行环境，准备运行的数据
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        DataSet<Integer> text = env.fromElements(1, 2, 3, 4, 5, 6, 7);

        //2.对DataSet的元素进行合并，这里是计算累加和
        DataSet<Integer> text2 = text.reduceGroup(new GroupReduceFunction<Integer, Integer>() {
            @Override
            public void reduce(Iterable<Integer> iterable, 
            Collector<Integer> collector) throws Exception {
                int sum = 0;
                Iterator<Integer> itor = iterable.iterator();
                while (itor.hasNext()) {
                    sum += itor.next();
                }
                collector.collect(sum);
            }
        });
        text2.print();

        //3.对DataSet的元素进行分组合并，这里是分别计算偶数和奇数的累加和
        DataSet<Tuple2<Integer, Integer>> text3 = text.reduceGroup(
        new GroupReduceFunction<Integer, Tuple2<Integer, Integer>>() {
            @Override
            public void reduce(Iterable<Integer> iterable,
            Collector<Tuple2<Integer, Integer>> collector)throws Exception {
                int sum0 = 0;
                int sum1 = 0;
                Iterator<Integer> itor = iterable.iterator();
                while (itor.hasNext()) {
                    int v = itor.next();
                    if (v % 2 == 0) {
                        sum0 += v;
                    } else {
                        sum1 += v;
                    }
                }
                collector.collect(new Tuple2<Integer, Integer>(sum0, sum1));
            }
        });
        text3.print();

        //4.对DataSet的元素进行分组合并，这里是对分组后的数据进行合并操作，统计每个人的工资总和
        //（每个分组会合并出一个结果）
        DataSet<Tuple2<String, Integer>> data = env.fromElements(
        new Tuple2("zhangsan", 1000), new Tuple2("lisi", 1001), 
        new Tuple2("zhangsan", 3000), new Tuple2("lisi", 1002));
        //4.1根据name进行分组
        DataSet<Tuple2<String, Integer>> data2 = data.groupBy(0).reduceGroup(
        new GroupReduceFunction<Tuple2<String, Integer>, Tuple2<String, Integer>>() {
            @Override
            public void reduce(Iterable<Tuple2<String, Integer>> iterable, 
            Collector<Tuple2<String, Integer>> collector) throws Exception {
                int salary = 0;
                String name = "";
                Iterator<Tuple2<String, Integer>> itor = iterable.iterator();
                //4.2统计每个人的工资总和
                while (itor.hasNext()) {
                    Tuple2<String, Integer> t = itor.next();
                    name = t.f0;
                    salary += t.f1;
                }
                collector.collect(new Tuple2(name, salary));
            }
        });
        data2.print();
    }
}
```
####执行结果：
```scala
text3.print()
28

text3.print()
(12,16)

data2.print
(lisi,2003)
(zhangsan,4000)
```



##Join
```
join将两个DataSet按照一定的关联度进行类似SQL中的Join操作。
```
####执行程序：
```java
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
        DataSet<Tuple4<String, String, String, String>> text2 = authors.join(posts).where(1).
        equalTo(1).with(new JoinFunction<Tuple3<String, String, String>, Tuple2<String, String>, 
        Tuple4<String, String, String, String>>() {
            @Override
            public Tuple4<String, String, String, String> join(Tuple3<String, String, String> author, 
            Tuple2<String, String> post) throws Exception {
                //AuthorArchive (title, id, name, email)
                return new Tuple4<>(post.f0, author.f0, author.f1, author.f2);
            }
        });

        //3.显示结果
        text2.print();
    }
}
```
####执行结果：
```java
text2.print();
(P003,A001,wangwu,wangwu@qq.com)
(P001,A001,zhangsan,zhangsan@qq.com)
(P002,A001,lisi,lisi@qq.com)
(P004,A001,lisi,lisi@qq.com)
```


##CoGroup
```
将2个DataSet中的元素，按照key进行分组，一起分组2个DataSet。而groupBy值能分组一个DataSet
```
####执行程序：
```java
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
        //Post (title, author name)
        DataSet<Tuple2<String, String>> posts = env.fromElements(
                new Tuple2<>("P001", "zhangsan"),
                new Tuple2<>("P002", "lisi"),
                new Tuple2<>("P003", "wangwu"),
                new Tuple2<>("P004", "lisi")

        );
        // 2.用自定义的方式进行coGroup操作,将相同name的Author和Post协同分组。
        DataSet<Tuple4<String, String, String, String text2 = authors.coGroup(posts).where(1)
        .equalTo(1).with(new CoGroupFunction<Tuple3<String, String, String>, Tuple2<String, String>,
        Tuple4<String, String, String, String>>() {

            @Override
            public void coGroup(Iterable<Tuple3<String, String, String>> authors,
            Iterable<Tuple2<String,String>> posts,Collector<Tuple4<String,String,String,String>> collector)
            throws Exception {
                //取出Author信息
                Tuple3<String, String, String> at = null;
                Iterator<Tuple3<String, String, String>> aitor = authors.iterator();
                while (aitor.hasNext()) {
                    at = aitor.next();
                }
                //取出Post信息
                Tuple2<String, String> pt = null;
                Iterator<Tuple2<String, String>> pitor = posts.iterator();
                while (pitor.hasNext()) {
                    pt = pitor.next();
                }
                //重新组装并发送AuthorPost信息
                collector.collect(new Tuple4<>(pt.f0, at.f0, at.f1, at.f2));
            }
        });

        //3.显示结果
        text2.print();
    }
}
```
####执行结果：
```java
text2.print();
(P003,A001,wangwu,wangwu@qq.com)
(P004,A001,lisi,lisi@qq.com)
(P001,A001,zhangsan,zhangsan@qq.com)
```
















##XXXX
```
```
####执行程序：
```java

```
####执行结果：
```java
```




##XXXX
```
```
####执行程序：
```java

```
####执行结果：
```java
```




##XXXX
```
```
####执行程序：
```java

```
####执行结果：
```java
```




##XXXX
```
```
####执行程序：
```java

```
####执行结果：
```java
```




##XXXX
```
```
####执行程序：
```java

```
####执行结果：
```java
```




##XXXX
```
```
####执行程序：
```java

```
####执行结果：
```java
```




##XXXX
```
```
####执行程序：
```java

```
####执行结果：
```java
```




##XXXX
```
```
####执行程序：
```java

```
####执行结果：
```java
```




##XXXX
```
```
####执行程序：
```java

```
####执行结果：
```java
```




##XXXX
```
```
####执行程序：
```java

```
####执行结果：
```java
```




##XXXX
```
```
####执行程序：
```java

```
####执行结果：
```java
```




##XXXX
```
```
####执行程序：
```java

```
####执行结果：
```java
```




##XXXX
```
```
####执行程序：
```java

```
####执行结果：
```java
```




##XXXX
```
```
####执行程序：
```java

```
####执行结果：
```java
```




##XXXX
```
```
####执行程序：
```java

```
####执行结果：
```java
```




##XXXX
```
```
####执行程序：
```java

```
####执行结果：
```java
```




##XXXX
```
```
####执行程序：
```java

```
####执行结果：
```java
```




##XXXX
```
```
####执行程序：
```java

```
####执行结果：
```java
```


