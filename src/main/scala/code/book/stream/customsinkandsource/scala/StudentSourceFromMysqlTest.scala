package code.book.stream.customsinkandsource.scala

import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}
object StudentSourceFromMysqlTest {
  def main(args: Array[String]): Unit = {
    //1.创建流执行环境
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    //2.从自定义source中读取数据
    val dataStream: DataStream[Student] = env.addSource(new StudentSourceFromMysql)

    //3.显示结果
    dataStream.print()

    //4.触发流执行
    env.execute()
  }
}
