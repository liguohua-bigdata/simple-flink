package code.book.stream.datastreamapi

import org.apache.flink.streaming.api.scala._

object Project001 {
  def main(args: Array[String]): Unit = {
    //1.创建流处理环境
    val senv = StreamExecutionEnvironment.getExecutionEnvironment

    //2.准备数据
    val stu:DataStream[(String,Int,Int)] = senv.fromElements(("zhangsan", 15, 137), ("lisi", 21, 154), ("wagnwu", 16, 145))


    //3.执行运算
//    val result =stu.project(2,0)

    //4.将结果打印出来
//    result.print()

    //5.触发流计算
    senv.execute(this.getClass.getName)
  }
}
