package code.book.batch.sinksource.scala

import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment, _}

object DataSink000 {
  def main(args: Array[String]): Unit = {
    //1.定义环境
    val env = ExecutionEnvironment.getExecutionEnvironment
    //2.定义数据 stu(age,name,height)
    val stu: DataSet[(Int, String, Double)] = env.fromElements(
      (19, "zhangsan", 178.8),
      (17, "lisi", 168.8),
      (18, "wangwu", 184.8),
      (21, "zhaoliu", 164.8)
    )
    //3.sink到标准输出
    stu.print

    //3.sink到标准error输出
    stu.printToErr()

    //4.sink到本地Collection
    print(stu.collect())
  }
}
