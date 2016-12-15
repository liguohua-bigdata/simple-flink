package code.book.stream.datastreamapi

import org.apache.flink.streaming.api.scala._


object Fold001 {
  def main(args: Array[String]): Unit = {
    //1.创建流处理环境
    val senv = StreamExecutionEnvironment.getExecutionEnvironment

    //2.准备数据
    val text = senv.fromElements(
      ("zhangsan", 99),
      ("lisi", 4),
      ("zhangsan", 99),
      ("lisi", 4))

    //3.执行运算
//    val result = text.keyBy(0).fold("work")((para, element) => {
//      para + "-" + element
//    }).print()


    val result2 = text.keyBy(0).fold(0)((para, element) => {
      para + element._2
    }) print()
    //5.触发流计算
    senv.execute(this.getClass.getName)
  }
}
