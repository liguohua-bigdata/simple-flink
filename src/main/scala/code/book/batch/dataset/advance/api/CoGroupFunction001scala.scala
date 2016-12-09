package code.book.batch.dataset.advance.api

import org.apache.flink.api.common.functions.FlatMapFunction
import org.apache.flink.api.scala.{ExecutionEnvironment, _}
import org.apache.flink.util.Collector

object CoGroupFunction001scala {
  def main(args: Array[String]): Unit = {
    // 1.设置运行环境,并创造测试数据
    val env = ExecutionEnvironment.getExecutionEnvironment

    val authors = env.fromElements(
      Tuple3("A001", "zhangsan", "zhangsan@qq.com"),
      Tuple3("A001", "lisi", "lisi@qq.com"),
      Tuple3("A001", "wangwu", "wangwu@qq.com"))
    val posts = env.fromElements(
      Tuple2("P001", "zhangsan"),
      Tuple2("P002", "lisi"),
      Tuple2("P003", "wangwu"),
      Tuple2("P004", "lisi"))
    // 2.scala中coGroup没有with方法来使用CoGroupFunction
    val text2 = authors.coGroup(posts).where(1).equalTo(1)

    //3.显示结果
    val text3 = text2.flatMap(new FlatMapFunction[(Array[(String, String, String)], Array[(String, String)]), Tuple4[String, String, String, String]] {
      override def flatMap(t: (Array[(String, String, String)], Array[(String, String)]), collector: Collector[(String, String, String, String)]): Unit = {


        val arr1 = t._1
        var id = ""
        var name = ""
        var email = ""


        for (a <- arr1) {
          id = a._1
          name = a._1
          email = a._1
        }

        val arr2 = t._2
        var title = ""
        for (p <- arr2) {
          title = p._1
        }
        collector.collect(Tuple4(id, name, email, title))
      }
    })

    text3.print()

  }
}
