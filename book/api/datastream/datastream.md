
#一、Flink DateStream API详解

##map
```
转化DateStream中的每一个元素。
```
###map示例程序一：
执行程序：
```scale
package code.datastream

import org.apache.flink.streaming.api.scala._

object MapTest001 {
  def main(args: Array[String]): Unit = {
    //1.创建流处理环境
    val senv = StreamExecutionEnvironment.getExecutionEnvironment

    //2.准备数据
    val text = senv.fromElements(1, 3, 2, 4, 6, 5)

    //3.执行map运算
    val result = text.map(_ * 2)

    //4.将结果打印出来
    result.print()

    //5.触发流计算
    senv.execute(this.getClass.getName)
  }
}
```
执行结果：
```scale
1> 2
2> 6
4> 8
3> 4
2> 10
1> 12
```


###map示例程序二：
执行程序：
```scale
package code.datastream

import org.apache.flink.streaming.api.scala._

object MapTest002 {
  def main(args: Array[String]): Unit = {
    //1.创建流处理环境
    val senv = StreamExecutionEnvironment.getExecutionEnvironment

    //2.准备数据
    val text = senv.fromElements((18, 4), (19, 5), (23, 6), (38, 3))

    //3.执行map运算
    val result = text.map { pair => (pair._1*2 , pair._2 )}

    //4.将结果打印出来
    result.print()

    //5.触发流计算
    senv.execute(this.getClass.getName)
  }
}
```
执行结果：
```scale
3> (46,6)
4> (76,3)
2> (38,5)
1> (36,4)
```


##flatMap
```
先进行flat操作，然后进行map操作。
```
执行程序：
```scale
package code.datastream

import org.apache.flink.streaming.api.scala._

object FlatMapTest001 {
  def main(args: Array[String]): Unit = {
    //1.创建流处理环境
    val senv = StreamExecutionEnvironment.getExecutionEnvironment

    //2.准备数据
    val text = senv.fromElements("zhangsan is a good boy", "lisi is a nice girl")

    //3.执行运算
    val result = text.flatMap { _.split(" ") }

    //4.将结果打印出来
    result.print()

    //5.触发流计算
    senv.execute(this.getClass.getName)
  }
}
```
执行结果：
```scale
2> lisi
1> zhangsan
1> is
1> a
1> good
1> boy
2> is
2> a
2> nice
2> girl
```


##filter
```
过滤满足条件的数据，不满足条件的数据将被丢弃。
```
执行程序：
```scale
package code.datastream

import org.apache.flink.streaming.api.scala._

object FilterTest001 {
  def main(args: Array[String]): Unit = {
    //1.创建流处理环境
    val senv = StreamExecutionEnvironment.getExecutionEnvironment

    //2.准备数据
    val text = senv.fromElements(1, 3, 2, 4, 6, 5)

    //3.执行运算
    val result = text.filter(_ % 2 == 0)

    //4.将结果打印出来
    result.print()

    //5.触发流计算
    senv.execute(this.getClass.getName)
  }
}
```
执行结果：
```scale
3> 2
1> 6
4> 4
```


##keyBy
```
将DataStream 转化为 KeyedStream
```
执行程序：
```scale
package code.datastream

import org.apache.flink.streaming.api.scala._

object KeyByTest001 {
  def main(args: Array[String]): Unit = {
    //1.创建流处理环境
    val senv = StreamExecutionEnvironment.getExecutionEnvironment

    //2.准备数据
    val text = senv.fromElements(("zhangsan",1200),("lisi",1200),("zhangsan",3200),("lisi",2200))

    //3.执行运算
    val result = text.keyBy(0)

    //4.将结果打印出来
    result.print()

    //5.触发流计算
    senv.execute(this.getClass.getName)
  }
}
```
执行结果：
```scale
4> (zhangsan,1200)
4> (lisi,1200)
4> (zhangsan,3200)
4> (lisi,2200)
```



##Reduce
```
将DataStream 转化为 KeyedStream
```
执行程序：
```scale
package code.datastream

import org.apache.flink.streaming.api.scala._

object KeyByTest001 {
  def main(args: Array[String]): Unit = {
    //1.创建流处理环境
    val senv = StreamExecutionEnvironment.getExecutionEnvironment

    //2.准备数据
    val text = senv.fromElements(("zhangsan",1200),("lisi",1200),("zhangsan",3200),("lisi",2200))

    //3.执行运算
    val result = text.keyBy(0)

    //4.将结果打印出来
    result.print()

    //5.触发流计算
    senv.execute(this.getClass.getName)
  }
}
```
执行结果：
```scale
4> (zhangsan,1200)
4> (lisi,1200)
4> (zhangsan,3200)
4> (lisi,2200)
```
