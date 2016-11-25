
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

    //3.执行运算
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

    //3.执行运算
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
