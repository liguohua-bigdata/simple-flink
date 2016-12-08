package code.book.batch.dataset.advance.api

import org.apache.flink.api.common.functions.MapFunction
import org.apache.flink.api.scala.{ExecutionEnvironment, _}


object Map001scala {
  def main(args: Array[String]): Unit = {
    // 1.设置运行环境,并创造测试数据
    val env = ExecutionEnvironment.getExecutionEnvironment

    val text = env.fromElements("flink vs spark", "buffer vs  shuffer")

    // 3.以element为粒度，将element进行map操作，转化为大写并添加后缀字符串"--##bigdata##"
    val text2 = text.map(new MapFunction[String, String] {
      override def map(s: String): String = s.toUpperCase() + "--##bigdata##"
    })
    text2.print()

    // 4.以element为粒度，将element进行map操作，转化为大写并,并计算line的长度。
    val text3 = text.map(new MapFunction[String, (String, Int)] {
      override def map(s: String): (String, Int) = (s.toUpperCase(), s.length)
    })
    text3.print()
  }
}
