package code.book.batch.dataset.advance.api

import org.apache.flink.api.common.functions.FlatMapFunction
import org.apache.flink.api.scala.{ExecutionEnvironment, _}
import org.apache.flink.util.Collector
object FlatMapFunction001scala {
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
