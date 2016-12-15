package code.book.stream.mutilstream

//0.引用必要的元素
import java.util.Date

import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time

object MutilSteam0001 {
  def main(args: Array[String]): Unit = {
    //1.创建运行环境
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    //2.定义worker数据流
    val text1 = env.socketTextStream("qingcheng11", 9999)
    case class Worker(name: String, age: Int)
    val ds1: DataStream[Worker] = text1.map(line => {
      val spits = line.split(",")
      Worker(spits(0), spits(1).toInt)
    }).assignAscendingTimestamps(w => new Date().getTime)
    //3.定义salary数据流
    val text2 = env.socketTextStream("qingcheng11", 9998)
    case class Salary(salary: Double, name: String)
    val ds2: DataStream[Salary] = text2.map(line => {
      val spits = line.split(",")
      Salary(spits(0).toDouble, spits(1))
    }).assignAscendingTimestamps(s => new Date().getTime)


    //4.打印结果


     val re=ds1.join(ds2).where(_.name).equalTo(_.name).window(TumblingEventTimeWindows.of(Time.seconds(3)))

    print(re)

    //5.触发计算
    env.execute("Window Stream WordCount")
  }
}
