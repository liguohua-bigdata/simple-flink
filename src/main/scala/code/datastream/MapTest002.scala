package code.datastream

import org.apache.flink.streaming.api.scala._

object MapTest002 {
  def main(args: Array[String]): Unit = {
    //1.创建流处理环境
    val senv = StreamExecutionEnvironment.getExecutionEnvironment

    //2.准备数据
    val text = senv.fromElements((18, 4), (19, 5), (23, 6), (38, 3))

    //3.执行运算
    val result = text.map { pair => (pair._1*2 , pair._2 )}

    //4.将结果打印出来
    result.print()

    //5.触发流计算
    senv.execute(this.getClass.getName)
  }
}
