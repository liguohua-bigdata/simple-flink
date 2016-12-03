package code.book.stream.streamwc

//0.引用必要的元素
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time

object SocketWC {
  def main(args: Array[String]): Unit = {
    //1.创建运行环境
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    //2.定义数据流来源
    val text = env.socketTextStream("qingcheng11", 9999)
    //3.进行wordcount计算
    val counts = text.flatMap(_.toLowerCase.split("\\W+") filter (_.nonEmpty))
      .map((_, 1))
      .keyBy(0)
      .timeWindow(Time.seconds(5))
      .sum(1)

    //4.打印结果
    counts.print

    //5.触发计算
    env.execute("Window Stream WordCount")
  }
}
