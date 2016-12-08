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
    //4.1根据name进行分组
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
