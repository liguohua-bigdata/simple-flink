#一、Flink DateSet定制API详解(Scala版)
##Map
```
以element为粒度，对element进行1：1的转化
```

####执行程序：
```scala
package code.book.batch.dataset.advance.api

import org.apache.flink.api.common.functions.MapFunction
import org.apache.flink.api.scala.{ExecutionEnvironment, _}

object Map001scala {
  def main(args: Array[String]): Unit = {
    // 1.设置运行环境,并创造测试数据
    val env = ExecutionEnvironment.getExecutionEnvironment
    val text = env.fromElements("flink vs spark", "buffer vs  shuffer")

    // 2.以element为粒度，将element进行map操作，转化为大写并添加后缀字符串"--##bigdata##"
    val text2 = text.map(new MapFunction[String, String] {
      override def map(s: String): String = s.toUpperCase() + "--##bigdata##"
    })
    text2.print()

    // 3.以element为粒度，将element进行map操作，转化为大写并,并计算line的长度。
    val text3 = text.map(new MapFunction[String, (String, Int)] {
      override def map(s: String): (String, Int) = (s.toUpperCase(), s.length)
    })
    text3.print()

    // 4.以element为粒度，将element进行map操作，转化为大写并,并计算line的长度。
    //4.1定义class
    case class Wc(line: String, lenght: Int)
    //4.2转化成class类型
    val text4 = text.map(new MapFunction[String, Wc] {
      override def map(s: String): Wc = Wc(s.toUpperCase(), s.length)
    })
    text4.print()
  }
}

```
####执行结果：
```scala
text2.print();
FLINK VS SPARK--##bigdata##
BUFFER VS  SHUFFER--##bigdata##

text3.print();
(FLINK VS SPARK,14)
(BUFFER VS  SHUFFER,18)

text4.print();
Wc(FLINK VS SPARK,14)
Wc(BUFFER VS  SHUFFER,18)
```

##mapPartition
```
以partition为粒度，对element进行1：1的转化。有时候会比map效率高。
```
####执行程序：
```scala
package code.book.batch.dataset.advance.api

import java.lang.Iterable
import org.apache.flink.api.common.functions.{MapFunction, MapPartitionFunction}
import org.apache.flink.api.scala.{ExecutionEnvironment, _}
import org.apache.flink.util.Collector

object MapPartition001scala {
  def main(args: Array[String]): Unit = {
    // 1.设置运行环境,创造测试数据
    val env = ExecutionEnvironment.getExecutionEnvironment
    val text = env.fromElements("flink vs spark", "buffer vs  shuffer")

    //2.以partition为粒度，进行map操作，计算element个数
    val text2 = text.mapPartition(new MapPartitionFunction[String, Long]() {
      override def mapPartition(iterable: Iterable[String], collector: Collector[Long]): Unit = {
        var c = 0
        val itor = iterable.iterator()
        while (itor.hasNext) {
          itor.next()
          c = c + 1
        }
        collector.collect(c)
      }
    })
    text2.print()
    //3.以partition为粒度，进行map操作，转化element内容
    val text3 = text.mapPartition(partitionMapper = new MapPartitionFunction[String, String]() {
      override def mapPartition(iterable: Iterable[String], collector: Collector[String]): Unit = {
        val itor = iterable.iterator()
        while (itor.hasNext) {
          val line = itor.next().toUpperCase + "--##bigdata##"
          collector.collect(line)
        }
      }
    })
    text3.print()

    //4.以partition为粒度，进行map操作，转化为大写并,并计算line的长度。
    //4.1定义class
    case class Wc(line: String, lenght: Int)
    //4.2转化成class类型
    val text4 = text.map(new MapFunction[String, Wc] {
      override def map(s: String): Wc = Wc(s.toUpperCase(), s.length)
    })
    text4.print()
  }
}
```
####执行结果：
```scala
text2.print();
2

text3.print();
FLINK VS SPARK--##bigdata##
BUFFER VS  SHUFFER--##bigdata##

text4.print();
Wc(FLINK VS SPARK,14)
Wc(BUFFER VS  SHUFFER,18)
```

##flatMap
```
以element为粒度，对element进行1：n的转化。
```
####执行程序：
```scala
package code.book.batch.dataset.advance.api

import org.apache.flink.api.common.functions.FlatMapFunction
import org.apache.flink.api.scala.{ExecutionEnvironment, _}
import org.apache.flink.util.Collector
object FlatMap001scala {
  def main(args: Array[String]): Unit = {
    // 1.设置运行环境,并创造测试数据
    val env = ExecutionEnvironment.getExecutionEnvironment
    val text = env.fromElements("flink vs spark", "buffer vs  shuffer")

    // 2.以element为粒度，将element进行map操作，转化为大写并添加后缀字符串"--##bigdata##"
    val text2 = text.flatMap(new FlatMapFunction[String, String]() {
      override def flatMap(s: String, collector: Collector[String]): Unit = {
        collector.collect(s.toUpperCase() + "--##bigdata##")
      }
    })
    text2.print()

    //3.对每句话进行单词切分,一个element可以转化为多个element，这里是一个line可以转化为多个Word
    //map的只能对element进行1：1转化，而flatMap可以对element进行1：n转化
    val text3 = text.flatMap {
      new FlatMapFunction[String, Array[String]] {
        override def flatMap(s: String, collector: Collector[Array[String]]): Unit = {
          val arr: Array[String] = s.toUpperCase().split("\\s+")
          collector.collect(arr)
        }
      }
    }
    //显示结果的简单写法
    text3.collect().foreach(_.foreach(println(_)))
    //实际上是先获取Array[String],再从中获取到String
    text3.collect().foreach(arr => {
      arr.foreach(token => {
        println(token)
      })
    })
  }
}
```
####执行结果：
```scala
text2.print()
FLINK VS SPARK--##bigdata##
BUFFER VS  SHUFFER--##bigdata##

text3.collect().foreach(_.foreach(println(_)))
FLINK
VS
SPARK
BUFFER
VS
```













##XXXX
```
```
####执行程序：
```scala
```
####执行结果：
```scala
```


##XXXX
```
```
####执行程序：
```scala
```
####执行结果：
```scala
```



##XXXX
```
```
####执行程序：
```scala
```
####执行结果：
```scala
```



##XXXX
```
```
####执行程序：
```scala
```
####执行结果：
```scala
```

