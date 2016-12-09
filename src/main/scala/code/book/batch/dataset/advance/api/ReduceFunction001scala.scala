package code.book.batch.dataset.advance.api

import org.apache.flink.api.common.functions.ReduceFunction
import org.apache.flink.api.scala.{ExecutionEnvironment, _}

object ReduceFunction001scala {
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
