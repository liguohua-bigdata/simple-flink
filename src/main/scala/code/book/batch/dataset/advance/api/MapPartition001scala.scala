package code.book.batch.dataset.advance.api

import java.lang.Iterable

import org.apache.flink.api.common.functions.MapPartitionFunction
import org.apache.flink.api.scala.{ExecutionEnvironment, _}
import org.apache.flink.util.Collector

object MapPartition001scala {
  def main(args: Array[String]): Unit = {
    // 1.设置运行环境
    val env = ExecutionEnvironment.getExecutionEnvironment

    //2.创造测试数据
    val text = env.fromElements("flink vs spark", "buffer vs  suffer")

    //3.以partition为粒度，进行map操作，计算element个数
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
    //4.以partition为粒度，进行map操作，转化element内容
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
  }

}
