package code.book.wordcount.stream

import org.apache.flink.streaming.api.scala._

/**
  * Created by liguohua on 25/11/2016.
  */
object WordCountHdfsFile {

  def main(args: Array[String]): Unit = {
    //1.创建流处理环境
    val senv = StreamExecutionEnvironment.getExecutionEnvironment

    //2.准备数据
    val text = senv.readTextFile("hdfs:///input/flink/README.txt");

    //3.执行运算
    val counts = text.flatMap(_.toLowerCase.split("\\W+")).map((_, 1)).keyBy(0).sum(1)


    //4.将结果打印出来
    counts.print()

    //5.触发流计算
    senv.execute("Flink Streaming Wordcount")
  }

}
