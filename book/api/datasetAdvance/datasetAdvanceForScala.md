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
SHUFFLE
```

##filter
```
以element为粒度，对element进行过滤操作。将满足过滤条件的element组成新的DataSet
```
####执行程序：
```scala
package code.book.batch.dataset.advance.api

import org.apache.flink.api.common.functions.FilterFunction
import org.apache.flink.api.scala.{ExecutionEnvironment, _}

object Filter001scala {
  def main(args: Array[String]): Unit = {
    // 1.设置运行环境,并创造测试数据
    val env = ExecutionEnvironment.getExecutionEnvironment
    val text = env.fromElements(2, 4, 7, 8, 9, 6)

    //2.对DataSet的元素进行过滤，筛选出偶数元素
    val text2 = text.filter(new FilterFunction[Int] {
      override def filter(t: Int): Boolean = {
        t % 2 == 0
      }
    })
    text2.print()

    //3.对DataSet的元素进行过滤，筛选出大于5的元素
    val text3 = text.filter(new FilterFunction[Int] {
      override def filter(t: Int): Boolean = {
        t >5
      }
    })
    text3.print()
  }
}
```
####执行结果：
```scala
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
```scala
package code.book.batch.dataset.advance.api

import org.apache.flink.api.common.functions.ReduceFunction
import org.apache.flink.api.scala.{ExecutionEnvironment, _}

object Reduce001scala {
  def main(args: Array[String]): Unit = {
    // 1.设置运行环境,并创造测试数据
    val env = ExecutionEnvironment.getExecutionEnvironment
    val text = env.fromElements(1, 2, 3, 4, 5, 6, 7)

    //2.对DataSet的元素进行合并，这里是计算累加和
    val text2 = text.reduce(new ReduceFunction[Int] {
      override def reduce(intermediateResult: Int, next: Int): Int = {
        intermediateResult + next
      }
    })
    text2.print()

    //3.对DataSet的元素进行合并，这里是计算累乘积
    val text3 = text.reduce(new ReduceFunction[Int] {
      override def reduce(intermediateResult: Int, next: Int): Int = {
        intermediateResult * next
      }
    })
    text3.print()

    //4.对DataSet的元素进行合并，逻辑可以写的很复杂
    val text4 = text.reduce(new ReduceFunction[Int] {
      override def reduce(intermediateResult: Int, next: Int): Int = {
        if (intermediateResult % 2 == 0) {
          intermediateResult + next
        } else {
          intermediateResult * next
        }
      }
    })
    text4.print()

    //5.对DataSet的元素进行合并，可以看出intermediateResult是临时合并结果，next是下一个元素
    val text5 = text.reduce(new ReduceFunction[Int] {
      override def reduce(intermediateResult: Int, next: Int): Int = {
        println("intermediateResult=" + intermediateResult + " ,next=" + next)
        intermediateResult + next
      }
    })
    text5.collect()
  }
}
```
####执行结果：
```scala
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
```scala
package code.book.batch.dataset.advance.api

import java.lang.Iterable
import org.apache.flink.api.common.functions.GroupReduceFunction
import org.apache.flink.api.scala.{ExecutionEnvironment, _}
import org.apache.flink.util.Collector

object ReduceGroup001scala {
  def main(args: Array[String]): Unit = {
    // 1.设置运行环境,并创造测试数据
    val env = ExecutionEnvironment.getExecutionEnvironment
    val text = env.fromElements(1, 2, 3, 4, 5, 6, 7)

    //2.对DataSet的元素进行分组合并，这里是计算累加和
    val text2 = text.reduceGroup(new GroupReduceFunction[Int, Int] {
      override def reduce(iterable: Iterable[Int], collector: Collector[Int]): Unit = {
        var sum = 0
        val itor = iterable.iterator()
        while (itor.hasNext) {
          sum += itor.next()
        }
        collector.collect(sum)
      }
    })
    text2.print()

    //3.对DataSet的元素进行分组合并，这里是分别计算偶数和奇数的累加和
    val text3 = text.reduceGroup(new GroupReduceFunction[Int, (Int, Int)] {
      override def reduce(iterable: Iterable[Int], collector: Collector[(Int, Int)]): Unit = {
        var sum0 = 0
        var sum1 = 0
        val itor = iterable.iterator()
        while (itor.hasNext) {
          val v = itor.next
          if (v % 2 == 0) {
            //偶数累加和
            sum0 += v
          } else {
            //奇数累加和
            sum1 += v
          }
        }
        collector.collect(sum0, sum1)
      }
    })
    text3.print()

    //4.对DataSet的元素进行分组合并，这里是对分组后的数据进行合并操作，统计每个人的工资总和（每个分组会合并出一个结果）
    val data = env.fromElements(("zhangsan", 1000), ("lisi", 1001), ("zhangsan", 3000), ("lisi", 1002))
    //4.1根据name进行分组，
    val data2 = data.groupBy(0).reduceGroup(new GroupReduceFunction[(String, Int), (String, Int)] {
      override def reduce(iterable: Iterable[(String, Int)], collector: Collector[(String, Int)]): Unit = {
        var salary = 0
        var name = ""
        val itor = iterable.iterator()
        //4.2统计每个人的工资总和
        while (itor.hasNext) {
          val t = itor.next()
          name = t._1
          salary += t._2
        }
        collector.collect(name, salary)
      }
    })
    data2.print
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

