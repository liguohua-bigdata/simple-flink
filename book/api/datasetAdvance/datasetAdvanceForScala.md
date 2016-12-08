#一、Flink DateSet定制API详解(Scala版)
##Map
###执行程序：
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
###执行结果：
```scala
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






##XXXX
###执行程序：
```scala
```
###执行结果：
```scala
```

##XXXX
###执行程序：
```scala
```
###执行结果：
```scala
```


##XXXX
###执行程序：
```scala
```
###执行结果：
```scala
```



##XXXX
###执行程序：
```scala
```
###执行结果：
```scala
```



##XXXX
###执行程序：
```scala
```
###执行结果：
```scala
```



##XXXX
###执行程序：
```scala
```
###执行结果：
```scala
```



##XXXX
###执行程序：
```scala
```
###执行结果：
```scala
```


##XXXX
###执行程序：
```scala
```
###执行结果：
```scala
```



##XXXX
###执行程序：
```scala
```
###执行结果：
```scala
```


##XXXX
###执行程序：
```scala
```
###执行结果：
```scala
```


##XXXX
###执行程序：
```scala
```
###执行结果：
```scala
```


##XXXX
###执行程序：
```scala
```
###执行结果：
```scala
```



##XXXX
###执行程序：
```scala
```
###执行结果：
```scala
```



##XXXX
###执行程序：
```scala
```
###执行结果：
```scala
```


##XXXX
###执行程序：
```scala
```
###执行结果：
```scala
```


##XXXX
###执行程序：
```scala
```
###执行结果：
```scala
```



##XXXX
###执行程序：
```scala
```
###执行结果：
```scala
```


##XXXX
###执行程序：
```scala
```
###执行结果：
```scala
```



##XXXX
###执行程序：
```scala
```
###执行结果：
```scala
```






