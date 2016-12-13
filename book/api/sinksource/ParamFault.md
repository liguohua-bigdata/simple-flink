#一、flink中的参数传递
```
flink中支持向Function传递参数，常见的有两种方式，
1.通过构造方法向Function传递参数
2.通过ExecutionConfig向Function传递参数
```
##1.通过构造方法向Function传递参数(基本数据)
###执行程序
```scala
package code.book.batch.sinksource.scala

import org.apache.flink.api.common.functions.MapFunction
import org.apache.flink.api.scala.{ExecutionEnvironment, _}

/**
  * Passing Parameters to Functions
  */
object Parameters001 {
  def main(args: Array[String]): Unit = {
    val env = ExecutionEnvironment.getExecutionEnvironment

    //1.准备工资数据
    case class Worker(name: String, salaryPerMonth: Double)
    val salary = env.fromElements(2123.5, 4345.2, 5987.3, 7991.2)

    //2.准备补助数据
    val bouns = 38.111

    //3.计算工资和补助之和
    salary.map(new SalarySumMap(bouns)).print()
    class SalarySumMap(b: Double) extends MapFunction[Double, Double] {
      override def map(s: Double): Double = {
        //工资+补助
        s + b
      }
    }
  }
}
```
####执行效果
```
2161.611
4383.311
6025.411
8029.311
```
##2.通过构造方法向Function传递参数(复合数据)
###执行程序
```scala
package code.book.batch.sinksource.scala
import org.apache.flink.api.common.functions.MapFunction
import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment, _}
object Parameters002 {
  def main(args: Array[String]): Unit = {
    val env = ExecutionEnvironment.getExecutionEnvironment

    //1.准备工人数据
    case class Worker(name: String, salaryPerMonth: Double)
    val workers: DataSet[Worker] = env.fromElements(
      Worker("zhagnsan", 1356.67),
      Worker("lisi", 1476.67)
    )
    //2.准备工作月份数据，作为参数传递出去
    val month = 4

    //3.接受参数进行计算
    workers.map(new SalarySumMap(month)).print()
    class SalarySumMap(m: Int) extends MapFunction[Worker, Worker] {
      override def map(w: Worker): Worker = {
        //取出参数，取出worker进行计算
        Worker(w.name, w.salaryPerMonth * m)
      }
    }
  }
}
```
####执行效果
```
Worker(zhagnsan,5426.68)
Worker(lisi,5906.68)
```

##3.通过ExecutionConfig向Function传递参数
###执行程序
```scala
package code.book.batch.sinksource.scala

import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment, _}
import org.apache.flink.configuration.Configuration

/**
  * Globally via the ExecutionConfig
  * *
  * Flink also allows to pass custom configuration values to the ExecutionConfig
  * interface of the environment. Since the execution config is accessible in all
  * (rich) user functions, the custom configuration will be available globally in all functions.
  */
object Parameters003scala {
  def main(args: Array[String]): Unit = {
    val env = ExecutionEnvironment.getExecutionEnvironment

    //1.准备工人数据
    case class Worker(name: String, salaryPerMonth: Double)
    val workers: DataSet[Worker] = env.fromElements(
      Worker("zhagnsan", 1356.67),
      Worker("lisi", 1476.67)
    )
    //2.准备工作月份数据，作为参数用Configuration传递出去
    val conf = new Configuration()
    conf.setString("month", "4")
    env.getConfig.setGlobalJobParameters(conf)

    //3.接受参数进行计算（如果要用Configuration传参，需要用RichFunction接受）
    workers.map(new RichMapFunction[Worker, Worker] {
      private var m = 0

      override def open(parameters: Configuration): Unit = {
        super.open(parameters)
        //3.1获取Configuration传递过来的参数
        val globalParams = this.getRuntimeContext.getExecutionConfig.getGlobalJobParameters
        m = globalParams.toMap.get("month").trim.toInt
      }

      override def map(w: Worker): Worker = {
        //3.2计算最新工人工资信息
        Worker(w.name, w.salaryPerMonth * m)
      }
    }).print
  }
}
```
####执行效果
```
Worker(zhagnsan,5426.68)
Worker(lisi,5906.68)
```

#二、flink中的容错设置
```
flink支持容错设置,当操作失败了，可以在指定重试的启动时间和重试的次数.有两种设置方式
1.通过配置文件，进行全局的默认设定
2.通过程序的api进行设定。
```

##1.通过配置flink-conf.yaml来设定全局容错
```
设定出错重试3次
execution-retries.default: 3

设定重试间隔时间5秒
execution-retries.delay: 5 s
```

##2.程序的api进行容错设定
```
flink支持通过api设定容错信息
//失败重试3次
env.setNumberOfExecutionRetries(3)
//重试时延 5000 milliseconds
env.getConfig.setExecutionRetryDelay(5000)
```

###执行程序
```scala
package code.book.batch.sinksource.scala

import org.apache.flink.api.scala.{ExecutionEnvironment, _}

object FaultTolerance001 {
  def main(args: Array[String]): Unit = {
    val env = ExecutionEnvironment.getExecutionEnvironment
    //失败重试3次
    env.setNumberOfExecutionRetries(3)
    //重试时延 5000 milliseconds
    env.getConfig.setExecutionRetryDelay(5000)
    val ds1 = env.fromElements(2, 5, 3, 7, 9)
    ds1.map(_ * 2).print()
  }
}
```

