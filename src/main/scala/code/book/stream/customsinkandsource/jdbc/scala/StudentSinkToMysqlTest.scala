package code.book.stream.customsinkandsource.jdbc.scala

import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}

object StudentSinkToMysqlTest {
  def main(args: Array[String]): Unit = {

    //1.创建流执行环境
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    //2.准备数据
    val dataStream:DataStream[Student] = env.fromElements(
      Student(5, "dahua", "beijing biejing", "female"),
      Student(6, "daming", "tainjing tianjin", "male "),
      Student(7, "daqiang ", "shanghai shanghai", "female")
    )

    //3.将数据写入到自定义的sink中（这里是mysql）
    dataStream.addSink(new StudentSinkToMysql)

    //4.触发流执行
    env.execute()
  }
}
