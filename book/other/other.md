##1.在flink中如何进行模糊读取
```
hdfs://qingcheng12:9000/bigData/*/20161214/*
```
示例程序
```
package code.book.other

import java.lang.Iterable
import org.apache.flink.api.common.functions.MapPartitionFunction
import org.apache.flink.api.scala._
import org.apache.flink.configuration.Configuration
import org.apache.flink.util.Collector
import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat

object ReadRegxPathDemo {
  def main(args: Array[String]): Unit = {
    //1.创建执行环境
    val env = ExecutionEnvironment.getExecutionEnvironment
    //2.创建配置
    val config = new Configuration()
    config.setBoolean("recursive.file.enumeration", true)
    //3.定义数据
    val path = "hdfs://qingcheng12:9000/input/spark/README.md"
    val texLines1: DataSet[(LongWritable, Text)] = env.readHadoopFile(new TextInputFormat,
    classOf[LongWritable], classOf[Text], path).withParameters(config)
    //4.读取数据
    val texLines2 = texLines1.mapPartition(new MapPartitionFunction[(LongWritable, Text), String]() {
      override def mapPartition(iterable: Iterable[(LongWritable, Text)],
      collector: Collector[String]): Unit = {
        val itor = iterable.iterator
        while (itor.hasNext) {
          //text
          val line = itor.next()._2.toString()
          if (line!=null){
            collector.collect(line)
          }
        }
      }
    })
    //5.显示数据
    texLines2.distinct().print()
  }
}
```
