package code.book.batch.dataset.advance.api

import org.apache.flink.api.scala.{ExecutionEnvironment, _}

object JoinFunction001scala {
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
    // 2.scala中没有with方法来使用JoinFunction
    val text2 = authors.join(posts).where(1).equalTo(1)

    //3.显示结果
    text2.print()
  }
}
